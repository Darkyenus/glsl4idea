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

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import glslplugin.lang.elements.declarations.*;
import org.jetbrains.annotations.NotNull;

class GLSLFileTreeElement extends GLSLStructureViewTreeElement<PsiFile> {

    public GLSLFileTreeElement(PsiFile file) {
        super(file);
    }

    protected GLSLPresentation createPresentation(@NotNull PsiFile file) {
        return GLSLPresentation.createFilePresentation(file.getName());
    }

    protected void createChildren(@NotNull PsiFile file) {
        PsiElement[] baseNodes = file.getChildren();
        for (PsiElement baseNode : baseNodes) {
            if (baseNode instanceof GLSLVariableDeclaration) {
                final GLSLVariableDeclaration declaration = (GLSLVariableDeclaration) baseNode;
                final GLSLTypeSpecifier typeSpecifier = declaration.getTypeSpecifierNode();

                if (typeSpecifier != null) {
                    final GLSLStructDefinition typedef = typeSpecifier.getEmbeddedStructDefinition();
                    if (typedef != null) {
                        addChild(new GLSLStructTreeElement(typedef));
                    }
                }

                for (GLSLDeclarator declarator : declaration.getDeclarators()) {
                    addChild(new GLSLDeclaratorTreeElement(declarator));
                }
            } else if (baseNode instanceof GLSLFunctionDeclaration) {
                addChild(new GLSLFunctionTreeElement((GLSLFunctionDeclaration) baseNode));
            }
        }
    }

    @Override
    protected int visualTreeOrder() {
        return VISUAL_TREE_ORDER_FILE;
    }
}
