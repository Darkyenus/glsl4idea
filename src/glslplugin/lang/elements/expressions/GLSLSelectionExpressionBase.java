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
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

/**
 * GLSLSelectionExpressionBase is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 3, 2009
 *         Time: 12:47:56 PM
 */
public abstract class GLSLSelectionExpressionBase extends GLSLExpression {
    public GLSLSelectionExpressionBase(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    public GLSLExpression getLeftHandExpression() {
        PsiElement first = getFirstChild();
        if (first instanceof GLSLExpression) {
            return (GLSLExpression) first;
        } else {
            Logger.getLogger("GLSLSelectionExpressionBase").warning("Field selection operator missing postfix expression in front of '.'");
            return null;
        }
    }
}
