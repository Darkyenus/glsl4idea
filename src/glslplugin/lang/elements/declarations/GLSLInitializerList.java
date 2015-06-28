package glslplugin.lang.elements.declarations;

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import glslplugin.lang.elements.GLSLElementImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by abigail on 28/06/15.
 */
public class GLSLInitializerList extends GLSLElementImpl {
    public GLSLInitializerList(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @NotNull
    public List<GLSLInitializer> getInitializers() {
        return PsiTreeUtil.getChildrenOfTypeAsList(this, GLSLInitializer.class);
    }

    @Override
    public String toString() {
        return "GLSLInitializerList";
    }
}
