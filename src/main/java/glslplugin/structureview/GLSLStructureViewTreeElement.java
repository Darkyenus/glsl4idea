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

package glslplugin.structureview;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

abstract class GLSLStructureViewTreeElement<T extends PsiElement> implements StructureViewTreeElement, Comparable<GLSLStructureViewTreeElement<?>> {
    private final T element;
    private final List<GLSLStructureViewTreeElement<?>> children = new ArrayList<>();

    public GLSLStructureViewTreeElement(@NotNull T element) {
        this.element = element;
    }

    @NotNull
    public T getValue() {
        return element;
    }

    protected abstract GLSLPresentation createPresentation(@NotNull T element);

    @NotNull
    public final ItemPresentation getPresentation() {
        return createPresentation(element);
    }

    protected final void addChild(final GLSLStructureViewTreeElement<?> child) {
        children.add(child);
    }

    protected abstract void createChildren(@NotNull T t);

    @NotNull
    public final TreeElement[] getChildren() {
        children.clear();
        createChildren(element);
        Collections.sort(children);
        return children.toArray(new TreeElement[0]);
    }

    public void navigate(boolean requestFocus) {
        if (element instanceof NavigatablePsiElement) {
            ((NavigatablePsiElement) element).navigate(requestFocus);
        }
    }

    public boolean canNavigate() {
        return element instanceof NavigatablePsiElement && ((NavigatablePsiElement) element).canNavigate();
    }

    public boolean canNavigateToSource() {
        return element instanceof NavigatablePsiElement && ((NavigatablePsiElement) element).canNavigateToSource();
    }

    /** Order in which tree elements should appear.
     * 0 = top, inf = bottom */
    protected abstract int visualTreeOrder();

    protected static final int VISUAL_TREE_ORDER_FILE = 0;
    protected static final int VISUAL_TREE_ORDER_STRUCT = 1;
    protected static final int VISUAL_TREE_ORDER_DECLARATOR = 2;
    protected static final int VISUAL_TREE_ORDER_FUNCTION = 3;

    @Override
    public int compareTo(@NotNull GLSLStructureViewTreeElement<?> o) {
        return Integer.compare(visualTreeOrder(), o.visualTreeOrder());
    }
}
