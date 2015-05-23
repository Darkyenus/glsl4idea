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

import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.declarations.GLSLArraySpecifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * NewType is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 6, 2009
 *         Time: 10:37:29 PM
 */
public abstract class GLSLType {
    public static final GLSLType[] EMPTY_ARRAY = {};

    private static final Map<String, GLSLFunctionType> EMPTY_METHOD_MAP = new HashMap<String, GLSLFunctionType>();
    private static final Map<String, GLSLType> EMPTY_MEMBER_MAP = new HashMap<String, GLSLType>();

    /**
     * @return the text representation of this type.
     */
    @NotNull
    public abstract String getTypename();

    /**
     * Fetches the array specifier of this type.
     *
     * @return the array specifier, null if it is not present.
     */
    @Nullable
    public GLSLArraySpecifier getArraySpecifier() {
        return null;
    }

    /**
     * @return Indicates whether this type is indexable, i.e. can be subscripted.
     */
    public boolean isIndexable() {
        return false;
    }

    /**
     * Return the base type of this type.
     * For example if this is an array (int[]), it will return (int)
     * This applies to all subscriptable types: arrays, vectors and matrices.
     *
     * @return the type this type is based on, null if this is a primitive type or a struct.
     */
    @NotNull
    public GLSLType getBaseType() {
        return GLSLTypes.INVALID_TYPE;
    }


    /**
     * Fetches the PsiElement containing the definition of this type.
     * Applies only to structures.
     *
     * @return the definition of this type.
     */
    @Nullable
    public GLSLElement getDefinition() {
        return null;
    }

    /**
     * Determines whether two types are equal.
     * Note: named struct definitions and references to that name are considered the same type.
     *
     * @param otherType another type.
     * @return true if equal, false otherwise.
     */
    public boolean typeEquals(GLSLType otherType) {
        return this == otherType;
    }

    /**
     * Checks whether this can be implicitly converted to.
     * Per specification, this only holds for int -&gt; float.
     *
     * @param otherType another type.
     * @return true if the conversion is possible, false otherwise.
     */
    public boolean isConvertibleTo(GLSLType otherType) {
        if (!otherType.isValidType()) return true;
        return typeEquals(otherType);
    }

    /**
     * @return a set containing all the names of the members of this type.
     */
    @NotNull
    public Set<String> getMemberNames() {
        return getMembers().keySet();
    }

    @NotNull
    public Map<String, GLSLType> getMembers() {
        return EMPTY_MEMBER_MAP;
    }

    @NotNull
    public GLSLType[] getMemberTypes() {
        return GLSLType.EMPTY_ARRAY;
    }

    /**
     * Indicates whether this type has a member of the specified name.
     *
     * @param name the name of the member.
     * @return true if the member exist, false otherwise.
     */
    public boolean hasMember(String name) {
        return getMemberNames().contains(name);
    }

    /**
     * Fetches the type of the member of the specified name.
     *
     * @param name the name to get the type of.
     * @return the type if found, {@link GLSLTypes#INVALID_TYPE} otherwise.
     */
    @NotNull
    public GLSLType getTypeOfMember(String name) {
        if (getMembers().containsKey(name)) {
            return getMembers().get(name);
        } else {
            return GLSLTypes.INVALID_TYPE;
        }
    }

    /**
     * Indicates whether this type has members.
     *
     * @return true for struct, vector and undefined types, false otherwise.
     */
    public boolean hasMembers() {
        return false;
    }

    /**
     * Retrieves the type constructors of this type.
     *
     * @return an array containing all the constructors of this type.
     */
    @NotNull
    public GLSLFunctionType[] getConstructors() {
        // All types has a type constructor which takes itself as an argument.
        return new GLSLFunctionType[]{new GLSLBasicFunctionType(getTypename(), this, this)};
    }

    /**
     * Retrieves the methods associated with this type.
     *
     * @return an array containing all the methods of this
     */
    @NotNull
    public Map<String, GLSLFunctionType> getMethods() {
        return EMPTY_METHOD_MAP;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + getTypename();
    }

    /**
     * Indicates whether this type is a valid type.
     *
     * @return true for all types except UNKNOWN_TYPE and INVALID_TYPE.
     */
    public boolean isValidType() {
        return true;
    }
}
