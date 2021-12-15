package glslplugin.lang.elements.preprocessor;

import com.intellij.lang.ASTNode;
import glslplugin.lang.elements.GLSLElementImpl;
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
