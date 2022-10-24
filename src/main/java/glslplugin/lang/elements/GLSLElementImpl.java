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

package glslplugin.lang.elements;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;

public class GLSLElementImpl extends ASTWrapperPsiElement implements GLSLElement {

    public GLSLElementImpl(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Override
    public String toString() {
        if (this.getClass() == GLSLElementImpl.class) return "GLSLElementImpl(" + getNode().getElementType() + ")";
        if (getName() != null) return this.getClass().getSimpleName() + "(" + getName() + ")";
        return this.getClass().getSimpleName();
    }

    ////////////////////////////
    // Utility methods

    @Override
    @Nullable
    public final <T extends GLSLElement> T findParentByClass(Class<T> clazz) {
        return clazz.cast(findParentByClasses(clazz));
    }

    @SafeVarargs
    @Nullable
    public final PsiElement findParentByClasses(Class<? extends PsiElement>... clazzes) {
        return findParentByClasses(Arrays.asList(clazzes));
    }

    @Nullable
    public final PsiElement findParentByClasses(Collection<Class<? extends PsiElement>> clazzes) {
        PsiElement parent = getParent();
        while (parent != null) {
            for (Class<? extends PsiElement> clazz : clazzes) {
                if (clazz.isInstance(parent)) {
                    return clazz.cast(parent);
                }
            }
            parent = parent.getParent();
        }
        return null;
    }

    @Override
    public <T> @Nullable T findChildByClass(Class<T> aClass) {
        return super.findChildByClass(aClass);
    }

    @Override
    public <T extends PsiElement> @Nullable T findChildByType(IElementType type) {
        return super.findChildByType(type);
    }

    @Override
    public <T> T @NotNull [] findChildrenByClass(Class<T> aClass) {
        return super.findChildrenByClass(aClass);
    }
}
