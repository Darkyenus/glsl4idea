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

package glslplugin.lang.elements.statements;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import glslplugin.lang.elements.declarations.GLSLVariableDeclaration;
import org.jetbrains.annotations.Nullable;

/**
 * GLSLDeclarationStatement is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 28, 2009
 *         Time: 6:13:58 PM
 */
public class GLSLDeclarationStatement extends GLSLStatement {
    public GLSLDeclarationStatement(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    public GLSLVariableDeclaration getDeclaration() {
        PsiElement firstChild = getFirstChild();
        if(firstChild instanceof GLSLVariableDeclaration){
            return (GLSLVariableDeclaration) getFirstChild();
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Declaration Statement";
    }
}
