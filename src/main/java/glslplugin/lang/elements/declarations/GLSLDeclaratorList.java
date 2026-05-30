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
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import glslplugin.lang.elements.GLSLElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

/**
 * GLSLDeclaratorList is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 28, 2009
 *         Time: 7:10:04 PM
 */
public class GLSLDeclaratorList extends GLSLElementImpl implements Iterable<GLSLDeclarator> {
    public GLSLDeclaratorList(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @NotNull
    public GLSLDeclarator[] getDeclarators() {
        return findChildrenByClass(GLSLDeclarator.class);
    }

    public boolean processDeclarators(
        @NotNull PsiScopeProcessor processor,
        @NotNull ResolveState state,
        @Nullable PsiElement lastParent,
        @NotNull PsiElement place
    ) {
        for (PsiElement child = getFirstChild(); child != null; child = child.getNextSibling()) {
            if (!(child instanceof GLSLDeclarator declarator)) {
                continue;
            }
            if (lastParent != null && PsiTreeUtil.isAncestor(lastParent, declarator, false)) {
                break;
            }
            if (!declarator.processDeclarations(processor, state, null, place)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Declarator List (" + getDeclarators().length + " declarators)";
    }

    @NotNull
    @Override
    public Iterator<GLSLDeclarator> iterator() {
        return java.util.Arrays.asList(getDeclarators()).iterator();
    }
}
