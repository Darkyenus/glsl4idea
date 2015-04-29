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
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.expressions.GLSLBinaryOperatorExpression;
import glslplugin.lang.elements.expressions.GLSLExpression;
import glslplugin.lang.elements.types.GLSLFunctionType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypeCompatibilityLevel;
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
        final GLSLType rightType = right.getType();
        final GLSLType leftType = left.getType();
        final GLSLFunctionType[] operatorAlternatives = expr.getOperatorTypeAlternatives();

        if (leftType.isValidType() && rightType.isValidType()) {
            boolean compatible = operatorAlternatives.length == 1;
            if (compatible) {
                compatible = operatorAlternatives[0].getParameterCompatibilityLevel(new GLSLType[]{leftType, rightType}) != GLSLTypeCompatibilityLevel.INCOMPATIBLE;
            }

            if (!compatible) {
                holder.createErrorAnnotation(expr, "Incompatible types as operands of '" + expr.getOperator().getTextRepresentation() + "': '"
                        + leftType.getTypename() + "' and '" + rightType.getTypename() + "'");
            }
        }
    }

    @NotNull
    @Override
    public Class<GLSLBinaryOperatorExpression> getElementType() {
        return GLSLBinaryOperatorExpression.class;
    }
}
