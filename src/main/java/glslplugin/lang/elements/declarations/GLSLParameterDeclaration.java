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
import glslplugin.lang.elements.reference.GLSLReferencableDeclaration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A function parameter declaration.
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 2, 2009
 *         Time: 2:04:56 PM
 */
public class GLSLParameterDeclaration extends GLSLDeclarationImpl {
    public static final GLSLParameterDeclaration[] NO_PARAMETER_DECLARATIONS = new GLSLParameterDeclaration[0];

    public GLSLParameterDeclaration(@NotNull ASTNode astNode) {
        super(astNode);
    }

    boolean hasDeclarator() {
        return findChildByClass(GLSLDeclarator.class) != null;
    }


    @NotNull
    public String getName() {
        final GLSLDeclarator declarator = getDeclarator();
        if (declarator == null) return "";
        return declarator.getName();
    }

    /**
     * Overridden to provide the single GLSLIdentifier.
     * It is not packaged in DECLARATOR_LIST like the other declarations.
     *
     * @return the declarator list.
     */
    @Override
    @NotNull
    public GLSLDeclarator[] getDeclarators() {
        return findChildrenByClass(GLSLDeclarator.class);
    }

    @Nullable
    public GLSLDeclarator getDeclarator() {
        GLSLDeclarator[] declarators = getDeclarators();
        if(declarators.length == 0){
            return null;
        }else{
            return declarators[0];
        }
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("Parameter Declaration: ");
        b.append(getTypeSpecifierNodeTypeName());
        if (hasDeclarator()) {
            b.append(getName());
        }
        return b.toString();
    }

    @NotNull
    @Override
    public String getDeclarationDescription() {
        return "parameter";
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
        for (GLSLDeclarator declarator : getDeclarators()) {
            if (PsiTreeUtil.isAncestor(lastParent, declarator, false) || PsiTreeUtil.isAncestor(place, declarator, false)) {
                continue;
            }
            if (!declarator.processDeclarations(processor, state, null, place))
                return false;
        }
        return true;
    }
}
