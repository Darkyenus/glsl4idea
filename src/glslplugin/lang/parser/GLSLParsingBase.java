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
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static glslplugin.lang.elements.GLSLElementTypes.*;
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
    protected final PsiBuilder b;

    protected Map<String, List<IElementType>> definitions = new HashMap<String, List<IElementType>>();

    GLSLParsingBase(PsiBuilder builder) {
        b = new GLSLPsiBuilderAdapter(builder);
    }

    private class GLSLPsiBuilderAdapter extends MultiRemapPsiBuilderAdapter {

        public GLSLPsiBuilderAdapter(PsiBuilder delegate) {
            super(delegate);
        }

        @Override
        public void advanceLexer() {
            super.advanceLexer();

            while (getTokenType() == PREPROCESSOR_BEGIN) {
                parsePreprocessor();
            }

            if (definitions.get(getTokenText()) != null) {
                Marker macro = mark();
                remapCurrentToken(definitions.get(getTokenText()));
                macro.done(REDEFINED_TOKEN);
            }
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
