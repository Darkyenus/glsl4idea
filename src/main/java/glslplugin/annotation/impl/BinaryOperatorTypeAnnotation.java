/*
 *     Copyright 2010 Jean-Paul Balabanian and Yngve Devik Hammersland
 *
 *     This file is part of glsl4idea.
 *
 *     Glsl4idea is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as
 *     published by the Free Software Foundation, either version 3 of
 *     the License, or (at your option) any later version.
 *
 *     Glsl4idea is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with glsl4idea.  If not, see <http://www.gnu.org/licenses/>.
 */

package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.expressions.GLSLBinaryOperatorExpression;
import glslplugin.lang.elements.expressions.GLSLExpression;
import glslplugin.lang.elements.expressions.operator.GLSLOperator;
import glslplugin.lang.elements.types.GLSLType;
import org.jetbrains.annotations.NotNull;

/**
 * BinaryOperatorTypeAnnotation is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Mar 2, 2009
 *         Time: 11:38:57 AM
 */
public class BinaryOperatorTypeAnnotation extends Annotator<GLSLBinaryOperatorExpression> {

    public void annotate(GLSLBinaryOperatorExpression expr, AnnotationHolder holder) {
        final GLSLExpression left = expr.getLeftOperand();
        final GLSLExpression right = expr.getRightOperand();
        final GLSLOperator operator = expr.getOperator();
        if(left == null || right == null || operator == null)return; //There are bigger problems than type compatibility

        if(!(operator instanceof GLSLOperator.GLSLBinaryOperator binaryOperator)) {
            holder.newAnnotation(HighlightSeverity.ERROR, "'"+operator.getTextRepresentation()+"' is not a binary operator").create();
            return;
        }

        final GLSLType rightType = right.getType();
        final GLSLType leftType = left.getType();

        if (leftType.isValidType() && rightType.isValidType()) {
            if(!binaryOperator.isValidInput(leftType, rightType)){
                holder.newAnnotation(HighlightSeverity.ERROR, "Incompatible types as operands of '" + operator.getTextRepresentation() + "': '"
                        + leftType.getTypename() + "' and '" + rightType.getTypename() + "'").create();
            }
        }
    }

    @NotNull
    @Override
    public Class<GLSLBinaryOperatorExpression> getElementType() {
        return GLSLBinaryOperatorExpression.class;
    }
}
