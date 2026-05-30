package glslplugin.lang;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import glslplugin.GLSLHighlighter;
import glslplugin.LightGLSLTestCase;
import glslplugin.lang.elements.declarations.GLSLFunctionDeclaration;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.List;

public class SemanticHighlightingTest extends LightGLSLTestCase {

    private static final class ExpectedHighlight {
        final String text;
        final com.intellij.openapi.editor.colors.TextAttributesKey key;

        ExpectedHighlight(String text, com.intellij.openapi.editor.colors.TextAttributesKey key) {
            this.text = text;
            this.key = key;
        }
    }

    private void assertHighlight(String shader, ExpectedHighlight... expected) {
        myFixture.configureByText(GLSLFileType.INSTANCE, shader);
        final List<HighlightInfo> highlights = myFixture.doHighlighting();
        final String documentText = myFixture.getEditor().getDocument().getText();

        for (ExpectedHighlight item : expected) {
            final int start = documentText.indexOf(item.text);
            assertTrue("Missing text: " + item.text, start >= 0);
            final int end = start + item.text.length();
            boolean found = false;
            for (HighlightInfo info : highlights) {
                if (info.startOffset == start && info.endOffset == end && item.key.equals(info.forcedTextAttributesKey)) {
                    found = true;
                    break;
                }
            }
            assertTrue("Missing highlight for " + item.text, found);
        }
    }

    public void testSemanticVariableAndFieldHighlights() {
        assertHighlight("""
                #version 140

                uniform mat4 view;
                in vec3 position;
                out vec4 fragColor;

                struct Light {
                    vec3 color;
                };

                uniform Camera {
                    mat4 projection;
                } camera;

                buffer Data {
                    Light light;
                } data;

                void main() {
                    Light l;
                    fragColor = vec4(position, 1.0);
                    l.color = vec3(1.0);
                    data.light = l;
                    camera.projection = view;
                }
                """,
                new ExpectedHighlight("view", GLSLHighlighter.GLSL_IDENTIFIER_UNIFORM[0]),
                new ExpectedHighlight("position", GLSLHighlighter.GLSL_IDENTIFIER_IN[0]),
                new ExpectedHighlight("fragColor", GLSLHighlighter.GLSL_IDENTIFIER_OUT[0]),
                new ExpectedHighlight("color", GLSLHighlighter.GLSL_IDENTIFIER_STRUCT_FIELD[0]),
                new ExpectedHighlight("projection", GLSLHighlighter.GLSL_IDENTIFIER_INTERFACE_BLOCK[0]),
                new ExpectedHighlight("light", GLSLHighlighter.GLSL_IDENTIFIER_INTERFACE_BLOCK[0])
        );
    }

    public void testSemanticIdentifierFallbacksUseIDEDefaults() {
        assertSame(DefaultLanguageHighlighterColors.CONSTANT, GLSLHighlighter.GLSL_IDENTIFIER_UNIFORM[0].getFallbackAttributeKey());
        assertSame(DefaultLanguageHighlighterColors.CONSTANT, GLSLHighlighter.GLSL_IDENTIFIER_IN[0].getFallbackAttributeKey());
        assertSame(DefaultLanguageHighlighterColors.CONSTANT, GLSLHighlighter.GLSL_IDENTIFIER_OUT[0].getFallbackAttributeKey());
        assertSame(DefaultLanguageHighlighterColors.CONSTANT, GLSLHighlighter.GLSL_IDENTIFIER_VARYING[0].getFallbackAttributeKey());
        assertSame(DefaultLanguageHighlighterColors.CONSTANT, GLSLHighlighter.GLSL_IDENTIFIER_ATTRIBUTE[0].getFallbackAttributeKey());
        assertSame(DefaultLanguageHighlighterColors.INSTANCE_FIELD, GLSLHighlighter.GLSL_IDENTIFIER_STRUCT_FIELD[0].getFallbackAttributeKey());
        assertSame(DefaultLanguageHighlighterColors.INSTANCE_FIELD, GLSLHighlighter.GLSL_IDENTIFIER_INTERFACE_BLOCK[0].getFallbackAttributeKey());
    }

    public void testFunctionDeclarationHighlights() {
        final String shader = """
                #version 140

                float computeValue(float input);

                void main() {
                }

                float computeValue(float input) {
                    return input;
                }
                """;
        myFixture.configureByText(GLSLFileType.INSTANCE, shader);
        assertFalse(PsiTreeUtil.findChildrenOfType(myFixture.getFile(), GLSLFunctionDeclaration.class).isEmpty());
        assertNotNull(PsiTreeUtil.findChildrenOfType(myFixture.getFile(), GLSLFunctionDeclaration.class).iterator().next().getNameIdentifier());
        final List<HighlightInfo> highlights = myFixture.doHighlighting();
        boolean found = false;
        for (HighlightInfo info : highlights) {
            if (GLSLHighlighter.GLSL_FUNCTION_DECLARATION[0].equals(info.forcedTextAttributesKey)) {
                found = true;
                break;
            }
        }
        assertTrue("Missing function declaration highlight", found);
        assertSame(DefaultLanguageHighlighterColors.FUNCTION_DECLARATION, GLSLHighlighter.GLSL_FUNCTION_DECLARATION[0].getFallbackAttributeKey());
    }
}
