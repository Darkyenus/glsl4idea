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
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * GLSLElement is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 6, 2009
 *         Time: 1:33:24 PM
 */
public interface GLSLElement extends NavigatablePsiElement {

    /**
     * @return the parent if the parent is of the given class or null otherwise
     */
    @Nullable
    <T extends GLSLElement> T findParentByClass(Class<T> clazz);

    /**
     * @return the parent if the parent is one of the given classes or null otherwise
     */
    @Nullable
    PsiElement findParentByClasses(Class<? extends PsiElement>... clazzes);
    @Nullable
    PsiElement findParentByClasses(Collection<Class<? extends PsiElement>> clazzes);

    /**
     * Checks whether this is a descendant of element.
     * That is; if element is an ancestor of this.
     * Loops through the parent chains and reports whether or not element is found.
     *
     * @param ancestor the proposed ancestor of this.
     * @return true if ancestor is indeed the ancestor of this, false otherwise.
     */
    boolean isDescendantOf(PsiElement ancestor);
}
