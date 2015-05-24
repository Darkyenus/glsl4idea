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

package glslplugin.lang.elements.types;

import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.declarations.GLSLTypeDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * GLSLStructType is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 26, 2009
 *         Time: 11:20:35 AM
 */
public class GLSLStructType extends GLSLType {

    private GLSLTypeDefinition definition;
    private String typename;
    private Map<String, GLSLType> members = null;
    private GLSLType[] memberTypes = null;

    public GLSLStructType(GLSLTypeDefinition definition) {
        this.definition = definition;
        final GLSLDeclarator[] declarators = definition.getDeclarators();

        typename = definition.getTypeName();

        members = new HashMap<String, GLSLType>();
        memberTypes = new GLSLType[declarators.length];

        for (int i = 0; i < declarators.length; i++) {
            final GLSLDeclarator declarator = declarators[i];
            members.put(declarator.getIdentifierName(), declarator.getType());
            memberTypes[i] = declarator.getType();
        }
    }

    @NotNull
    public String getTypename() {
        return typename;
    }

    @Override
    public GLSLTypeDefinition getDefinition() {
        return definition;
    }

    @Override
    public boolean typeEquals(GLSLType otherType) {
        if (otherType instanceof GLSLStructType) {
            GLSLStructType other = (GLSLStructType) otherType;

            // typename should be enough as only a single struct can have any given name
            // TODO: Check definition reference instead? NOTE: It may be null
            if (getTypename().equals(other.typename)) {
                return true;
            }
        }
        return false;

    }

    @NotNull
    @Override
    public Map<String, GLSLType> getMembers() {
        return members;
    }

    @NotNull
    @Override
    public GLSLType[] getMemberTypes() {
        return memberTypes;
    }

    @Override
    public boolean hasMembers() {
        return true;
    }

    @NotNull
    @Override
    public GLSLFunctionType[] getConstructors() {
        return new GLSLFunctionType[]{
                new GLSLBasicFunctionType(getTypename(), this, getMemberTypes())
        };
    }


}
