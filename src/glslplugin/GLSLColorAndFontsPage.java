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

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class GLSLColorAndFontsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] ATTRIBUTES;
    private static final SyntaxHighlighter syntaxHighlighter = new GLSLHighlighter();

    static {
        ATTRIBUTES = new AttributesDescriptor[]{
            new AttributesDescriptor("Numbers",GLSLHighlighter.GLSL_NUMBER[0]),
            new AttributesDescriptor("Types",GLSLHighlighter.GLSL_TYPE_SPECIFIER[0]),
            new AttributesDescriptor("Type qualifiers",GLSLHighlighter.GLSL_TYPE_QUALIFIERS[0]),
            //new AttributesDescriptor("Parameter qualifiers",GLSLHighlighter.GLSL_PARAMETER_QUALIFIERS),//Useless
            new AttributesDescriptor("Flow keywords",GLSLHighlighter.GLSL_FLOW_KEYWORDS[0]),
            new AttributesDescriptor("Block comments",GLSLHighlighter.GLSL_BLOCK_COMMENT[0]),
            new AttributesDescriptor("Line comments",GLSLHighlighter.GLSL_LINE_COMMENT[0]),
            new AttributesDescriptor("Braces", GLSLHighlighter.GLSL_BRACES[0]),
            new AttributesDescriptor("Dot", GLSLHighlighter.GLSL_DOT[0]),
            new AttributesDescriptor("Semicolon", GLSLHighlighter.GLSL_SEMICOLON[0]),
            new AttributesDescriptor("Comma", GLSLHighlighter.GLSL_COMMA[0]),
            new AttributesDescriptor("Parentheses", GLSLHighlighter.GLSL_PARENS[0]),
            new AttributesDescriptor("Brackets", GLSLHighlighter.GLSL_BRACKETS[0]),
            new AttributesDescriptor("Operators", GLSLHighlighter.GLSL_OPERATOR[0]),
            new AttributesDescriptor("Identifiers",GLSLHighlighter.GLSL_IDENTIFIER[0]),
            new AttributesDescriptor("Identifiers of Uniforms",GLSLHighlighter.GLSL_IDENTIFIER_UNIFORM[0]),
            new AttributesDescriptor("Identifiers of Varyings",GLSLHighlighter.GLSL_IDENTIFIER_VARYING[0]),
            new AttributesDescriptor("Identifiers of Attributes",GLSLHighlighter.GLSL_IDENTIFIER_ATTRIBUTE[0]),
            new AttributesDescriptor("Text",GLSLHighlighter.GLSL_TEXT[0]),
            new AttributesDescriptor("Directives",GLSLHighlighter.GLSL_PREPROCESSOR_DIRECTIVE[0]),
            new AttributesDescriptor("Preprocessor Strings",GLSLHighlighter.GLSL_STRING[0])
        };
    }

    @NotNull
    public String getDisplayName() {
        return "GLSL";
    }

    @Nullable
    public Icon getIcon() {
        return GLSLSupportLoader.GLSL.getIcon();
    }

    @NotNull
    public AttributesDescriptor[] getAttributeDescriptors() {
        return ATTRIBUTES;
    }

    @NotNull
    public ColorDescriptor[] getColorDescriptors() {
        return new ColorDescriptor[0];
    }

    @NotNull
    public SyntaxHighlighter getHighlighter() {
        return syntaxHighlighter;
    }

    @NonNls
    @NotNull
    public String getDemoText() {
        return "#version 120\n" +
                "precision lowp int;\n" +
                "uniform vec3 normal; // surface normal\n" +
                "const vec3 light = vec3(5.0, 0.5, 1.0);\n" +
                "\n" +
                "void shade(in vec3 light, in vec3 normal, out vec4 color);\n" +
                "\n" +
                "struct PointLight{\n" +
                "    vec3 position;\n" +
                "    float intensity;\n" +
                "};\n" +
                "\n" +
                "/* Fragment shader */\n" +
                "void main() {\n" +
                "#ifdef TEXTURED\n" +
                "    vec2 tex = gl_TexCoord[0].xy;\n" +
                "#endif\n" +
                "    float diffuse = dot(normal, light);\n" +
                "    if(diffuse < 0) {\n" +
                "        diffuse = -diffuse;\n" +
                "    }\n" +
                "    gl_FragColor = vec4(diffuse, diffuse, diffuse, 1.0);\n" +
                "}";
    }

    @Nullable
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }
}
