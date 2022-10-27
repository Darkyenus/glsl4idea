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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    final PreprocessorPsiBuilderAdapter b;

    GLSLParsingBase(PsiBuilder builder) {
        b = new PreprocessorPsiBuilderAdapter(builder);
    }

    private boolean parsingPreprocessor = false;

    /**
     * Called before marking or querying tokens.
     * This makes sure that the preprocessor AST is as isolated in the PSI (as close to root) as possible.
     * The only disadvantage to this is that it does not automatically handle preprocessor at the end of the file, which we handle explicitly.
     */
    private void flushPreprocessor() {
        if (parsingPreprocessor) return;
        try {
            parsingPreprocessor = true;
            while (getTokenType() == PREPROCESSOR_BEGIN) {
                b.setAllowRedefinitions(false);
                parsePreprocessor();
                b.setAllowRedefinitions(true);
            }
        } finally {
            parsingPreprocessor = false;
        }
    }

    //Utility code

    /**
     * @return whether lexer is at the end of the file.
     */
    protected final boolean eof() {
        flushPreprocessor();
        return b.eof();
    }

    /**
     * Checks whether lexer is at the end of the file,
     * complains about it if it is
     * and closes all marks supplied (if eof).
     *
     * @return b.eof()
     */
    protected final boolean eof(Marker... marksToClose) {
        if (eof()) {
            if (marksToClose.length > 0) {
                for (Marker mark : marksToClose) {
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

    protected final void error(@NotNull final String messageText) {
        b.error(messageText);
    }

    protected final @Nullable IElementType getTokenType() {
        flushPreprocessor();
        return b.getTokenType();
    }

    protected final @Nullable IElementType lookAhead(int steps) {
        if (steps <= 0) return getTokenType();
        final Marker mark = mark();
        try {
            for (int step = 0; step < steps; step++) {
                advanceLexer();
            }
            return getTokenType();
        } finally {
            mark.rollbackTo();
        }
    }

    protected final @Nullable String getTokenText() {
        flushPreprocessor();
        return b.getTokenText();
    }

    protected final @NotNull Marker mark() {
        flushPreprocessor();
        return b.mark();
    }

    protected final void advanceLexer() {
        b.advanceLexer();
    }

    /**
     * Verifies that the current token is of the given type, if not it will flag an error.
     *
     * @param type  the expected token type.
     * @param error an appropriate error message if any other token is found instead.
     * @return indicates whether the match was successful or not.
     */
    protected final boolean match(IElementType type, String error) {
        final boolean matched = !eof() && getTokenType() == type;
        if (matched) {
            advanceLexer();
        } else {
            error(error);
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
        if (eof()) {
            return false;
        }
        boolean match = false;
        for (IElementType type : types) {
            match |= getTokenType() == type;
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
        boolean match = types.contains(getTokenType());
        if (match) {
            advanceLexer();
        }
        return match;
    }

    // Interface to implementation

    protected abstract void parsePreprocessor();
}
