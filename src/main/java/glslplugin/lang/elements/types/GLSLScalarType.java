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

import glslplugin.lang.elements.types.constructors.GLSLBasicConstructorType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Scalar type is a type that has only magnitude, no members or elements.
 * NOTE: Although practically, they have one member - themselves.
 *
 * @author Yngve Devik Hammersland
 * @author Jan Polák
 */
public class GLSLScalarType extends GLSLType {

    //region Static
    public static final GLSLScalarType BOOL = new GLSLScalarType("bool", Boolean.class);
    public static final GLSLScalarType DOUBLE = new GLSLScalarType("double", Double.class);
    public static final GLSLScalarType FLOAT = new GLSLScalarType("float", Double.class);
    public static final GLSLScalarType UINT = new GLSLScalarType("uint", Long.class);
    public static final GLSLScalarType INT = new GLSLScalarType("int", Long.class);
    // https://www.khronos.org/registry/OpenGL/extensions/ARB/ARB_gpu_shader_int64.txt
    public static final GLSLScalarType UINT64 = new GLSLScalarType("uint64_t", Long.class);
    public static final GLSLScalarType INT64 = new GLSLScalarType("int64_t", Long.class);

    static {
        INT.implicitConversions = Arrays.asList(UINT, INT64, UINT64, FLOAT, DOUBLE);
        UINT.implicitConversions = Arrays.asList(UINT64, FLOAT, DOUBLE);
        INT64.implicitConversions = Arrays.asList(UINT64, DOUBLE);
        UINT64.implicitConversions = List.of(DOUBLE);
        FLOAT.implicitConversions = List.of(DOUBLE);
    }

    public static final GLSLScalarType[] SCALARS = {BOOL, DOUBLE, FLOAT, UINT, INT, UINT64, INT64};

    public static boolean isIntegerScalar(GLSLType type){
        return type == INT || type == UINT || type == INT64 || type == UINT64;
    }
    //endregion

    private final String typename;
    private List<GLSLType> implicitConversions;

    private GLSLScalarType(String typename, Class<?> javaType) {
        super(javaType);
        this.typename = typename;
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
    public boolean hasMember(String member) {
        //Only members of scalars are first vector components
        //Scalars are basically one-component-vectors in this regard
        return member.length() == 1 && "xrs".indexOf(member.charAt(0)) != -1;
    }

    private GLSLFunctionType[] constructorsCache;

    /**
     * Each scalar type is explicitly convertible to any other scalar type.
     * Scalar constructors with non-scalar parameters can be used to take the first element from a non-scalar.
     * For example, the constructor float(vec3) will select the first component of the vec3 parameter.
     * See 5.4.1 of GLSL Spec. 4.50
     */
    @NotNull
    @Override
    public GLSLFunctionType[] getConstructors() {
        if (constructorsCache != null) {
            return constructorsCache;
        }
        final ArrayList<GLSLFunctionType> constructors = new ArrayList<>();
        for (GLSLScalarType scalar : SCALARS) {
            constructors.add(new GLSLBasicConstructorType(null, this, scalar));
        }
        for (GLSLVectorType[] vectors : GLSLVectorType.VECTOR_TYPES.values()) {
            for (GLSLVectorType vector : vectors) {
                constructors.add(new GLSLBasicConstructorType(null, this, vector));
            }
        }
        for (GLSLMatrixType[][] matrices : GLSLMatrixType.MATRIX_TYPES.values()) {
            for (GLSLMatrixType[] matricesRow : matrices) {
                for (GLSLMatrixType matrix : matricesRow) {
                    constructors.add(new GLSLBasicConstructorType(null, this, matrix));
                }
            }
        }
        return constructorsCache = constructors.toArray(GLSLFunctionType.EMPTY_ARRAY);
    }

    @Override
    public String toString() {
        return "Primitive Type: " + getTypename();
    }
}
