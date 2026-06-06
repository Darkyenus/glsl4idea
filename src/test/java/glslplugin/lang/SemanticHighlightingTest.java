package glslplugin.lang;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import glslplugin.GLSLHighlighter;
import glslplugin.LightGLSLTestCase;
import glslplugin.lang.elements.declarations.GLSLFunctionDeclaration;
import glslplugin.lang.elements.declarations.GLSLInterfaceBlockDefinition;
import glslplugin.lang.elements.expressions.GLSLFunctionOrConstructorCallExpression;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
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
        assertSame(DefaultLanguageHighlighterColors.GLOBAL_VARIABLE, GLSLHighlighter.GLSL_IDENTIFIER_UNIFORM[0].getFallbackAttributeKey());
        assertSame(DefaultLanguageHighlighterColors.GLOBAL_VARIABLE, GLSLHighlighter.GLSL_IDENTIFIER_IN[0].getFallbackAttributeKey());
        assertSame(DefaultLanguageHighlighterColors.GLOBAL_VARIABLE, GLSLHighlighter.GLSL_IDENTIFIER_OUT[0].getFallbackAttributeKey());
        assertSame(DefaultLanguageHighlighterColors.GLOBAL_VARIABLE, GLSLHighlighter.GLSL_IDENTIFIER_VARYING[0].getFallbackAttributeKey());
        assertSame(DefaultLanguageHighlighterColors.GLOBAL_VARIABLE, GLSLHighlighter.GLSL_IDENTIFIER_ATTRIBUTE[0].getFallbackAttributeKey());
        assertSame(DefaultLanguageHighlighterColors.INSTANCE_FIELD, GLSLHighlighter.GLSL_IDENTIFIER_STRUCT_FIELD[0].getFallbackAttributeKey());
        assertSame(DefaultLanguageHighlighterColors.INSTANCE_FIELD, GLSLHighlighter.GLSL_IDENTIFIER_INTERFACE_BLOCK[0].getFallbackAttributeKey());
    }

    public void testInterfaceBlockNameUsesBlockIdentifier() {
        myFixture.configureByText(GLSLFileType.INSTANCE, """
                uniform Camera {
                    mat4 projection;
                } camera;
                """);

        final GLSLInterfaceBlockDefinition definition = PsiTreeUtil.findChildOfType(myFixture.getFile(), GLSLInterfaceBlockDefinition.class);
        assertNotNull(definition);
        assertEquals("Camera", definition.getName());
        assertEquals("Camera", definition.getType().getTypename());
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

    public void testFunctionCallHighlights() {
        final String shader = """
                #version 140

                float computeValue(float input) {
                    return input;
                }

                void main() {
                    computeValue(1);
                }

                """;
        myFixture.configureByText(GLSLFileType.INSTANCE, shader);
        assertFalse(PsiTreeUtil.findChildrenOfType(myFixture.getFile(), GLSLFunctionOrConstructorCallExpression.class).isEmpty());
        PsiElement callIdentifier = PsiTreeUtil.findChildrenOfType(myFixture.getFile(), GLSLFunctionOrConstructorCallExpression.class).iterator().next().getFunctionOrConstructedTypeNameIdentifier();
        assertNotNull(callIdentifier);
        final List<HighlightInfo> highlights = myFixture.doHighlighting();
        boolean found = false;
        for (HighlightInfo info : highlights) {
            if (GLSLHighlighter.GLSL_FUNCTION_CALL[0].equals(info.forcedTextAttributesKey)) {
                found = true;
                break;
            }
        }
        assertTrue("Missing function call highlight", found);
        assertSame(DefaultLanguageHighlighterColors.FUNCTION_CALL, GLSLHighlighter.GLSL_FUNCTION_CALL[0].getFallbackAttributeKey());
    }

    public void testConstructorCallHighlights() {
        final String shader = """
                #version 140

                struct MyStruct {
                    int value;
                };

                void main() {
                    MyStruct value = MyStruct(1);
                }

                """;
        myFixture.configureByText(GLSLFileType.INSTANCE, shader);
        assertFalse(PsiTreeUtil.findChildrenOfType(myFixture.getFile(), GLSLFunctionOrConstructorCallExpression.class).isEmpty());
        PsiElement callIdentifier = PsiTreeUtil.findChildrenOfType(myFixture.getFile(), GLSLFunctionOrConstructorCallExpression.class).iterator().next().getFunctionOrConstructedTypeNameIdentifier();
        assertNotNull(callIdentifier);
        final List<HighlightInfo> highlights = myFixture.doHighlighting();
        boolean found = false;
        for (HighlightInfo info : highlights) {
            if (GLSLHighlighter.GLSL_CONSTRUCTOR_CALL[0].equals(info.forcedTextAttributesKey)) {
                found = true;
                break;
            }
        }
        assertTrue("Missing function call highlight", found);
        assertSame(DefaultLanguageHighlighterColors.FUNCTION_CALL, GLSLHighlighter.GLSL_CONSTRUCTOR_CALL[0].getFallbackAttributeKey());
    }

    static {
        // Workaround when running tests on systems that symlink the home directory (such as Fedora Atomic).
        // The plugin needs to access files in its own jar, which is in the home directory, which is allowed.
        // But only the symlinked variant is added, but we are accessing it at its real path, so it breaks.
        try {
            Path home = Path.of(System.getProperty("user.home"));
            Path realHome = home.toRealPath();
            if (!home.equals(realHome)) {
                String roots = System.getProperty("vfs.additional-allowed-roots");
                if (roots == null || roots.isEmpty()) {
                    roots = realHome.toString();
                } else {
                    roots = roots + File.pathSeparator + realHome;
                }
                System.setProperty("vfs.additional-allowed-roots", roots);
            }
        } catch (IOException e) {
            System.err.println("Failed to add fallback allowed roots");
            e.printStackTrace(System.err);
        }
    }
}
