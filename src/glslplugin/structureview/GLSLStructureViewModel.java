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
import com.intellij.ide.structureView.TextEditorBasedStructureViewModel;
import com.intellij.ide.util.treeView.smartTree.Filter;
import com.intellij.ide.util.treeView.smartTree.Grouper;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import glslplugin.lang.elements.declarations.GLSLDeclarationImpl;
import glslplugin.lang.elements.declarations.GLSLFunctionDeclarationImpl;
import glslplugin.lang.elements.declarations.GLSLTypeDefinition;
import org.jetbrains.annotations.NotNull;

class GLSLStructureViewModel extends TextEditorBasedStructureViewModel {
    private PsiElement rootElement;

    public GLSLStructureViewModel(PsiElement element) {
        super(element.getContainingFile());
        this.rootElement = element;
    }

    protected PsiFile getPsiFile() {
        return rootElement.getContainingFile();
    }

    @NotNull
    public StructureViewTreeElement getRoot() {
        return new GLSLFileTreeElement(rootElement.getContainingFile());
    }

    @NotNull
    public Grouper[] getGroupers() {
        return Grouper.EMPTY_ARRAY;
    }

    @NotNull
    public Sorter[] getSorters() {
        return new Sorter[]{Sorter.ALPHA_SORTER};
    }

    @NotNull
    public Filter[] getFilters() {
        return Filter.EMPTY_ARRAY;
    }


    @NotNull
    @Override
    protected Class[] getSuitableClasses() {
        return new Class[]{GLSLFunctionDeclarationImpl.class, PsiFile.class, GLSLTypeDefinition.class, GLSLDeclarationImpl.class};
    }
}
