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

package glslplugin.annotation;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for all annotators.
 * Registered in {@link glslplugin.annotation.Annotator}.
 */
public abstract class Annotator<T extends PsiElement> {

    public abstract void annotate(T expr, AnnotationHolder holder);

    /**
     * Returns class of T
     */
    //Circumventing type erasure. Called only once.
    @NotNull
    public abstract Class<T> getElementType();

    /**
     * To be called only from glslplugin.annotation.Annotator
     */
    final void annotateGeneric(PsiElement element, AnnotationHolder holder) {
        //noinspection unchecked
        annotate((T) element, holder);
    }
}
