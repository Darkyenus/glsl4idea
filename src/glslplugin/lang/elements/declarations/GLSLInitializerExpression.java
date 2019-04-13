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

package glslplugin.lang.elements.declarations;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import glslplugin.lang.elements.GLSLElementImpl;
import org.jetbrains.annotations.NotNull;
import glslplugin.lang.elements.expressions.GLSLExpression;
import org.jetbrains.annotations.Nullable;

/**
 * GLSLInitializerExpression is actually just a holder for GLSLExpression.
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 29, 2009
 *         Time: 2:09:11 PM
 */
public class GLSLInitializerExpression extends GLSLElementImpl implements GLSLInitializer {
    public GLSLInitializerExpression(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    public GLSLExpression getInitializerExpression() {
        PsiElement result = getFirstChild();
        if (result instanceof GLSLExpression) {
            return (GLSLExpression) result;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Initializer";
    }
}
