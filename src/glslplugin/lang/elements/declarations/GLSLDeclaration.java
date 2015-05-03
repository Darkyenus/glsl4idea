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

package glslplugin.lang.elements.declarations;

import glslplugin.lang.elements.GLSLElement;
import org.jetbrains.annotations.NotNull;

/**
 * GLSLDeclaration is a common interface for all declarations;
 * variable declarations, function declarations/definitions, and struct member declarations.
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 2, 2009
 *         Time: 12:39:33 AM
 */
public interface GLSLDeclaration extends GLSLElement {
    GLSLTypeSpecifier getTypeSpecifierNode();

    GLSLQualifierList getQualifierList();

    @NotNull
    GLSLDeclarator[] getDeclarators();
}
