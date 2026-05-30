package glslplugin.lang;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import glslplugin.LightGLSLTestCase;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.preprocessor.GLSLPreprocessorInclude;
import glslplugin.lang.parser.GLSLFile;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class IncludeResolutionTest extends LightGLSLTestCase {

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
