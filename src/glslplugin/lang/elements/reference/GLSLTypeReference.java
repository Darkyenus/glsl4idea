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

package glslplugin.lang.elements.reference;

import glslplugin.lang.elements.declarations.GLSLTypeDefinition;
import glslplugin.lang.elements.declarations.GLSLTypename;

/**
 * GLSLVariableReference is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 4, 2009
 *         Time: 1:29:50 AM
 */
public class GLSLTypeReference extends GLSLReferenceBase<GLSLTypename, GLSLTypeDefinition> {
    public GLSLTypeReference(GLSLTypename source, GLSLTypeDefinition target) {
        super(source, target);
    }
}