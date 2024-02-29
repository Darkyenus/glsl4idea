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

import com.google.common.base.Strings;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.declarations.GLSLInterfaceBlockDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class GLSLInterfaceBlockType extends GLSLType {

    @NotNull
    private final GLSLInterfaceBlockDefinition definition;
    private String typename = "<not-initialized>";
    private final Map<String, GLSLType> members = new HashMap<>();
    private String[] memberNames;

    public GLSLInterfaceBlockType(@NotNull GLSLInterfaceBlockDefinition definition) {
        super(null);
        this.definition = definition;
        updateNameAndMembers();
    }

    /**
     * Updates internal members containers from the struct type definition.
     */
    public void updateNameAndMembers() {
        final String definitionName = definition.getInterfaceBlockName();
        typename = !Strings.isNullOrEmpty(definitionName) ? definitionName  : "(anonymous " + System.identityHashCode(this) + ")";

        final GLSLDeclarator[] declarators = definition.getDeclarators();

        memberNames = new String[declarators.length];

        members.clear();
        for (int i = 0; i < declarators.length; i++) {
            final GLSLDeclarator declarator = declarators[i];
            final String memberName = declarator.getVariableName();
            final GLSLType memberType = declarator.getType();
            members.put(memberName, memberType);
            memberNames[i] = memberName;
        }
    }

    @NotNull
    public String getTypename() {
        return typename;
    }

    /**
     * Get the place where the struct was defined.
     * Note that GLSL does not support forward struct declarations (since it does not have pointers).
     */
    @Override
    public @NotNull GLSLInterfaceBlockDefinition getDefinition() {
        return definition;
    }

    @Override
    public boolean typeEquals(GLSLType otherType) {
        if (otherType instanceof GLSLInterfaceBlockType other) {

            // typename should be enough as only a single struct can have any given name
            // TODO: Check definition reference instead? NOTE: It may be null
            return getTypename().equals(other.typename);
        }

        return false;
    }

    public String[] getMemberNames() {
        return memberNames;
    }

    @NotNull
    public Map<String, GLSLType> getMembers() {
        return members;
    }

    @Override
    public boolean hasMember(String member) {
        return members.containsKey(member);
    }
}
