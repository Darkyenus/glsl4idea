package glslplugin.lang.elements.declarations;

import com.intellij.lang.ASTNode;
import glslplugin.lang.elements.GLSLElementImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Initializer list is defined as list of either initializer lists or initializers.
 * Initializer is an GLSLExpression, hiding inside GLSLInitializerExpression, which holds it.
 *
 * Created by abigail on 28/06/15.
 */
public class GLSLInitializerList extends GLSLElementImpl implements GLSLInitializer {
    public GLSLInitializerList(@NotNull ASTNode astNode) {
        super(astNode);
    }

    /**
     * @return all initializers this contains
     */
    @NotNull
    public GLSLInitializer[] getInitializers() {
        return findChildrenByClass(GLSLInitializer.class);
    }

    @Override
    public String toString() {
        return "GLSLInitializerList("+getInitializers().length+")";
    }
}
