package glslplugin.lang.elements.expressions;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Comma separated expressions, takes the value of the rightmost sub-expression.
 */
public class GLSLSequenceExpression extends GLSLExpression {

    public GLSLSequenceExpression(@NotNull ASTNode astNode) {
        super(astNode);
    }

    public @NotNull GLSLExpression @NotNull[] getSequenceExpressions() {
        return findChildrenByClass(GLSLExpression.class);
    }

    private @Nullable GLSLExpression getLastSequenceExpression() {
        for (PsiElement cur = getLastChild(); cur != null; cur = cur.getPrevSibling()) {
            if (cur instanceof GLSLExpression expr) return expr;
        }
        return null;
    }

    @Override
    public boolean isLValue() {
        final GLSLExpression expr = getLastSequenceExpression();
        return expr != null && expr.isLValue();
    }

    @Override
    public boolean isConstantValue() {
        final GLSLExpression expr = getLastSequenceExpression();
        return expr != null && expr.isConstantValue();
    }

    @Override
    public @Nullable Object getConstantValue() {
        final GLSLExpression expr = getLastSequenceExpression();
        return expr == null ? null : expr.getConstantValue();
    }

    @Override
    public @NotNull GLSLType getType() {
        final GLSLExpression expr = getLastSequenceExpression();
        return expr == null ? GLSLTypes.UNKNOWN_TYPE : expr.getType();
    }
}
