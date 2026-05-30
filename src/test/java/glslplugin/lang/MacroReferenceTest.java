package glslplugin.lang;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationAction;
import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import glslplugin.GLSLHighlighter;
import glslplugin.LightGLSLTestCase;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.preprocessor.GLSLDefineDirective;
import glslplugin.lang.elements.preprocessor.GLSLRedefinedToken;
import glslplugin.lang.parser.GLSLFile;

public class MacroReferenceTest extends LightGLSLTestCase {

    private void configureProjectShaderFile(String path, String textWithCaret) {
        int caretOffset = textWithCaret.indexOf("<caret>");
        assertTrue(caretOffset >= 0);

        PsiFile file = addProjectShaderFile(path, textWithCaret.replace("<caret>", ""));
        assertNotNull(file.getVirtualFile());
        myFixture.openFileInEditor(file.getVirtualFile());
        myFixture.getEditor().getCaretModel().moveToOffset(caretOffset);
    }

    private PsiFile addProjectShaderFile(String path, String text) {
        PsiFile file = myFixture.addFileToProject(path, text);
        if (file instanceof GLSLFile glslFile) {
            glslFile.isBuiltinFile = true;
        }
        return file;
    }

    private GLSLDefineDirective assertGotoDefine(String expectedName) {
        PsiElement target = GotoDeclarationAction.findTargetElement(
            getProject(),
            myFixture.getEditor(),
            myFixture.getCaretOffset()
        );

        assertTrue("Expected GLSLDefineDirective, got " + target, target instanceof GLSLDefineDirective);
        GLSLDefineDirective define = (GLSLDefineDirective) target;
        assertEquals(expectedName, define.getName());
        return define;
    }

    private void assertRedefinedTokenHighlight(String text) {
        final String documentText = myFixture.getEditor().getDocument().getText();
        final int startOffset = documentText.indexOf(text);
        assertTrue(startOffset >= 0);
        final int endOffset = startOffset + text.length();

        for (HighlightInfo info : myFixture.doHighlighting()) {
            if (info.startOffset == startOffset
                && info.endOffset == endOffset
                && info.forcedTextAttributesKey == GLSLHighlighter.GLSL_REDEFINED_TOKEN[0]) {
                return;
            }
        }

        fail("Expected macro highlight for " + text);
    }

    public void testObjectLikeMacroReferenceResolvesToDefineDirective() {
        myFixture.configureByText(GLSLFileType.INSTANCE, """
            #define LIGHT_COUNT 4

            void main() {
                int x = LIGHT_<caret>COUNT;
            }
            """);

        PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion();
        assertTrue(reference.getElement() instanceof GLSLRedefinedToken);

        PsiElement resolved = reference.resolve();
        assertTrue(resolved instanceof GLSLDefineDirective);
        assertEquals("LIGHT_COUNT", ((GLSLDefineDirective) resolved).getName());
    }

    public void testObjectLikeMacroIsGotoDeclarationTarget() {
        myFixture.configureByText(GLSLFileType.INSTANCE, """
            #define LIGHT_COUNT 4

            void main() {
                int x = LIGHT_<caret>COUNT;
            }
            """);

        PsiElement target = GotoDeclarationAction.findTargetElement(
            getProject(),
            myFixture.getEditor(),
            myFixture.getCaretOffset()
        );

        assertTrue(target instanceof GLSLDefineDirective);
        assertEquals("LIGHT_COUNT", ((GLSLDefineDirective) target).getName());
    }

    public void testFunctionLikeMacroIsGotoDeclarationTarget() {
        myFixture.configureByText(GLSLFileType.INSTANCE, """
            #define SCALE(x) ((x) * 2.0)

            void main() {
                float x = SCA<caret>LE(1.0);
            }
            """);

        PsiElement target = GotoDeclarationAction.findTargetElement(
            getProject(),
            myFixture.getEditor(),
            myFixture.getCaretOffset()
        );

        assertTrue(target instanceof GLSLDefineDirective);
        assertEquals("SCALE", ((GLSLDefineDirective) target).getName());
    }

    public void testIncludedObjectLikeMacroIsGotoDeclarationTarget() {
        addProjectShaderFile("shaders/common.glsl", "#define INCLUDED_VALUE 7\n");
        configureProjectShaderFile("shaders/main.glsl", """
            #include "common.glsl"

            void main() {
                int x = INCLUDED_<caret>VALUE;
            }
            """);

        GLSLDefineDirective define = assertGotoDefine("INCLUDED_VALUE");
        assertEquals("common.glsl", define.getContainingFile().getName());
    }

    public void testIncludedObjectLikeMacroIsHighlighted() {
        addProjectShaderFile("shaders/common.glsl", "#define INCLUDED_VALUE 7\n");
        configureProjectShaderFile("shaders/main.glsl", """
            #include "common.glsl"

            void main() {
                int x = INCLUDED_<caret>VALUE;
            }
            """);

        assertRedefinedTokenHighlight("INCLUDED_VALUE");
    }

    public void testIncludedFunctionLikeMacroIsGotoDeclarationTarget() {
        addProjectShaderFile("shaders/common.glsl", "#define SCALE(x) ((x) * 2.0)\n");
        configureProjectShaderFile("shaders/main.glsl", """
            #include "common.glsl"

            void main() {
                float x = SCA<caret>LE(1.0);
            }
            """);

        GLSLDefineDirective define = assertGotoDefine("SCALE");
        assertEquals("common.glsl", define.getContainingFile().getName());
    }

    public void testIncludedFunctionLikeMacroIsHighlighted() {
        addProjectShaderFile("shaders/common.glsl", "#define SCALE(x) ((x) * 2.0)\n");
        configureProjectShaderFile("shaders/main.glsl", """
            #include "common.glsl"

            void main() {
                float x = SCA<caret>LE(1.0);
            }
            """);

        assertRedefinedTokenHighlight("SCALE");
    }

    public void testIncludedMacroInPreprocessorDirectiveIsGotoDeclarationTarget() {
        addProjectShaderFile("shaders/common.glsl", "#define FEATURE 1\n");
        configureProjectShaderFile("shaders/main.glsl", """
            #include "common.glsl"
            #ifdef FEA<caret>TURE
            #endif
            """);

        GLSLDefineDirective define = assertGotoDefine("FEATURE");
        assertEquals("common.glsl", define.getContainingFile().getName());
    }

    public void testIncludedMacroInPreprocessorDirectiveIsHighlighted() {
        addProjectShaderFile("shaders/common.glsl", "#define FEATURE 1\n");
        configureProjectShaderFile("shaders/main.glsl", """
            #include "common.glsl"
            #ifdef FEA<caret>TURE
            #endif
            """);

        assertRedefinedTokenHighlight("FEATURE");
    }

    public void testNestedIncludedMacroIsGotoDeclarationTarget() {
        addProjectShaderFile("shaders/defs.glsl", "#define NESTED_FEATURE 1\n");
        addProjectShaderFile("shaders/common.glsl", "#include \"defs.glsl\"\n");
        configureProjectShaderFile("shaders/main.glsl", """
            #include "common.glsl"
            #ifdef NESTED_<caret>FEATURE
            #endif
            """);

        GLSLDefineDirective define = assertGotoDefine("NESTED_FEATURE");
        assertEquals("defs.glsl", define.getContainingFile().getName());
    }

    public void testCyclicIncludeDoesNotBreakMissingMacroLookup() {
        addProjectShaderFile("shaders/a.glsl", "#include \"b.glsl\"\n");
        addProjectShaderFile("shaders/b.glsl", "#include \"a.glsl\"\n");
        configureProjectShaderFile("shaders/main.glsl", """
            #include "a.glsl"
            #ifdef MISS<caret>ING_FEATURE
            #endif
            """);

        PsiElement target = GotoDeclarationAction.findTargetElement(
            getProject(),
            myFixture.getEditor(),
            myFixture.getCaretOffset()
        );

        assertNull(target);
    }

    public void testIncludedUndefStopsIncludedMacroNavigation() {
        addProjectShaderFile("shaders/common.glsl", """
            #define FEATURE 1
            #undef FEATURE
            """);
        configureProjectShaderFile("shaders/main.glsl", """
            #include "common.glsl"
            #ifdef FEA<caret>TURE
            #endif
            """);

        PsiElement target = GotoDeclarationAction.findTargetElement(
            getProject(),
            myFixture.getEditor(),
            myFixture.getCaretOffset()
        );

        assertNull(target);
    }

    public void testLocalMacroOverrideWinsOverIncludedMacro() {
        addProjectShaderFile("shaders/common.glsl", "#define FEATURE 1\n");
        configureProjectShaderFile("shaders/main.glsl", """
            #include "common.glsl"
            #define FEATURE 2
            #ifdef FEA<caret>TURE
            #endif
            """);

        GLSLDefineDirective define = assertGotoDefine("FEATURE");
        assertEquals("main.glsl", define.getContainingFile().getName());
        assertEquals("2", define.getBoundText());
    }

    public void testIfdefMacroIsGotoDeclarationTarget() {
        myFixture.configureByText(GLSLFileType.INSTANCE, """
            #define FEATURE 1
            #ifdef FEA<caret>TURE
            #endif
            """);

        PsiElement target = GotoDeclarationAction.findTargetElement(
            getProject(),
            myFixture.getEditor(),
            myFixture.getCaretOffset()
        );

        assertTrue(target instanceof GLSLDefineDirective);
        assertEquals("FEATURE", ((GLSLDefineDirective) target).getName());
    }

    public void testDefinedOperatorMacroIsGotoDeclarationTarget() {
        myFixture.configureByText(GLSLFileType.INSTANCE, """
            #define FEATURE 1
            #if defined(FEA<caret>TURE)
            #endif
            """);

        PsiElement target = GotoDeclarationAction.findTargetElement(
            getProject(),
            myFixture.getEditor(),
            myFixture.getCaretOffset()
        );

        assertTrue(target instanceof GLSLDefineDirective);
        assertEquals("FEATURE", ((GLSLDefineDirective) target).getName());
    }

    public void testIfMacroExpressionIsGotoDeclarationTarget() {
        myFixture.configureByText(GLSLFileType.INSTANCE, """
            #define FEATURE 1
            #if FEA<caret>TURE
            #endif
            """);

        PsiElement target = GotoDeclarationAction.findTargetElement(
            getProject(),
            myFixture.getEditor(),
            myFixture.getCaretOffset()
        );

        assertTrue(target instanceof GLSLDefineDirective);
        assertEquals("FEATURE", ((GLSLDefineDirective) target).getName());
    }

    public void testIfMacroExpressionWithMultipleMacrosUsesIdentifierAtCaret() {
        myFixture.configureByText(GLSLFileType.INSTANCE, """
            #define FEATURE_A 1
            #define FEATURE_B 1
            #if FEATURE_A && FEATURE_<caret>B
            #endif
            """);

        PsiElement target = GotoDeclarationAction.findTargetElement(
            getProject(),
            myFixture.getEditor(),
            myFixture.getCaretOffset()
        );

        assertTrue(target instanceof GLSLDefineDirective);
        assertEquals("FEATURE_B", ((GLSLDefineDirective) target).getName());
    }

    public void testIfndefMacroIsGotoDeclarationTarget() {
        myFixture.configureByText(GLSLFileType.INSTANCE, """
            #define FEATURE 1
            #ifndef FEA<caret>TURE
            #endif
            """);

        PsiElement target = GotoDeclarationAction.findTargetElement(
            getProject(),
            myFixture.getEditor(),
            myFixture.getCaretOffset()
        );

        assertTrue(target instanceof GLSLDefineDirective);
        assertEquals("FEATURE", ((GLSLDefineDirective) target).getName());
    }

    public void testUndefMacroIsGotoDeclarationTarget() {
        myFixture.configureByText(GLSLFileType.INSTANCE, """
            #define FEATURE 1
            #undef FEA<caret>TURE
            """);

        PsiElement target = GotoDeclarationAction.findTargetElement(
            getProject(),
            myFixture.getEditor(),
            myFixture.getCaretOffset()
        );

        assertTrue(target instanceof GLSLDefineDirective);
        assertEquals("FEATURE", ((GLSLDefineDirective) target).getName());
    }

    public void testUndefStopsLaterPreprocessorDirectiveReference() {
        myFixture.configureByText(GLSLFileType.INSTANCE, """
            #define FEATURE 1
            #undef FEATURE
            #ifdef FEA<caret>TURE
            #endif
            """);

        PsiElement target = GotoDeclarationAction.findTargetElement(
            getProject(),
            myFixture.getEditor(),
            myFixture.getCaretOffset()
        );

        assertNull(target);
    }

    public void testRedefinedMacroResolvesToNearestActiveDefine() {
        myFixture.configureByText(GLSLFileType.INSTANCE, """
            #define FEATURE 1
            #define FEATURE 2
            #ifdef FEA<caret>TURE
            #endif
            """);

        PsiElement target = GotoDeclarationAction.findTargetElement(
            getProject(),
            myFixture.getEditor(),
            myFixture.getCaretOffset()
        );

        assertTrue(target instanceof GLSLDefineDirective);
        assertEquals("FEATURE", ((GLSLDefineDirective) target).getName());
        assertEquals("2", ((GLSLDefineDirective) target).getBoundText());
    }

    public void testDefineBodyMacroIsGotoDeclarationTarget() {
        myFixture.configureByText(GLSLFileType.INSTANCE, """
            #define FEATURE 1
            #define ENABLED FEA<caret>TURE
            """);

        PsiElement target = GotoDeclarationAction.findTargetElement(
            getProject(),
            myFixture.getEditor(),
            myFixture.getCaretOffset()
        );

        assertTrue(target instanceof GLSLDefineDirective);
        assertEquals("FEATURE", ((GLSLDefineDirective) target).getName());
    }

    public void testMacroNameTokenHasIdentifierElementType() {
        myFixture.configureByText(GLSLFileType.INSTANCE, "#define LIGHT_COUNT 4\nLIGHT_<caret>COUNT\n");

        PsiElement resolved = myFixture.getReferenceAtCaretPositionWithAssertion().resolve();
        assertTrue(resolved instanceof GLSLDefineDirective);

        PsiElement nameIdentifier = ((GLSLDefineDirective) resolved).getNameIdentifier();
        assertNotNull(nameIdentifier);
        assertEquals(GLSLTokenTypes.IDENTIFIER, nameIdentifier.getNode().getElementType());
        assertEquals("LIGHT_COUNT", nameIdentifier.getText());
    }
}
