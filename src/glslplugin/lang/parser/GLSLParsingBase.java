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

import com.intellij.lang.ForeignLeafType;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import glslplugin.lang.elements.GLSLElementType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static glslplugin.lang.elements.GLSLTokenTypes.*;

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
    protected final GLSLPsiBuilderAdapter b;

    protected Map<String, List<ForeignLeafType>> definitions = new HashMap<>();
    protected Map<String, String> definitionTexts = new HashMap<>();
    protected Stack<PsiBuilder.Marker> conditionalBlockMarkers = new Stack<>();

    GLSLParsingBase(PsiBuilder builder) {
        b = new GLSLPsiBuilderAdapter(builder);
    }

    protected final class GLSLPsiBuilderAdapter extends MultiRemapPsiBuilderAdapter {

        public GLSLPsiBuilderAdapter(PsiBuilder delegate) {
            super(delegate);
        }

        @Override
        public void advanceLexer() {
            advanceLexer(true, true);
        }

        public void advanceLexer(boolean checkForPreprocessor, boolean remapTokens) {
            super.advanceLexer();

            if(checkForPreprocessor) {
                while (getTokenType() == PREPROCESSOR_BEGIN) {
                    parsePreprocessor();
                }
            }

            if (remapTokens) {
                advanceLexer_remapTokens();
            }
        }

        public void advanceLexer_remapTokens(){
            final String tokenText = getTokenText();
            final String[] namesThroughWhichThisTokenWasRedefined = getNamesThroughWhichThisTokenWasRedefined();
            for (String name : namesThroughWhichThisTokenWasRedefined) {
                if (name != null && name.equals(tokenText)) {
                    // Happens for #define name .*name.*
                    // We must not replace it with itself, as it would lead to much tears (and probably is't up to spec anyway)
                    return;
                }
            }
            final List<ForeignLeafType> definition = definitions.get(tokenText);
            if (definition != null) {
                Marker macro = mark();
                remapCurrentTokenAdvanceLexer_remapTokens = false;
                remapCurrentToken(definition, tokenText); //This will advance the lexer which will eat the (real or substituted) token and replace it with redefinition
                remapCurrentTokenAdvanceLexer_remapTokens = true;
                macro.done(new GLSLElementType.RedefinedTokenElementType(definitionTexts.get(tokenText)));
                advanceLexer_remapTokens();
            }
        }

        //Behold, the longest boolean on this hemisphere
        //Used in advanceLexer_remapTokens to not remap immediately after advancing in remapCurrentToken
        //That prevents two redefined tokens merging together (second becomes child of first)
        //I know that it sounds complicated, but you will have to trust me.
        private boolean remapCurrentTokenAdvanceLexer_remapTokens = true;

        @Override
        protected void remapCurrentTokenAdvanceLexer() {
            advanceLexer(false, remapCurrentTokenAdvanceLexer_remapTokens);
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
