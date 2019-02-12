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

import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLIdentifier;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.declarations.GLSLTypeDefinition;
import glslplugin.lang.elements.expressions.GLSLExpression;
import glslplugin.lang.elements.expressions.GLSLFieldSelectionExpression;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.Nullable;

/**
 * GLSLFieldReference is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Mar 1, 2009
 *         Time: 11:07:52 PM
 */
public class GLSLFieldReference extends GLSLReferenceBase<GLSLIdentifier, GLSLDeclarator> {
    private final GLSLFieldSelectionExpression sourceExpression;

    public GLSLFieldReference(GLSLFieldSelectionExpression source) {
        super(source.getMemberIdentifier());
        sourceExpression = source;
    }

    @Override
    @Nullable
    public GLSLDeclarator resolve() {
        GLSLExpression left = sourceExpression.getLeftHandExpression();
        if(left == null)return null;
        GLSLType type = left.getType();
        if (type == GLSLTypes.UNKNOWN_TYPE) {
            return null;
        }
        GLSLElement definition = type.getDefinition();
        if (definition instanceof GLSLTypeDefinition) {
            GLSLIdentifier memberIdentifier = source;
            if(memberIdentifier == null){
                return null;
            }else{
                return ((GLSLTypeDefinition) definition).getDeclarator(memberIdentifier.getName());
            }
        } else {
            return null;
        }
    }
}
