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
import com.intellij.lang.impl.PsiBuilderAdapter;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import glslplugin.lang.elements.GLSLElementTypes;
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
    protected final PsiBuilder b;

    GLSLParsingBase(PsiBuilder builder) {
        b = new GLSLPsiBuilderAdapter(builder);
    }

    private class GLSLPsiBuilderAdapter extends PsiBuilderAdapter {

        public GLSLPsiBuilderAdapter(PsiBuilder delegate) {
            super(delegate);
        }

        @Nullable
        @Override
        public IElementType getTokenType() {
            if (preprocessorTokens != null) {
                return preprocessorTokens.get(preprocessorTokensIndex).type;
            } else {
                return super.getTokenType();
            }
        }

        @Override
        public Marker mark() {
            return new GLSLMarkerDelegate(super.mark());
        }

        @Override
        public IElementType lookAhead(int steps) {
            Marker fallback = mark();
            for (int i = 0; i < steps; ++i) advanceLexer();
            IElementType type = getTokenType();
            fallback.rollbackTo();
            return type;
        }

        @Override
        public boolean eof() {
            if (preprocessorTokens != null && preprocessorTokensIndex < preprocessorTokens.size()) {
                return false;
            } else return super.eof();
        }

        @Nullable
        @Override
        public String getTokenText() {
            if(preprocessorTokens != null){
                return preprocessorTokens.get(preprocessorTokensIndex).text;
            } else {
                return super.getTokenText();
            }
        }

        @Override
        public void advanceLexer() {
            Marker emptyReplacementMarker = null;
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

                        if(preprocessorTokensReplacementType == PreprocessorDropInType.UNKNOWN
                                && getTokenType() instanceof GLSLRedefinedTokenType && ((GLSLRedefinedTokenType)getTokenType()).mark()){
                            Marker unknownReplacementMarker = mark();
                            super.advanceLexer();
                            unknownReplacementMarker.done(new GLSLElementTypes.PreprocessedUnknownElementType(preprocessorTokensText));
                        }else{
                            super.advanceLexer();
                        }

                        //Cleanup
                        preprocessorTokens = null;
                        preprocessorTokensReplacementType = null;
                        preprocessorTokensIndex = -1;
                        preprocessorTokensText = null;

                        advancedRealLexer = true;
                    }
                } else {
                    //Not in defined mode
                    super.advanceLexer();
                    advancedRealLexer = true;
                }

                if (advancedRealLexer) {
                    //Real lexer advanced, so check for directives and redefined tokens
                    IElementType tokenType = getTokenType();
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
            advanceLexer_initializePreprocessorToken(b.getTokenType());
            preprocessorTokensIndex = markPreprocessorTokensIndex;
        }

        @Override
        public PsiBuilder.Marker precede() {
            return new GLSLMarkerDelegate(super.precede(), markPreprocessorTokensIndex);
        }
    }

    //Code that deals with preprocessor, b.call replacements

    protected final HashMap<String, PreprocessorDropIn> defines = new HashMap<String, PreprocessorDropIn>();

    /** List of tokens to serve, because the real b is currently on #define-d IDENTIFIER.
     * If null, b is not there and everything is normal. */
    protected List<PreprocessorToken> preprocessorTokens = null;
    /** How far in preprocessorTokens is parser advanced. */
    protected int preprocessorTokensIndex = -1;
    /** Original text of `preprocessorTokens`. Used for UI and debug. */
    private String preprocessorTokensText = null;
    /** Value of preprocessorTokensText before calling consumePreprocessorTokens(). (For convenience) */
    protected String preprocessorTextOfConsumedTokens = null;

    private PreprocessorDropInType preprocessorTokensReplacementType = PreprocessorDropInType.UNKNOWN;

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
            b.advanceLexer();
        }
    }

    private void advanceLexer_initializePreprocessorToken(final IElementType tokenType){
        boolean rerolling = (tokenType instanceof GLSLRedefinedTokenType);
        if (tokenType == IDENTIFIER || rerolling) {
            //It may be preprocessor defined identifier
            final String text = b.getTokenText();
            final PreprocessorDropIn dropIn = defines.get(text);

            if (dropIn != null) {
                preprocessorTokensReplacementType = dropIn.type;
                final List<PreprocessorToken> tokens = dropIn.tokens;
                preprocessorTokensText = dropIn.text;
                //This IDENTIFIER has been #define-d to tokens
                if(!rerolling){
                    b.remapCurrentToken(new GLSLRedefinedTokenType(tokens));
                }

                preprocessorTokens = tokens;
                preprocessorTokensIndex = 0;
            }// else There is nothing defined to this, continue normally
        }
    }

    //Utility code

    /**
     * Checks whether lexer is at the end of the file,
     * complains about it if it is
     * and closes all marks supplied (if eof).
     *
     * @return b.eof()
     */
    protected final boolean eof(PsiBuilder.Marker... marksToClose) {
        if (b.eof()) {
            if (marksToClose.length > 0) {
                for (PsiBuilder.Marker mark : marksToClose) {
                    mark.error("Premature end of file.");
                }
            } else {
                b.error("Premature end of file.");
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
        final boolean matched = !b.eof() && b.getTokenType() == type;
        if (matched) {
            b.advanceLexer();
        } else {
            b.error(error);
        }
        return matched;
    }

    /**
     * Consumes the next token if it is of the given types, otherwise it is ignored.
     *
     * @param types the expected token types.
     * @return indicates whether the match was successful or not.
     */
    protected final boolean tryMatch(IElementType... types) {
        if (b.eof()) {
            return false;
        }
        boolean match = false;
        for (IElementType type : types) {
            match |= b.getTokenType() == type;
        }
        if (match) {
            b.advanceLexer();
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
        boolean match = types.contains(b.getTokenType());
        if (match) {
            b.advanceLexer();
        }
        return match;
    }

    // Interface to implementation

    protected abstract void parsePreprocessor();
}
