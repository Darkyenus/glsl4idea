package glslplugin.lang.elements.statements;

import com.intellij.lang.ASTNode;
import glslplugin.lang.elements.expressions.GLSLExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * "case" label statement used in switch statements
 * <p/>
 * Created by abigail on 08/07/15.
 */
public class GLSLCaseStatement extends GLSLStatement implements GLSLLabelStatement {

    public GLSLCaseStatement(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    public GLSLExpression getCaseExpression() {
        return findChildByClass(GLSLExpression.class);
    }
}
