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

import java.util.HashMap;
import java.util.Map;

/**
 * GLGLBuiltInTypes holds all the built-in types for easy access.
 * These types are not constructible by other classes so reference comparison can be used.
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 26, 2009
 *         Time: 4:00:42 PM
 */
public class GLSLTypes {
    // Scalars
    public static final GLSLPrimitiveType INT = register(GLSLPrimitiveType.INT);
    public static final GLSLPrimitiveType UINT = register(GLSLPrimitiveType.UINT);
    public static final GLSLPrimitiveType FLOAT = register(GLSLPrimitiveType.FLOAT);
    public static final GLSLPrimitiveType DOUBLE = register(GLSLPrimitiveType.DOUBLE);
    public static final GLSLPrimitiveType BOOL = register(GLSLPrimitiveType.BOOL);

    // Vectors
    public static final GLSLVectorType VEC2 =  register(GLSLVectorType.getType(FLOAT,  2));
    public static final GLSLVectorType VEC3 =  register(GLSLVectorType.getType(FLOAT,  3));
    public static final GLSLVectorType VEC4 =  register(GLSLVectorType.getType(FLOAT,  4));
    public static final GLSLVectorType DVEC2 = register(GLSLVectorType.getType(DOUBLE, 2));
    public static final GLSLVectorType DVEC3 = register(GLSLVectorType.getType(DOUBLE, 3));
    public static final GLSLVectorType DVEC4 = register(GLSLVectorType.getType(DOUBLE, 4));
    public static final GLSLVectorType IVEC2 = register(GLSLVectorType.getType(INT,    2));
    public static final GLSLVectorType IVEC3 = register(GLSLVectorType.getType(INT,    3));
    public static final GLSLVectorType IVEC4 = register(GLSLVectorType.getType(INT,    4));
    public static final GLSLVectorType UVEC2 = register(GLSLVectorType.getType(UINT,   2));
    public static final GLSLVectorType UVEC3 = register(GLSLVectorType.getType(UINT,   3));
    public static final GLSLVectorType UVEC4 = register(GLSLVectorType.getType(UINT,   4));
    public static final GLSLVectorType BVEC2 = register(GLSLVectorType.getType(BOOL,   2));
    public static final GLSLVectorType BVEC3 = register(GLSLVectorType.getType(BOOL,   3));
    public static final GLSLVectorType BVEC4 = register(GLSLVectorType.getType(BOOL,   4));

    // Matrices
    public static final GLSLMatrixType MAT2x2 =  register(GLSLMatrixType.getType(FLOAT,  2, 2));
    public static final GLSLMatrixType MAT2x3 =  register(GLSLMatrixType.getType(FLOAT,  2, 3));
    public static final GLSLMatrixType MAT2x4 =  register(GLSLMatrixType.getType(FLOAT,  2, 4));
    public static final GLSLMatrixType MAT3x2 =  register(GLSLMatrixType.getType(FLOAT,  3, 2));
    public static final GLSLMatrixType MAT3x3 =  register(GLSLMatrixType.getType(FLOAT,  3, 3));
    public static final GLSLMatrixType MAT3x4 =  register(GLSLMatrixType.getType(FLOAT,  3, 4));
    public static final GLSLMatrixType MAT4x2 =  register(GLSLMatrixType.getType(FLOAT,  4, 2));
    public static final GLSLMatrixType MAT4x3 =  register(GLSLMatrixType.getType(FLOAT,  4, 3));
    public static final GLSLMatrixType MAT4x4 =  register(GLSLMatrixType.getType(FLOAT,  4, 4));
    public static final GLSLMatrixType DMAT2x2 = register(GLSLMatrixType.getType(DOUBLE, 2, 2));
    public static final GLSLMatrixType DMAT2x3 = register(GLSLMatrixType.getType(DOUBLE, 2, 3));
    public static final GLSLMatrixType DMAT2x4 = register(GLSLMatrixType.getType(DOUBLE, 2, 4));
    public static final GLSLMatrixType DMAT3x2 = register(GLSLMatrixType.getType(DOUBLE, 3, 2));
    public static final GLSLMatrixType DMAT3x3 = register(GLSLMatrixType.getType(DOUBLE, 3, 3));
    public static final GLSLMatrixType DMAT3x4 = register(GLSLMatrixType.getType(DOUBLE, 3, 4));
    public static final GLSLMatrixType DMAT4x2 = register(GLSLMatrixType.getType(DOUBLE, 4, 2));
    public static final GLSLMatrixType DMAT4x3 = register(GLSLMatrixType.getType(DOUBLE, 4, 3));
    public static final GLSLMatrixType DMAT4x4 = register(GLSLMatrixType.getType(DOUBLE, 4, 4));

    // For convenience
    public static final GLSLMatrixType MAT2 = MAT2x2;
    public static final GLSLMatrixType MAT3 = MAT3x3;
    public static final GLSLMatrixType MAT4 = MAT4x4;
    public static final GLSLMatrixType DMAT2 = DMAT2x2;
    public static final GLSLMatrixType DMAT3 = DMAT3x3;
    public static final GLSLMatrixType DMAT4 = DMAT4x4;

    // Samplers
    public static final GLSLPrimitiveType SAMPLER1D = register(GLSLPrimitiveType.SAMPLER1D);
    public static final GLSLPrimitiveType SAMPLER2D = register(GLSLPrimitiveType.SAMPLER2D);
    public static final GLSLPrimitiveType SAMPLER3D = register(GLSLPrimitiveType.SAMPLER3D);
    public static final GLSLPrimitiveType SAMPLER1D_SHADOW = register(GLSLPrimitiveType.SAMPLER1D_SHADOW);
    public static final GLSLPrimitiveType SAMPLER2D_SHADOW = register(GLSLPrimitiveType.SAMPLER2D_SHADOW);
    public static final GLSLPrimitiveType SAMPLER_CUBE = register(GLSLPrimitiveType.SAMPLER_CUBE);

    // Specials
    public static final GLSLPrimitiveType VOID = GLSLPrimitiveType.VOID;

    public static final GLSLType UNKNOWN_TYPE = new GLSLType() {
        @NotNull
        public String getTypename() {
            return "(unknown type)";
        }

        @Override
        public boolean typeEquals(GLSLType otherType) {
            return false;
        }

        @Override
        public boolean isConvertibleTo(GLSLType otherType) {
            return true;
        }

        @Override
        public boolean isValidType() {
            return false;
        }

        @Override
        public boolean isIndexable() {
            return true;
        }

        @Override
        public boolean hasMembers() {
            return true;
        }
    };

    private static final Map<String, GLSLType> undefinedTypes = new HashMap<String, GLSLType>();

    public static GLSLType getUndefinedType(final String text) {
        if (!undefinedTypes.containsKey(text)) {
            undefinedTypes.put(text,
                    new GLSLType() {
                        private final String name = text;

                        @NotNull
                        public String getTypename() {
                            return name;
                        }

                        @Override
                        public boolean hasMembers() {
                            return true;
                        }
                    });
        }
        return undefinedTypes.get(text);
    }

    @NotNull
    public static GLSLType unifyTypes(GLSLType t1, GLSLType t2) {
        if (t1 == UNKNOWN_TYPE || t2 == UNKNOWN_TYPE) return UNKNOWN_TYPE;
        if (t1.isConvertibleTo(t2)) return t2;
        if (t2.isConvertibleTo(t1)) return t1;
        return UNKNOWN_TYPE;
    }

    static boolean isScalar(GLSLType type) {
        return type == INT || type == FLOAT || type == BOOL || type == DOUBLE || type == UINT;
    }

    private static Map<String, GLSLType> types;

    private static <TYPE extends GLSLType> TYPE register(TYPE type) {
        if (types == null) types = new HashMap<String, GLSLType>();
        types.put(type.getTypename(), type);
        return type;
    }

    public static GLSLType getTypeFromName(String name) {
        return types.get(name);
    }
}
