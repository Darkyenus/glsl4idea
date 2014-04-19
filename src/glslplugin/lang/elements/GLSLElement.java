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

import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;

/**
 * GLSLElement is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 6, 2009
 *         Time: 1:33:24 PM
 */
public interface GLSLElement extends NavigatablePsiElement {
    <T extends GLSLElement> T findParentByClass(Class<T> clazz);

    GLSLElement findParentByClasses(Class<? extends GLSLElement>... clazzes);

    <T extends GLSLElement> T findPrevSiblingByClass(Class<T> clazz);

    GLSLElement findPrevSiblingByClasses(Class<? extends GLSLElement>... clazzes);

    /**
     * Checks whether this is a descendant of elt.
     * That is; if elt is an ancestor of this.
     * Loops through the parent chains and reports whether or not elt is found.
     *
     * @param ancestor the proposed ancestor of this.
     * @return true if ancestor is indeed the ancestor of this, false otherwise.
     */
    boolean isDescendantOf(PsiElement ancestor);
}
