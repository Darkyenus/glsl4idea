package glslplugin.lang.elements.statements;

import com.intellij.lang.ASTNode;
import glslplugin.lang.elements.expressions.GLSLExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a switch statement, with control expression and compound expression body.
 * <p/>
 * Created by abigail on 08/07/15.
 */
public class GLSLSwitchStatement extends GLSLStatement {

    public GLSLSwitchStatement(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    public GLSLExpression getSwitchCondition() {
        return findChildByClass(GLSLExpression.class);
    }

    @NotNull
    public GLSLStatement[] getSwitchBodyStatements() {
        final GLSLCompoundStatement body = findChildByClass(GLSLCompoundStatement.class);
        if (body != null) return body.getStatements();
        else return GLSLStatement.NO_STATEMENTS;
    }

    @NotNull
    public List<GLSLLabelStatement> getLabelStatements() {
        List<GLSLLabelStatement> result = new ArrayList<GLSLLabelStatement>();
        for (GLSLStatement statement : getSwitchBodyStatements()) {
            if (statement instanceof GLSLLabelStatement) {
                result.add((GLSLLabelStatement) statement);
            }
        }
        return result;
    }
}
