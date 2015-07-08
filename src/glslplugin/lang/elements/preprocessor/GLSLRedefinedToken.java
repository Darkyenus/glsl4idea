package glslplugin.lang.elements.preprocessor;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.reference.GLSLMacroReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by abigail on 08/07/15.
 */
public class GLSLRedefinedToken extends GLSLElementImpl {
    public GLSLRedefinedToken(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Override
    public PsiReference getReference() {
        GLSLPreprocessorDirective directive = getDefiningDirective(this);
        if (directive != null) return new GLSLMacroReference(this, directive);
        return null;
    }

    @Override
    @NotNull
    public String getName() {
        return getText();
    }

    @Nullable
    public static GLSLDefineDirective getDefiningDirective(GLSLRedefinedToken start) {
        PsiElement current = start.getPrevSibling();
        if (current == null) current = start.getParent();

        while (current != null) {
            if (current instanceof GLSLDefineDirective) {
                GLSLDefineDirective directive = (GLSLDefineDirective) current;
                if (start.getName().equals(directive.getName())) return directive;
            }
            if (current.getPrevSibling() == null) {
                current = current.getParent();
                if (current instanceof PsiFile) return null;
            } else {
                current = current.getPrevSibling();
            }
        }
        return null;
    }
}
