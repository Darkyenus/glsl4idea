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
import glslplugin.lang.parser.GLSLFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GLSLElementImpl extends ASTWrapperPsiElement implements GLSLElement {

    public GLSLElementImpl(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Override
    public String toString() {
        return "GLSLElementImpl:" + getNode().getElementType();
    }


    ////////////////////////////
    // Utility methods

    @Nullable
    public final <T extends GLSLElement> T findParentByClass(Class<T> clazz) {
        //noinspection unchecked
        return clazz.cast(findParentByClasses(clazz));
    }

    @Nullable
    public final GLSLElement findParentByClasses(Class<? extends GLSLElement>... clazzes) {
        PsiElement parent = getParent();
        while (parent != null) {
            for (Class<? extends GLSLElement> clazz : clazzes) {
                if (clazz.isInstance(parent)) {
                    return clazz.cast(parent);
                }
            }
            parent = parent.getParent();
        }
        return null;
    }

    public final boolean isDescendantOf(PsiElement ancestor) {
        PsiElement current = this;
        while (current != null && !(current instanceof GLSLFile)) {
            if (current == ancestor) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }
}
