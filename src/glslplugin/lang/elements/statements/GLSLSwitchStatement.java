package glslplugin.lang.elements.statements;

import com.intellij.lang.ASTNode;
import glslplugin.lang.elements.expressions.GLSLExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a switch statement, with control expression and compound expression body.
 *
 * Created by abigail on 08/07/15.
 */
public class GLSLSwitchStatement extends GLSLStatement {

    public GLSLSwitchStatement(@NotNull ASTNode astNode) {
        super(astNode);
    }

    public GLSLExpression getSwitchCondition() {
        return findChildByClass(GLSLExpression.class);
    }
}
