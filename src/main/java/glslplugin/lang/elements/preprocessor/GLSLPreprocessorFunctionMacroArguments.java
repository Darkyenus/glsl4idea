package glslplugin.lang.elements.preprocessor;

import com.intellij.lang.ASTNode;
import glslplugin.lang.elements.GLSLElementImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Wraps preprocessor function macro arguments to move them out of the tree and to highlight them.
 */
public class GLSLPreprocessorFunctionMacroArguments extends GLSLElementImpl {
    public GLSLPreprocessorFunctionMacroArguments(@NotNull ASTNode astNode) {
        super(astNode);
    }
}
