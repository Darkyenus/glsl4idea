package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.expressions.GLSLExpression;
import glslplugin.lang.elements.statements.GLSLCaseStatement;
import glslplugin.lang.elements.statements.GLSLDefaultStatement;
import glslplugin.lang.elements.statements.GLSLLabelStatement;
import glslplugin.lang.elements.statements.GLSLSwitchStatement;
import glslplugin.lang.elements.types.GLSLScalarType;
import glslplugin.lang.elements.types.GLSLType;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Switch statements can only operate on integer expressions.
 * This checks that correct expression type is used and warns otherwise.
 * <p/>
 * Also checks for constant expressions and warns on them.
 * <p/>
 * For label statements, it is checked that:
 * - max one default label exists
 * - all case label types are integer scalars
 * - no case labels are duplicated
 *
 * @author Darkyen
 */
public class SwitchAnnotation extends Annotator<GLSLSwitchStatement> {

    @Override
    public void annotate(GLSLSwitchStatement expr, AnnotationHolder holder) {
        final GLSLExpression switchCondition = expr.getSwitchCondition();
        if (switchCondition == null) return;

        final GLSLType switchConditionType = switchCondition.getType();
        if (!switchConditionType.isValidType()) return;

        if (!GLSLScalarType.isIntegerScalar(switchConditionType)) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Expression must be of integer scalar type").range(switchCondition).create();
        } else if (switchCondition.isConstantValue()) {
            holder.newAnnotation(HighlightSeverity.WEAK_WARNING, "Expression is constant").range(switchCondition).create();
        }

        final List<GLSLLabelStatement> labelStatements = expr.getLabelStatements();


        Set<Object> encounteredCases = new HashSet<>();
        boolean defaultFound = false;

        for (GLSLLabelStatement label : labelStatements) {
            if (label instanceof GLSLDefaultStatement) {
                if (defaultFound) {
                    holder.newAnnotation(HighlightSeverity.ERROR, "Multiple default labels are not allowed").range(label).create();
                }
                defaultFound = true;
            } else if (label instanceof GLSLCaseStatement) {//This _should_ be the only possible way
                final GLSLCaseStatement caseLabel = (GLSLCaseStatement) label;
                final GLSLExpression caseExpression = caseLabel.getCaseExpression();
                if (caseExpression != null) {
                    final GLSLType caseExpressionType = caseExpression.getType();
                    if (caseExpressionType.isValidType()) {
                        if (!GLSLScalarType.isIntegerScalar(caseExpressionType)) {
                            holder.newAnnotation(HighlightSeverity.ERROR, "Case expression must be of integer scalar type").range(caseExpression).create();
                        } else {
                            //It is a valid type, do dupe check
                            if (caseExpression.isConstantValue()) {
                                Object constantValue = caseExpression.getConstantValue();
                                //constantValue should be Long, but don't dwell on that
                                if (encounteredCases.contains(constantValue)) {
                                    holder.newAnnotation(HighlightSeverity.WARNING, "Duplicate case label (" + constantValue + ")").range(caseExpression).create();
                                }
                                encounteredCases.add(constantValue);
                            }
                        }
                    }
                }
            }
        }
    }

    @NotNull
    @Override
    public Class<GLSLSwitchStatement> getElementType() {
        return GLSLSwitchStatement.class;
    }
}
