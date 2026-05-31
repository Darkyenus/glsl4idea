package glslplugin.lang;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import glslplugin.lang.GLSLFileType;
import glslplugin.LightGLSLTestCase;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.preprocessor.GLSLPreprocessorInclude;
import glslplugin.lang.parser.GLSLFile;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class IncludeResolutionTest extends LightGLSLTestCase {

    private void configureProjectShaderFile(String path, String textWithCaret) {
        int caretOffset = textWithCaret.indexOf("<caret>");
        assertTrue(caretOffset >= 0);

        PsiFile file = addShaderFile(path, textWithCaret.replace("<caret>", ""));
        assertNotNull(file.getVirtualFile());
        myFixture.openFileInEditor(file.getVirtualFile());
        myFixture.getEditor().getCaretModel().moveToOffset(caretOffset);
    }

    public void testIncludeStringReferenceResolvesToIncludedFile() {
        addShaderFile("shaders/common.glsl", "float includedValue;\n");
        configureProjectShaderFile("shaders/main.glsl", "#include \"common<caret>.glsl\"\n");

        PsiReference reference = myFixture.getReferenceAtCaretPositionWithAssertion();
        PsiElement resolved = reference.resolve();

        assertTrue(resolved instanceof GLSLFile);
        assertEquals("common.glsl", ((GLSLFile) resolved).getName());
    }

    public void testIncludeStringIsGotoDeclarationTarget() {
        addShaderFile("shaders/common.glsl", "float includedValue;\n");
        configureProjectShaderFile("shaders/main.glsl", "#include \"common<caret>.glsl\"\n");

        PsiElement target = GotoDeclarationAction.findTargetElement(
            getProject(),
            myFixture.getEditor(),
            myFixture.getCaretOffset()
        );

        assertTrue("Expected GLSLFile, got " + target, target instanceof GLSLFile);
        assertEquals("common.glsl", ((GLSLFile) target).getName());
    }

    public void testCyclicIncludesDoNotReenterFilesDuringDeclarationWalk() {
        GLSLFile a = addShaderFile("shaders/a.glsl", "#include \"b.glsl\"\nfloat a;\n");
        GLSLFile b = addShaderFile("shaders/b.glsl", "#include \"a.glsl\"\nfloat b;\n");

        GLSLPreprocessorInclude include = findFirstInclude(a);
        assertSame(b, include.includedFile());

        DeclarationCountingProcessor processor = new DeclarationCountingProcessor();
        assertTrue(a.processDeclarations(processor, ResolveState.initial(), null, a));

        assertEquals(1, processor.count("a"));
        assertEquals(1, processor.count("b"));
    }

    private GLSLFile addShaderFile(String path, String text) {
        PsiFile psiFile = myFixture.addFileToProject(path, text);
        assertTrue(psiFile instanceof GLSLFile);
        GLSLFile file = (GLSLFile) psiFile;
        file.isBuiltinFile = true;
        return file;
    }

    private static GLSLPreprocessorInclude findFirstInclude(GLSLFile file) {
        for (PsiElement child = file.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof GLSLPreprocessorInclude include) {
                return include;
            }
        }
        fail("Expected an include directive");
        return null;
    }

    private static final class DeclarationCountingProcessor implements PsiScopeProcessor {
        private final Map<String, Integer> counts = new HashMap<>();

        @Override
        public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state) {
            if (element instanceof GLSLDeclarator declarator) {
                String name = declarator.getName();
                if (name != null) {
                    counts.merge(name, 1, Integer::sum);
                }
            }
            return true;
        }

        int count(String name) {
            return counts.getOrDefault(name, 0);
        }
    }
}
