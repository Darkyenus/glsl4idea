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
        return new GLSLMacroReference(this);
    }

    @Override
    @NotNull
    public String getName() {
        return getText();
    }

}
