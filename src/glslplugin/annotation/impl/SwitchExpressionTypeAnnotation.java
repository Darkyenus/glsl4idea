package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.expressions.GLSLExpression;
import glslplugin.lang.elements.statements.GLSLSwitchStatement;
import glslplugin.lang.elements.types.GLSLScalarType;
import glslplugin.lang.elements.types.GLSLType;
import org.jetbrains.annotations.NotNull;

/**
 * Switch statements can only operate on integer expressions.
 * This checks that correct expression type is used and warns otherwise.
 *
 * Also checks for constant expressions and warns on them.
 *
 * @author Darkyen
 */
public class SwitchExpressionTypeAnnotation extends Annotator<GLSLSwitchStatement> {

    @Override
    public void annotate(GLSLSwitchStatement expr, AnnotationHolder holder) {
        final GLSLExpression switchCondition = expr.getSwitchCondition();
        if(switchCondition == null)return;

        final GLSLType switchConditionType = switchCondition.getType();
        if(!switchConditionType.isValidType())return;

        if(!GLSLScalarType.isIntegerScalar(switchConditionType)){
            holder.createErrorAnnotation(switchCondition, "Expression must be of integer scalar type");
            return;
        }

        if(switchCondition.isConstantValue()){
            holder.createWeakWarningAnnotation(switchCondition, "Expression is constant");
        }
    }

    @NotNull
    @Override
    public Class<GLSLSwitchStatement> getElementType() {
        return GLSLSwitchStatement.class;
    }
}
