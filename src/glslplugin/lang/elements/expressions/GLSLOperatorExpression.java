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
import com.intellij.psi.tree.IElementType;
import glslplugin.lang.elements.GLSLTokenTypes;
import org.jetbrains.annotations.NotNull;

/**
 * GLSLOperatorExpression is the base class for all operator expressions.
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 28, 2009
 *         Time: 2:50:42 PM
 */
public abstract class GLSLOperatorExpression extends GLSLExpression {

    public GLSLOperatorExpression(@NotNull ASTNode astNode) {
        super(astNode);
    }

    protected GLSLExpression[] getOperands() {
        return findChildrenByClass(GLSLExpression.class);
    }

    public GLSLOperator getOperator() {
        ASTNode operatorNode = getNode().findChildByType(GLSLTokenTypes.OPERATORS);
        if (operatorNode != null) {
            return getOperatorFromType(operatorNode.getElementType());
        } else {
            throw new RuntimeException("Operator does not contain an operator token.");
        }
    }

    @NotNull
    protected GLSLOperator getOperatorFromType(final IElementType type) {
        if (type == GLSLTokenTypes.INC_OP) return GLSLOperator.INCREMENT;
        if (type == GLSLTokenTypes.DEC_OP) return GLSLOperator.DECREMENT;
        if (type == GLSLTokenTypes.BANG) return GLSLOperator.LOGIC_NEGATION;
        if (type == GLSLTokenTypes.TILDE) return GLSLOperator.BINARY_NEGATION;
        if (type == GLSLTokenTypes.PLUS) return GLSLOperator.ADDITION;
        if (type == GLSLTokenTypes.DASH) return GLSLOperator.SUBTRACTION;
        if (type == GLSLTokenTypes.STAR) return GLSLOperator.MULTIPLICATION;
        if (type == GLSLTokenTypes.SLASH) return GLSLOperator.DIVISION;
        if (type == GLSLTokenTypes.PERCENT) return GLSLOperator.MODULO;

        if (type == GLSLTokenTypes.EQUAL) return GLSLOperator.ASSIGN;
        if (type == GLSLTokenTypes.ADD_ASSIGN) return GLSLOperator.ADDITION_ASSIGN;
        if (type == GLSLTokenTypes.SUB_ASSIGN) return GLSLOperator.SUBTRACTION_ASSIGN;
        if (type == GLSLTokenTypes.MUL_ASSIGN) return GLSLOperator.MULTIPLICATION_ASSIGN;
        if (type == GLSLTokenTypes.DIV_ASSIGN) return GLSLOperator.DIVISION_ASSIGN;
        if (type == GLSLTokenTypes.MOD_ASSIGN) return GLSLOperator.MODULO_ASSIGN;
        if (type == GLSLTokenTypes.LEFT_ASSIGN) return GLSLOperator.LEFT_SHIFT_ASSIGN;
        if (type == GLSLTokenTypes.RIGHT_ASSIGN) return GLSLOperator.RIGHT_SHIFT_ASSIGN;
        if (type == GLSLTokenTypes.AND_ASSIGN) return GLSLOperator.BINARY_AND_ASSIGN;
        if (type == GLSLTokenTypes.XOR_ASSIGN) return GLSLOperator.BINARY_XOR_ASSIGN;
        if (type == GLSLTokenTypes.OR_ASSIGN) return GLSLOperator.BINARY_OR_ASSIGN;

        if (type == GLSLTokenTypes.AND_OP) return GLSLOperator.LOGIC_AND;
        if (type == GLSLTokenTypes.OR_OP) return GLSLOperator.LOGIC_OR;
        if (type == GLSLTokenTypes.XOR_OP) return GLSLOperator.LOGIC_XOR;

        if (type == GLSLTokenTypes.AMPERSAND) return GLSLOperator.BINARY_AND;
        if (type == GLSLTokenTypes.CARET) return GLSLOperator.BINARY_XOR;
        if (type == GLSLTokenTypes.VERTICAL_BAR) return GLSLOperator.BINARY_OR;
        if (type == GLSLTokenTypes.LEFT_OP) return GLSLOperator.BINARY_LEFT_SHIFT;
        if (type == GLSLTokenTypes.RIGHT_OP) return GLSLOperator.BINARY_RIGHT_SHIFT;

        if (type == GLSLTokenTypes.EQ_OP) return GLSLOperator.EQUAL;
        if (type == GLSLTokenTypes.NE_OP) return GLSLOperator.NOT_EQUAL;
        if (type == GLSLTokenTypes.LEFT_ANGLE) return GLSLOperator.LESSER;
        if (type == GLSLTokenTypes.RIGHT_ANGLE) return GLSLOperator.GREATER;
        if (type == GLSLTokenTypes.LE_OP) return GLSLOperator.LESSER_EQUAL;
        if (type == GLSLTokenTypes.GE_OP) return GLSLOperator.GREATER_EQUAL;

        if (type == GLSLTokenTypes.DOT) return GLSLOperator.MEMBER;

        throw new RuntimeException("Unsupported Operator: '" + getText() + "'");
    }
}
