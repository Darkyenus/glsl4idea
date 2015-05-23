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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

/**
 * PostfixOperator is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 28, 2009
 *         Time: 3:17:16 PM
 */
public class GLSLPrefixOperatorExpression extends GLSLOperatorExpression {
    public GLSLPrefixOperatorExpression(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    public GLSLExpression getOperand() {
        GLSLExpression[] operands = getOperands();
        if (operands.length != 1) {
            return operands[0];
        } else {
            Logger.getLogger("GLSLPrefixOperatorExpression").warning("Prefix operator with " + operands.length + " operand(s).");
            return null;
        }
    }

    /**
     * Overrides the operator type to provide the unary versions of '+' and '-'.
     *
     * @param type the elements type to convert to an operator.
     * @return the resulting operator.
     */
    @Override
    @Nullable
    protected GLSLOperator getOperatorFromType(IElementType type) {
        GLSLOperator op = super.getOperatorFromType(type);
        if (op == GLSLOperator.ADDITION) op = GLSLOperator.PLUS;
        if (op == GLSLOperator.SUBTRACTION) op = GLSLOperator.MINUS;
        return op;
    }

    public String toString() {
        GLSLOperator operator = getOperator();
        if(operator != null){
            return "Prefix Operator '" + operator.getTextRepresentation() + "'";
        }else{
            return "Prefix Operator '(unknown)'";
        }
    }
}