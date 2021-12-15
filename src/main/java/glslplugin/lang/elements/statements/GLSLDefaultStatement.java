package glslplugin.lang.elements.statements;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

/**
 * "default" label statement used in switch statements
 * <p/>
 * Created by abigail on 08/07/15.
 */
public class GLSLDefaultStatement extends GLSLStatement implements GLSLLabelStatement {
    public GLSLDefaultStatement(@NotNull ASTNode astNode) {
        super(astNode);
    }
}
