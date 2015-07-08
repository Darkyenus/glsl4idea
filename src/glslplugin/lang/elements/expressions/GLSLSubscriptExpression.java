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
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

/**
 * Expression of selecting an element from (probably) an array.
 * For example: "example_array[3]".
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 30, 2009
 *         Time: 11:40:03 AM
 */
public class GLSLSubscriptExpression extends GLSLOperatorExpression {
    public GLSLSubscriptExpression(ASTNode node) {
        super(node);
    }

    /**
     * @return expression that is being selected from, that is "example_array" from above example.
     */
    @Nullable
    public GLSLExpression getArrayExpression() {
        GLSLExpression[] operands = getOperands();
        if (operands.length == 2) {
            return operands[0];
        } else {
            Logger.getLogger("GLSLSubscriptExpression").warning("Binary operator with " + operands.length + " operand(s).");
            return null;
        }
    }

    @Nullable
    public GLSLExpression getSubscript() {
        GLSLExpression[] operands = getOperands();
        if (operands.length == 2) {
            return operands[1];
        } else {
            Logger.getLogger("GLSLSubscriptExpression").warning("Binary operator with " + operands.length + " operand(s).");
            return null;
        }
    }



    @Override
    public boolean isLValue() {
        GLSLExpression arrExpr = getArrayExpression();
        //noinspection SimplifiableIfStatement
        if(arrExpr != null)return arrExpr.isLValue();
        else return true;
    }

    @Override
    public String toString() {
        return "Subscript expression";
    }

    @NotNull
    @Override
    public GLSLType getType() {
        GLSLExpression left = getArrayExpression();
        if(left == null)return GLSLTypes.UNKNOWN_TYPE;
        GLSLType type = left.getType();
        if (type != GLSLTypes.UNKNOWN_TYPE) {
            if (type.isIndexable()) {
                return type.getIndexType();
            } else {
                return GLSLTypes.UNKNOWN_TYPE;
            }
        }
        return GLSLTypes.UNKNOWN_TYPE;
    }
}
