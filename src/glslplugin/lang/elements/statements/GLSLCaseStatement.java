package glslplugin.lang.elements.statements;

import com.intellij.lang.ASTNode;
import glslplugin.lang.elements.GLSLElementImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by abigail on 08/07/15.
 */
public class GLSLCaseStatement extends GLSLStatement {
    public GLSLCaseStatement(@NotNull ASTNode astNode) {
        super(astNode);
    }
}
