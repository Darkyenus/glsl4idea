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
import org.jetbrains.annotations.NotNull;

/**
 * GLSLGroupedExpression is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 28, 2009
 *         Time: 2:27:41 PM
 */
public class GLSLGroupedExpression extends GLSLPrimaryExpression {
    public GLSLGroupedExpression(@NotNull ASTNode astNode) {
        super(astNode);
    }

    public GLSLExpression getExpression() {
        GLSLExpression expr = findChildByClass(GLSLExpression.class);
        if (expr != null) {
            return expr;
        } else {
            throw new RuntimeException("Grouped expression does not contain any expression!");
        }
    }

    @NotNull
    @Override
    public GLSLType getType() {
        return getExpression().getType();
    }

    public String toString() {
        return "Grouped Expression: '()'";
    }
}
