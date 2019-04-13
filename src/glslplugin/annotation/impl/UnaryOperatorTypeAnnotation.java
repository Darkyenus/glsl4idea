package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.expressions.GLSLExpression;
import glslplugin.lang.elements.expressions.GLSLUnaryOperatorExpression;
import glslplugin.lang.elements.expressions.operator.GLSLOperator;
import glslplugin.lang.elements.types.GLSLType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Darkyen
 */
public class UnaryOperatorTypeAnnotation extends Annotator<GLSLUnaryOperatorExpression> {

    @Override
    public void annotate(GLSLUnaryOperatorExpression expr, AnnotationHolder holder) {
        final GLSLExpression operand = expr.getOperand();
        final GLSLOperator operator = expr.getOperator();
        if (operand == null || operator == null) {
            return;
        }

        if (!(operator instanceof GLSLOperator.GLSLUnaryOperator)) {
            holder.createErrorAnnotation(expr, '\''+operator.getTextRepresentation()+"' is not an unary operator");
            return;
        }

        GLSLOperator.GLSLUnaryOperator unaryOperator = (GLSLOperator.GLSLUnaryOperator) operator;
        final GLSLType operandType = operand.getType();
        if (operandType.isValidType()) {
            if (!unaryOperator.isValidInput(operandType)) {
                holder.createErrorAnnotation(expr,
                        "'" + operator.getTextRepresentation() + "' does not operate on '" + operandType.getTypename() + "'");
            }
        }
    }

    @NotNull
    @Override
    public Class<GLSLUnaryOperatorExpression> getElementType() {
        return GLSLUnaryOperatorExpression.class;
    }
}
