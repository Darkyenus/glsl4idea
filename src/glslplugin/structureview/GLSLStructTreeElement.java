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

import glslplugin.lang.elements.declarations.GLSLDeclaration;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.declarations.GLSLStructDefinition;
import org.jetbrains.annotations.NotNull;

public class GLSLStructTreeElement extends GLSLStructureViewTreeElement<GLSLStructDefinition> {
    public GLSLStructTreeElement(GLSLStructDefinition definition) {
        super(definition);
    }

    protected GLSLPresentation createPresentation(@NotNull GLSLStructDefinition definition) {
        return GLSLPresentation.createStructPresentation(definition.getName());
    }

    protected void createChildren(@NotNull GLSLStructDefinition definition) {
        GLSLDeclaration[] glslDeclarations = definition.getDeclarations();
        for (GLSLDeclaration declaration : glslDeclarations) {
            for (GLSLDeclarator declarator : declaration.getDeclarators()) {
                addChild(new GLSLDeclaratorTreeElement(declarator));
            }
        }
    }

    @Override
    protected int visualTreeOrder() {
        return VISUAL_TREE_ORDER_STRUCT;
    }
}
