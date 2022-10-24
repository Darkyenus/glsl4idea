package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
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
        if(operand == null || operator == null)return;

        if(!(operator instanceof GLSLOperator.GLSLUnaryOperator unaryOperator)){
            holder.newAnnotation(HighlightSeverity.ERROR, '\''+operator.getTextRepresentation()+"' is not an unary operator").create();
            return;
        }

        final GLSLType operandType = operand.getType();
        if(operandType.isValidType()){
            if(!unaryOperator.isValidInput(operandType)){
                holder.newAnnotation(HighlightSeverity.ERROR,
                        "'" + operator.getTextRepresentation() + "' does not operate on '" + operandType.getTypename() + "'").create();
            }
        }
    }

    @NotNull
    @Override
    public Class<GLSLUnaryOperatorExpression> getElementType() {
        return GLSLUnaryOperatorExpression.class;
    }
}
