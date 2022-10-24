package glslplugin.lang.elements.preprocessor;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.GLSLTokenTypes;
import org.jetbrains.annotations.NotNull;

/**
 *
 * Created by abigail on 08/07/15.
 */
public class GLSLPreprocessorDirective extends GLSLElementImpl {
    public GLSLPreprocessorDirective(@NotNull ASTNode astNode) {
        super(astNode);
    }
}
