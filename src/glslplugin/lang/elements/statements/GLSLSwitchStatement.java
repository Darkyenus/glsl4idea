package glslplugin.lang.elements.statements;

import com.intellij.lang.ASTNode;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.expressions.GLSLCondition;
import org.jetbrains.annotations.NotNull;

/**
 * Created by abigail on 08/07/15.
 */
public class GLSLSwitchStatement extends GLSLStatement implements ConditionStatement {
    public GLSLSwitchStatement(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Override
    public GLSLCondition getCondition() {
        return findChildByClass(GLSLCondition.class);
    }
}
