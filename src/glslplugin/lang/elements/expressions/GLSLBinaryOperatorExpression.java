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

package glslplugin.lang.elements.expressions;

import com.intellij.lang.ASTNode;
import glslplugin.lang.elements.expressions.operator.GLSLOperator;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * GLSLBinaryOperatorExpression is an expression from two operands and one operator between them.
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 28, 2009
 *         Time: 3:17:16 PM
 */
public class GLSLBinaryOperatorExpression extends GLSLOperatorExpression {
    public GLSLBinaryOperatorExpression(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    public GLSLExpression getLeftOperand() {
        GLSLExpression[] operands = getOperands();
        if (operands.length == 2) {
            return operands[0];
        } else {
            return null;
        }
    }

    @Nullable
    public GLSLExpression getRightOperand() {
        GLSLExpression[] operands = getOperands();
        if (operands.length == 2) {
            return operands[1];
        } else {
            return null;
        }
    }

    @Override
    public boolean isConstantValue() {
        return getConstantValue() != null;
    }

    @Nullable
    @Override
    public Object getConstantValue() {
        GLSLExpression leftOperand = getLeftOperand();
        GLSLExpression rightOperand = getRightOperand();
        if(leftOperand == null || rightOperand == null || !leftOperand.isConstantValue() || !rightOperand.isConstantValue())return null;
        GLSLOperator operator = getOperator();
        if(operator == null || !(operator instanceof GLSLOperator.GLSLBinaryOperator))return null;
        GLSLOperator.GLSLBinaryOperator binaryOperator = (GLSLOperator.GLSLBinaryOperator) operator;
        GLSLType leftOperandType = leftOperand.getType();
        GLSLType rightOperandType = rightOperand.getType();
        if(!leftOperandType.isValidType() || !rightOperandType.isValidType())return null;
        if(!binaryOperator.isValidInput(leftOperandType, rightOperandType))return null;

        return binaryOperator.getResultValue(leftOperand.getConstantValue(), rightOperand.getConstantValue());
    }

    @NotNull
    @Override
    public GLSLType getType() {
        GLSLOperator operator = getOperator();
        if (operator instanceof GLSLOperator.GLSLBinaryOperator) {
            GLSLOperator.GLSLBinaryOperator binaryOperator = (GLSLOperator.GLSLBinaryOperator) operator;
            GLSLExpression leftOperand = getLeftOperand();
            GLSLExpression rightOperand = getRightOperand();
            return binaryOperator.getResultType(leftOperand == null ? GLSLTypes.UNKNOWN_TYPE : leftOperand.getType(),
                    rightOperand == null ? GLSLTypes.UNKNOWN_TYPE : rightOperand.getType());
        } else {
            return GLSLTypes.UNKNOWN_TYPE;
        }
    }

    public String toString() {
        GLSLOperator operator = getOperator();
        if (operator == null) {
            return "Binary Operator: '(unknown)'";
        } else {
            return "Binary Operator: '" + operator.getTextRepresentation() + "'";
        }
    }
}
