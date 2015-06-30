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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

/**
 * NewType is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 6, 2009
 *         Time: 10:37:29 PM
 */
public abstract class GLSLType {
    public static final GLSLType[] EMPTY_ARRAY = {};

    private final Class<?> javaType;

    protected GLSLType(@Nullable Class<?> javaType) {
        this.javaType = javaType;
    }

    /**
     * @return the text representation of this type.
     */
    @NotNull
    public abstract String getTypename();

    /**
     * Fetches the PsiElement containing the definition of this type.
     * Applies only to user-defined types (structures).
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
        return !otherType.isValidType() || typeEquals(otherType);
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
     * Indicates whether this type is a valid type.
     *
     * @return true for all types except UNKNOWN_TYPE
     */
    public boolean isValidType() {
        return true;
    }

    /**
     * Java type used for this GLSL type during constant expression analysis.
     * Can be null if this type does not support that.
     */
    @Nullable
    public Class<?> getJavaType(){
        return javaType;
    }

    //region Array-like related

    /**
     * @return Indicates whether this type is indexable, i.e. can be subscripted ("[i]").
     */
    public boolean isIndexable() {
        return false;
    }

    /**
     * Return the base type of this type.
     * That is, the type obtained by indexing.
     * For example if this is an array (int[]), it will return (int)
     * This applies to all indexable types: arrays, vectors and matrices.
     *
     * @see #getBaseType()
     * @return the type obtained by indexing, this type if this is a primitive type or a struct.
     */
    @NotNull
    public GLSLType getIndexType() {
        return this;
    }

    /**
     * Return the fundamental type of this type.
     * This is usually same as {@link #getIndexType()}, but can be different for multidimensional-like types,
     * such as matrices. Index type of matrix is a vector, but base type of matrix is a scalar.
     *
     * @return the type this type is based on and fundamentally contains
     */
    @NotNull
    public GLSLType getBaseType(){
        return getIndexType();
    }

    //endregion

    //region Struct-like related

    /**
     * Check whether or not this type has given member variable.
     * Only for those will getMemberType call be meaningful.
     */
    public boolean hasMember(String member){
        return false;
    }

    /**
     * Get the type type of given member variable.
     *
     * @param member the name of variable to get the type of
     * @return the type if found, {@link GLSLTypes#UNKNOWN_TYPE} otherwise
     */
    @NotNull
    public GLSLType getMemberType(String member){
        return GLSLTypes.UNKNOWN_TYPE;
    }

    /**
     * Indicates whether this type has any members.
     *
     * @return true for struct, vector and undefined types, false otherwise.
     */
    public boolean hasMembers() {
        return false;
    }

    //endregion

    //region Object-like related

    private static final Map<String, GLSLFunctionType> NO_FUNCTIONS = Collections.emptyMap();

    /**
     * Returns convenient Map containing all member functions of this type, with their name as key.
     *
     * As of GLSL version 430, that is only .length() function on arrays, vectors and matrices.
     */
    @NotNull
    public Map<String, GLSLFunctionType> getMemberFunctions(){
        return NO_FUNCTIONS;
    }

    //endregion

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + getTypename();
    }

}
