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
import glslplugin.lang.elements.expressions.operator.GLSLOperator;
import glslplugin.lang.elements.expressions.operator.GLSLOperators;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

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

    @NotNull
    protected GLSLExpression[] getOperands() {
        return findChildrenByClass(GLSLExpression.class);
    }

    @Nullable
    public GLSLOperator getOperator() {
        ASTNode operatorNode = getNode().findChildByType(GLSLTokenTypes.OPERATORS);
        if (operatorNode != null) {
            return getOperatorFromType(operatorNode.getElementType());
        } else {
            return null;
        }
    }

    @Nullable
    protected GLSLOperator getOperatorFromType(final IElementType type) {
        if (type == GLSLTokenTypes.INC_OP) return GLSLOperators.INCREMENT;
        if (type == GLSLTokenTypes.DEC_OP) return GLSLOperators.DECREMENT;
        if (type == GLSLTokenTypes.BANG) return GLSLOperators.LOGIC_NEGATION;
        if (type == GLSLTokenTypes.TILDE) return GLSLOperators.BINARY_NEGATION;
        if (type == GLSLTokenTypes.PLUS) return GLSLOperators.ADDITION;
        if (type == GLSLTokenTypes.DASH) return GLSLOperators.SUBTRACTION;
        if (type == GLSLTokenTypes.STAR) return GLSLOperators.MULTIPLICATION;
        if (type == GLSLTokenTypes.SLASH) return GLSLOperators.DIVISION;
        if (type == GLSLTokenTypes.PERCENT) return GLSLOperators.MODULO;

        if (type == GLSLTokenTypes.EQUAL) return GLSLOperators.ASSIGN;
        if (type == GLSLTokenTypes.ADD_ASSIGN) return GLSLOperators.ADDITION_ASSIGN;
        if (type == GLSLTokenTypes.SUB_ASSIGN) return GLSLOperators.SUBTRACTION_ASSIGN;
        if (type == GLSLTokenTypes.MUL_ASSIGN) return GLSLOperators.MULTIPLICATION_ASSIGN;
        if (type == GLSLTokenTypes.DIV_ASSIGN) return GLSLOperators.DIVISION_ASSIGN;
        if (type == GLSLTokenTypes.MOD_ASSIGN) return GLSLOperators.MODULO_ASSIGN;
        if (type == GLSLTokenTypes.LEFT_ASSIGN) return GLSLOperators.LEFT_SHIFT_ASSIGN;
        if (type == GLSLTokenTypes.RIGHT_ASSIGN) return GLSLOperators.RIGHT_SHIFT_ASSIGN;
        if (type == GLSLTokenTypes.AND_ASSIGN) return GLSLOperators.BINARY_AND_ASSIGN;
        if (type == GLSLTokenTypes.XOR_ASSIGN) return GLSLOperators.BINARY_XOR_ASSIGN;
        if (type == GLSLTokenTypes.OR_ASSIGN) return GLSLOperators.BINARY_OR_ASSIGN;

        if (type == GLSLTokenTypes.AND_OP) return GLSLOperators.LOGIC_AND;
        if (type == GLSLTokenTypes.OR_OP) return GLSLOperators.LOGIC_OR;
        if (type == GLSLTokenTypes.XOR_OP) return GLSLOperators.LOGIC_XOR;

        if (type == GLSLTokenTypes.AMPERSAND) return GLSLOperators.BINARY_AND;
        if (type == GLSLTokenTypes.CARET) return GLSLOperators.BINARY_XOR;
        if (type == GLSLTokenTypes.VERTICAL_BAR) return GLSLOperators.BINARY_OR;
        if (type == GLSLTokenTypes.LEFT_OP) return GLSLOperators.BINARY_LEFT_SHIFT;
        if (type == GLSLTokenTypes.RIGHT_OP) return GLSLOperators.BINARY_RIGHT_SHIFT;

        if (type == GLSLTokenTypes.EQ_OP) return GLSLOperators.EQUAL;
        if (type == GLSLTokenTypes.NE_OP) return GLSLOperators.NOT_EQUAL;
        if (type == GLSLTokenTypes.LEFT_ANGLE) return GLSLOperators.LESSER;
        if (type == GLSLTokenTypes.RIGHT_ANGLE) return GLSLOperators.GREATER;
        if (type == GLSLTokenTypes.LE_OP) return GLSLOperators.LESSER_EQUAL;
        if (type == GLSLTokenTypes.GE_OP) return GLSLOperators.GREATER_EQUAL;

        if (type == GLSLTokenTypes.DOT) return GLSLOperators.MEMBER;

        Logger.getLogger("Unsupported Operator: '" + getText() + "'");
        return null;
    }
}
