/*
 * Copyright 2010 Jean-Paul Balabanian and Yngve Devik Hammersland
 *
 *     This file is part of glsl4idea.
 *
 *     Glsl4idea is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as
 *     published by the Free Software Foundation, either version 3 of
 *     the License, or (at your option) any later version.
 *
 *     Glsl4idea is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with glsl4idea.  If not, see <http://www.gnu.org/licenses/>.
 */

package glslplugin.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.impl.DelegateMarker;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

import static glslplugin.lang.elements.GLSLElementTypes.PREPROCESSED_EMPTY;
import static glslplugin.lang.elements.GLSLTokenTypes.IDENTIFIER;
import static glslplugin.lang.elements.GLSLTokenTypes.PREPROCESSOR_BEGIN;

/**
 * Base of GLSLParsing to clearly divide helper and parsing methods.
 * This class contains the helper methods.
 *
 * @author Darkyen
 */
abstract class GLSLParsingBase {

    /**
     * The source of operatorTokens and the target for the AST nodes.
     * Do not use directly, use specialized proxy functions below, which can handle preprocessor
     * directives and macro replacements.
     */
    protected final PsiBuilder psiBuilder;

    GLSLParsingBase(PsiBuilder builder) {
        psiBuilder = builder;
    }

    //Code that deals with preprocessor, b.call replacements

    protected final HashMap<String, PreprocessorDropIn> defines = new HashMap<String, PreprocessorDropIn>();

    /** List of tokens to serve, because the real psiBuilder is currently on #define-d IDENTIFIER.
     * If null, psiBuilder is not there and everything is normal. */
    protected List<PreprocessorToken> preprocessorTokens = null;
    /** How far in preprocessorTokens is parser advanced. */
    protected int preprocessorTokensIndex = -1;
    /** Original text of `preprocessorTokens`. Used for UI and debug. */
    private String preprocessorTokensText = null;
    /** Value of preprocessorTokensText before calling consumePreprocessorTokens(). (For convenience) */
    protected String preprocessorTextOfConsumedTokens = null;

    private PreprocessorDropInType preprocessorTokensReplacementType = PreprocessorDropInType.UNKNOWN;

    /**
     * @return type of token lexer is at or null if eof
     */
    @Nullable
    protected final IElementType tokenType() {
        if(preprocessorTokens != null){
            return preprocessorTokens.get(preprocessorTokensIndex).type;
        } else {
            return psiBuilder.getTokenType();
        }
    }

    /**
     * @return type of token after [amount] times calling advanceLexer()
     */
    @Nullable
    protected final IElementType lookAhead(int amount) {
        return psiBuilder.lookAhead(amount); //TODO Implement for preprocessor
    }

    /**
     * Places a mark at token.
     * This mark MUST be closed by calling functions on it,
     * for example done() to complete the element,
     * error() to mark element as invalid (error on the mark, not {@link GLSLParsing#error(String)}!)
     * or drop() to cancel the mark altogether.
     *
     * @return placed mark
     */
    @NotNull
    protected final PsiBuilder.Marker mark() {
        //This will have to do.
        //If token is expanded to more tokens, it will be hard to mark mid-token.
        return new GLSLMarkerDelegate(psiBuilder.mark());
    }

    private final class GLSLMarkerDelegate extends DelegateMarker {

        private final int markPreprocessorTokensIndex;

        public GLSLMarkerDelegate(@NotNull PsiBuilder.Marker delegate) {
            super(delegate);
            markPreprocessorTokensIndex = preprocessorTokensIndex;
        }

        public GLSLMarkerDelegate(@NotNull PsiBuilder.Marker delegate, int markPreprocessorTokensIndex) {
            super(delegate);
            this.markPreprocessorTokensIndex = markPreprocessorTokensIndex;
        }

        @Override
        public void rollbackTo() {
            super.rollbackTo();
            //Reinitialize replacement tokens
            preprocessorTokens = null;
            preprocessorTokensIndex = -1;
            preprocessorTokensReplacementType = null;
            advanceLexer_initializePreprocessorToken(psiBuilder.getTokenType());
            preprocessorTokensIndex = markPreprocessorTokensIndex;
        }

        @Override
        public PsiBuilder.Marker precede() {
            return new GLSLMarkerDelegate(super.precede(), markPreprocessorTokensIndex);
        }
    }

    /**
     * Places an error at current lexer position.
     *
     * @param error message to be shown
     */
    protected final void error(String error) {
        //This is probably fine preprocessor-define wise.
        psiBuilder.error(error);
    }

    /**
     * Returns same as eof(), but does not complain about anything
     * and doesn't close anything.
     *
     * @return whether or not is the lexer at the end of the file
     */
    protected final boolean isEof() {
        //noinspection SimplifiableIfStatement
        if(preprocessorTokens != null && preprocessorTokensIndex < preprocessorTokens.size()){
            return false;
        }else return psiBuilder.eof();
    }

    /**
     * @return the text of current token
     */
    @Nullable
    protected final String getTokenText() {
        if(preprocessorTokens != null){
            return preprocessorTokens.get(preprocessorTokensIndex).text;
        } else {
            return psiBuilder.getTokenText();
        }
    }

    /**
     * Iff advanceLexer() just advanced into the preprocessor redefined tokens,
     * this method will return what type of redefinition should happen here.
     * If no type of replacement was recognized, type will be {@link PreprocessorDropInType#UNKNOWN}.
     *
     * Parsing methods can call this before doing their job, to find if it is already parsed.
     * If it is, they should mark the spot for special drop in element, consumePreprocessorTokens() and return true.
     * There is a shorthand to that, isTokenPreprocessorAlias(PreprocessorDropInType).
     *
     * @return Replacement type if just encountered the preprocessor-replaced block, null otherwise
     */
    @Nullable
    protected final PreprocessorDropInType getTokenPreprocessorAlias(){
        if(preprocessorTokens != null && preprocessorTokensIndex == 0){
            return preprocessorTokensReplacementType;
        }else{
            return null;
        }
    }

    /**
     * Checks if next token is preprocessor alias for given type.
     * If it is, consumes all injected tokens for that preprocessor token and return true.
     * Otherwise false.
     */
    protected final boolean isTokenPreprocessorAlias(PreprocessorDropInType type){
        if(getTokenPreprocessorAlias() == type){
            consumePreprocessorTokens();
            return true;
        }else{
            return false;
        }
    }

    /**
     * Advances lexer over all preprocessor-injected tokens.
     * Does nothing if not in preprocessor-injected tokens.
     */
    protected final void consumePreprocessorTokens(){
        if(preprocessorTokens != null){
            preprocessorTextOfConsumedTokens = preprocessorTokensText;
            preprocessorTokensIndex = preprocessorTokens.size();
            advanceLexer();
        }
    }

    private void advanceLexer_initializePreprocessorToken(final IElementType tokenType){
        boolean rerolling = (tokenType instanceof GLSLRedefinedTokenType);
        if (tokenType == IDENTIFIER || rerolling) {
            //It may be preprocessor defined identifier
            final String text = psiBuilder.getTokenText();
            final PreprocessorDropIn dropIn = defines.get(text);

            if (dropIn != null) {
                preprocessorTokensReplacementType = dropIn.type;
                final List<PreprocessorToken> tokens = dropIn.tokens;
                preprocessorTokensText = dropIn.text;
                //This IDENTIFIER has been #define-d to tokens
                if(!rerolling)psiBuilder.remapCurrentToken(new GLSLRedefinedTokenType(tokens));

                preprocessorTokens = tokens;
                preprocessorTokensIndex = 0;
            }// else There is nothing defined to this, continue normally
        }
    }

    /**
     * Advance lexer by one token.
     *
     * This goes through preprocessor-injected tokens,
     * skips empty-injected tokens and parses more preprocessor directives.
     */
    protected final void advanceLexer(){
        PsiBuilder.Marker emptyReplacementMarker = null;
        int advances = 0;
        do {
            advances++;
            if(advances == 2)emptyReplacementMarker = mark();
            boolean advancedRealLexer = false;

            if (preprocessorTokens != null) {
                //In defined mode
                //Go to next token
                preprocessorTokensIndex++;
                if (preprocessorTokensIndex >= preprocessorTokens.size()) {
                    //At the end, jump out of preprocessor mode and advance real lexer instead
                    preprocessorTokens = null;
                    preprocessorTokensReplacementType = null;
                    preprocessorTokensIndex = -1;
                    psiBuilder.advanceLexer();
                    advancedRealLexer = true;
                }
            } else {
                //Not in defined mode
                psiBuilder.advanceLexer();
                advancedRealLexer = true;
            }

            if (advancedRealLexer) {
                //Real lexer advanced, so check for directives and redefined tokens
                IElementType tokenType = psiBuilder.getTokenType();
                if (tokenType == PREPROCESSOR_BEGIN) {
                    parsePreprocessor();
                } else {
                    advanceLexer_initializePreprocessorToken(tokenType);
                }
            }
        } while (preprocessorTokensReplacementType == PreprocessorDropInType.EMPTY);
        if(emptyReplacementMarker != null){
            emptyReplacementMarker.done(PREPROCESSED_EMPTY);
        }
    }

    //Utility code

    /**
     * Checks whether lexer is at the end of the file,
     * complains about it if it is
     * and closes all marks supplied (if eof).
     *
     * @return psiBuilder.eof()
     */
    protected final boolean eof(PsiBuilder.Marker... marksToClose) {
        if (isEof()) {
            if (marksToClose.length > 0) {
                for (PsiBuilder.Marker mark : marksToClose) {
                    mark.error("Premature end of file.");
                }
            } else {
                error("Premature end of file.");
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifies that the current token is of the given type, if not it will flag an error.
     *
     * @param type  the expected token type.
     * @param error an appropriate error message if any other token is found instead.
     * @return indicates whether the match was successful or not.
     */
    protected final boolean match(IElementType type, String error) {
        if (isEof()) {
            return false;
        }
        boolean match = tokenType() == type;
        if (match) {
            advanceLexer();
        } else {
            error(error);
        }
        return match;
    }

    /**
     * Consumes the next token if it is of the given types, otherwise it is ignored.
     *
     * @param types the expected token types.
     * @return indicates whether the match was successful or not.
     */
    protected final boolean tryMatch(IElementType... types) {
        if (isEof()) {
            return false;
        }
        boolean match = false;
        for (IElementType type : types) {
            match |= tokenType() == type;
        }
        if (match) {
            advanceLexer();
        }
        return match;
    }

    /**
     * Consumes the next token if it is contained in the given token set, otherwise it is ignored.
     *
     * @param types a token set containing the expected token types.
     * @return indicates whether the match was successful or not.
     */
    protected final boolean tryMatch(TokenSet types) {
        boolean match = types.contains(tokenType());
        if (match) {
            advanceLexer();
        }
        return match;
    }

    // Interface to implementation

    protected abstract void parsePreprocessor();
}
