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

import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import glslplugin.lang.elements.GLSLElementImpl;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.Nullable;

/**
 * NewVariableDeclaration is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 2, 2009
 *         Time: 1:14:40 AM
 */
public class GLSLVariableDeclaration extends GLSLElementImpl implements GLSLQualifiedDeclaration {
    public GLSLVariableDeclaration(@NotNull ASTNode astNode) {
        super(astNode);
    }

    public GLSLDeclarator @NotNull[] getDeclarators() {
        final GLSLDeclaratorList list = findChildByClass(GLSLDeclaratorList.class);
        if (list == null) return GLSLDeclarator.NO_DECLARATORS;
        else return list.getDeclarators();
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, @Nullable PsiElement lastParent, @NotNull PsiElement place) {
        for (GLSLDeclarator declarator : getDeclarators()) {
            if (PsiTreeUtil.isAncestor(lastParent, declarator, false))
                break;// Can't see later-defined stuff

            if (!declarator.processDeclarations(processor, state, null, place))
                return false;
        }

        GLSLTypeSpecifier specifier = getTypeSpecifierNode();
        if (specifier != null && !PsiTreeUtil.isAncestor(lastParent, specifier, false)) {
            return specifier.processDeclarations(processor, state, null, place);
        }
        return true;
    }

    @Override
    public String toString() {
        return "Variable Declaration: " + GLSLDeclarator.toString(getDeclarators());
    }

    @NotNull
    @Override
    public String getDeclarationDescription() {
        if (PsiTreeUtil.getParentOfType(this, GLSLFunctionDefinition.class) != null) {
            return "local variable";
        }
        return "global variable";
    }


    @Override
    public <T> @Nullable T findChildByClass(Class<T> aClass) {
        return super.findChildByClass(aClass);
    }
}
