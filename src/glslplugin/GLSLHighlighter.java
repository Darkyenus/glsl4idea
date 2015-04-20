/*
 *     Copyright 2010 Jean-Paul Balabanian and Yngve Devik Hammersland
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

package glslplugin;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.tree.IElementType;
import glslplugin.lang.scanner.GLSLFlexAdapter;
import org.jetbrains.annotations.NotNull;

import static glslplugin.lang.elements.GLSLTokenTypes.*;

public class GLSLHighlighter extends SyntaxHighlighterBase {

    static final TextAttributesKey[] GLSL_NUMBER =
            { TextAttributesKey.createTextAttributesKey("GLSL.NUMBER", DefaultLanguageHighlighterColors.NUMBER) };
    static final TextAttributesKey[] GLSL_TYPE_SPECIFIER =
            { TextAttributesKey.createTextAttributesKey("GLSL.TYPE_SPECIFIER", DefaultLanguageHighlighterColors.CLASS_REFERENCE) };
    static final TextAttributesKey[] GLSL_TYPE_QUALIFIERS =
            { TextAttributesKey.createTextAttributesKey("GLSL.QUALIFIER_TOKENS", DefaultLanguageHighlighterColors.KEYWORD) };
    //static final TextAttributesKey[] GLSL_PARAMETER_QUALIFIERS = {
    //        { TextAttributesKey.createTextAttributesKey("GLSL.PARAMETER_QUALIFIERS", DefaultLanguageHighlighterColors.KEYWORD) };
    static final TextAttributesKey[] GLSL_FLOW_KEYWORDS =
            { TextAttributesKey.createTextAttributesKey("GLSL.FLOW_KEYWORDS", DefaultLanguageHighlighterColors.KEYWORD) };
    static final TextAttributesKey[] GLSL_COMMENT =
            { TextAttributesKey.createTextAttributesKey("GLSL.COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT) };
    static final TextAttributesKey[] GLSL_IDENTIFIER =
            { TextAttributesKey.createTextAttributesKey("GLSL.IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER) };
    static final TextAttributesKey[] GLSL_COMPILER_DIRECTIVE =
            { TextAttributesKey.createTextAttributesKey("GLSL.COMPILER_DIRECTIVE",DefaultLanguageHighlighterColors.DOC_COMMENT_MARKUP) };
    static final TextAttributesKey[] GLSL_COMPILER_DIRECTIVE_VERSION =
            { TextAttributesKey.createTextAttributesKey("GLSL.COMPILER_DIRECTIVE_VERSION", GLSL_COMPILER_DIRECTIVE[0]) };
    static final TextAttributesKey[] GLSL_COMPILER_DIRECTIVE_EXTENSION =
            { TextAttributesKey.createTextAttributesKey("GLSL.COMPILER_DIRECTIVE_EXTENSION", GLSL_COMPILER_DIRECTIVE[0]) };
    static final TextAttributesKey[] GLSL_COMPILER_DIRECTIVE_PRAGMA =
            { TextAttributesKey.createTextAttributesKey("GLSL.COMPILER_DIRECTIVE_PRAGMA", GLSL_COMPILER_DIRECTIVE[0]) };
    static final TextAttributesKey[] GLSL_COMPILER_DIRECTIVE_OTHER =
            { TextAttributesKey.createTextAttributesKey("GLSL.COMPILER_DIRECTIVE_OTHER", GLSL_COMPILER_DIRECTIVE[0]) };
    static final TextAttributesKey[] GLSL_PRECISION_STATEMENT =
            { TextAttributesKey.createTextAttributesKey("GLSL.PRECISION_STATEMENT", GLSL_COMPILER_DIRECTIVE[0]) };
    static final TextAttributesKey[] GLSL_UNKNOWN =
            { TextAttributesKey.createTextAttributesKey("GLSL.UNKNOWN", HighlighterColors.BAD_CHARACTER) };
    static final TextAttributesKey[] GLSL_TEXT =
            { TextAttributesKey.createTextAttributesKey("GLSL.TEXT", DefaultLanguageHighlighterColors.DOT) };

    @NotNull
    public Lexer getHighlightingLexer() {
        return new GLSLFlexAdapter();
    }

    @NotNull
    public TextAttributesKey[] getTokenHighlights(IElementType type) {
        if (type == INTEGER_CONSTANT || type == FLOAT_CONSTANT || type == BOOL_CONSTANT) return GLSL_NUMBER;
        if (type == COMMENT_BLOCK || type == COMMENT_LINE) return GLSL_COMMENT;
        if (type == IDENTIFIER) return GLSL_IDENTIFIER;
        if (type == COMPILER_DIRECTIVE_VERSION) return GLSL_COMPILER_DIRECTIVE_VERSION;
        if (type == COMPILER_DIRECTIVE_EXTENSION) return GLSL_COMPILER_DIRECTIVE_EXTENSION;
        if (type == COMPILER_DIRECTIVE_PRAGMA) return GLSL_COMPILER_DIRECTIVE_PRAGMA;
        if (type == COMPILER_DIRECTIVE_OTHER) return GLSL_COMPILER_DIRECTIVE_OTHER;
        if (TYPE_SPECIFIER_NONARRAY_TOKENS.contains(type)) return GLSL_TYPE_SPECIFIER;
        if (QUALIFIER_TOKENS.contains(type)) return GLSL_TYPE_QUALIFIERS;
        if (FLOW_KEYWORDS.contains(type)) return GLSL_FLOW_KEYWORDS;
        if (type == PRECISION_STATEMENT) return GLSL_PRECISION_STATEMENT;
        if (type == UNKNOWN) return GLSL_UNKNOWN;
        return GLSL_TEXT;
    }
}
