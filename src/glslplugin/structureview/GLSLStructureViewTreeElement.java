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
import java.util.List;

abstract class GLSLStructureViewTreeElement<T extends PsiElement> implements StructureViewTreeElement {
    private T element;
    private List<StructureViewTreeElement> children = new ArrayList<StructureViewTreeElement>();

    public GLSLStructureViewTreeElement(T element) {
        this.element = element;
    }

    protected abstract void createChildren(T t);

    protected void addChild(GLSLStructureViewTreeElement child) {
        children.add(child);
    }

    protected abstract GLSLPresentation createPresentation(T element);

    public T getValue() {
        return element;
    }

    @NotNull
    public ItemPresentation getPresentation() {
        return createPresentation(element);
    }

    @NotNull
    public TreeElement[] getChildren() {
        children.clear();
        createChildren(element);
        return children.toArray(new TreeElement[children.size()]);
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
}
