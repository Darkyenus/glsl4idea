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
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;

/**
 * GLSLSubscriptExpression is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 30, 2009
 *         Time: 11:40:03 AM
 */
public class GLSLSubscriptExpression extends GLSLOperatorExpression {
    public GLSLSubscriptExpression(ASTNode node) {
        super(node);
    }

    public GLSLExpression getArrayExpression() {
        GLSLExpression[] operands = getOperands();
        if (operands.length == 2) {
            return operands[0];
        } else {
            throw new RuntimeException("Binary operator with " + operands.length + " operand(s).");
        }
    }

    public GLSLExpression getSubscriptExpression() {
        GLSLExpression[] operands = getOperands();
        if (operands.length == 2) {
            return operands[0];
        } else {
            throw new RuntimeException("Binary operator with " + operands.length + " operand(s).");
        }
    }

    @Override
    public boolean isLValue() {
        return getArrayExpression().isLValue();
    }

    @Override
    public String toString() {
        return "Subscript expression";
    }

    @NotNull
    @Override
    public GLSLType getType() {
        GLSLExpression left = getArrayExpression();
        GLSLType type = left.getType();
        if (type != GLSLTypes.UNKNOWN_TYPE) {
            if (type.isIndexable()) {
                return type.getBaseType();
            } else {
                return GLSLTypes.INVALID_TYPE;
            }
        }
        return GLSLTypes.UNKNOWN_TYPE;
    }
}
