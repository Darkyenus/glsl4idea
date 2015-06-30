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

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

/**
 * Scalar type is a scalar type. Not much to say about that.
 *
 * @author Yngve Devik Hammersland
 * @author Jan Polák
 */
public class GLSLScalarType extends GLSLType {

    //region Static
    public static final GLSLScalarType BOOL = new GLSLScalarType("bool", Boolean.class);
    public static final GLSLScalarType DOUBLE = new GLSLScalarType("double", Double.class);
    public static final GLSLScalarType FLOAT = new GLSLScalarType("float", Double.class, DOUBLE);
    public static final GLSLScalarType UINT = new GLSLScalarType("uint", Long.class, FLOAT, DOUBLE);
    public static final GLSLScalarType INT = new GLSLScalarType("int", Long.class, UINT, FLOAT, DOUBLE);

    public static boolean isIntegerScalar(GLSLType type){
        return type == INT || type == UINT;
    }
    //endregion

    private final String typename;
    private final Collection<GLSLType> implicitConversions;

    private GLSLScalarType(String typename, Class<?> javaType, GLSLType... implicitlyConvertibleTo) {
        super(javaType);
        this.typename = typename;
        this.implicitConversions = Arrays.asList(implicitlyConvertibleTo);
    }

    @NotNull
    public String getTypename() {
        return typename;
    }

    public boolean typeEquals(GLSLType otherType) {
        return this == otherType;
    }

    public boolean isConvertibleTo(GLSLType otherType) {
        return otherType.isValidType() &&
                (typeEquals(otherType) || implicitConversions.contains(otherType));
    }

    @Override
    public boolean hasMembers() {
        //Scalars do have members like vectors, but no .length() and no indexing
        return true;
    }

    @Override
    public boolean hasMember(String member) {
        //Only members of scalars are first vector components
        //Scalars are basically one-component-vectors in this regard
        return member.length() == 1 && "xrs".indexOf(member.charAt(0)) != -1;
    }

    @NotNull
    @Override
    public GLSLType getMemberType(String member) {
        if(hasMember(member)){
            return this;
        }else{
            return GLSLTypes.UNKNOWN_TYPE;
        }
    }

    @Override
    public String toString() {
        return "Primitive Type: " + getTypename();
    }
}
