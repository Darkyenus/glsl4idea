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

public class GLSLElementImpl extends ASTWrapperPsiElement implements GLSLElement {

    public GLSLElementImpl(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Override
    public String toString() {
        return "GLSLElementImpl:" + getNode().getElementType();
    }


    ////////////////////////////
    // Utility methods.

    public <T extends GLSLElement> T findParentByClass(Class<T> clazz) {
        //noinspection unchecked
        return clazz.cast(findParentByClasses(clazz));
    }

    public GLSLElement findParentByClasses(Class<? extends GLSLElement>... clazzes) {
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

    public <T extends GLSLElement> T findPrevSiblingByClass(Class<T> clazz) {
        //noinspection unchecked
        return clazz.cast(findPrevSiblingByClasses(clazz));
    }

    public GLSLElement findPrevSiblingByClasses(Class<? extends GLSLElement>... clazzes) {
        PsiElement prev = getPrevSibling();
        while (prev != null) {
            for (Class<? extends GLSLElement> clazz : clazzes) {
                if (clazz.isInstance(prev)) {
                    return clazz.cast(prev);
                }
            }
            prev = prev.getPrevSibling();
        }
        return null;
    }

    /**
     * Checks whether this is a descendant of elt.
     * That is; if elt is an ancestor of this.
     * Loops through the parent chains and reports whether or not elt is found.
     *
     * @param ancestor the proposed ancestor of this.
     * @return true if ancestor is indeed the ancestor of this, false otherwise.
     */
    public boolean isDescendantOf(PsiElement ancestor) {
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
