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
import org.jetbrains.annotations.NotNull;

/**
 * GLSLAssignmentExpression is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 30, 2009
 *         Time: 10:51:02 AM
 */
public class GLSLAssignmentExpression extends GLSLBinaryOperatorExpression {
    public GLSLAssignmentExpression(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Override
    public boolean isLValue() {
        return true;
    }

    @Override
    public String toString() {
        GLSLOperator operator = getOperator();
        if(operator == null){
            return "Assignment: (unknown)";
        }else{
            return "Assignment: " + operator.getTextRepresentation();
        }
    }
}
