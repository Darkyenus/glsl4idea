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
import java.util.HashMap;

/**
 * GLSLVectorType is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 26, 2009
 *         Time: 11:48:00 AM
 */
public class GLSLMatrixType extends GLSLType {
    private class Constructor extends GLSLFunctionType {
        protected Constructor() {
            super(GLSLMatrixType.this.getTypename(), GLSLMatrixType.this);
        }

        protected String generateTypename() {
            return "(...) : " + GLSLMatrixType.this.getTypename();
        }

        @NotNull
        public GLSLTypeCompatibilityLevel getParameterCompatibilityLevel(@NotNull GLSLType[] types) {
            // Special constructor for vectors.
            // See GLSL specification 5.4.2 for details.
            if (types.length == 0) {
                return GLSLTypeCompatibilityLevel.INCOMPATIBLE;
            }
            if (types.length == 1) {
                if (GLSLTypes.isScalar(types[0])) {
                    return GLSLTypeCompatibilityLevel.DIRECTLY_COMPATIBLE;
                } else if (types[0] instanceof GLSLVectorType) {
                    return GLSLTypeCompatibilityLevel.DIRECTLY_COMPATIBLE;
                } else {
                    return GLSLTypeCompatibilityLevel.INCOMPATIBLE;
                }
            } else {
                int numComponents = 0;
                for (GLSLType type : types) {
                    if (GLSLTypes.isScalar(type)) {
                        numComponents++;
                    } else if (type instanceof GLSLVectorType) {
                        numComponents += ((GLSLVectorType) type).getNumComponents();
                    } else if (type instanceof GLSLMatrixType) {
                        numComponents += ((GLSLMatrixType) type).getNumComponents();
                    } else {
                        return GLSLTypeCompatibilityLevel.INCOMPATIBLE;
                    }
                }
                if (numComponents == getNumComponents()) {
                    return GLSLTypeCompatibilityLevel.DIRECTLY_COMPATIBLE;
                } else {
                    return GLSLTypeCompatibilityLevel.INCOMPATIBLE;
                }
            }
        }
    }

    private static final HashMap<GLSLType, String> PREFIXES = new HashMap<GLSLType, String>() {{
        put(GLSLTypes.FLOAT, "mat");
        put(GLSLTypes.DOUBLE, "dmat");
    }};

    private static final HashMap<String, GLSLMatrixType> matrixTypes = new HashMap<String, GLSLMatrixType>() {{
        for (int x = 2; x <= 4; x++) {
            for (int y = 2; y <= 4; y++) {
                for (GLSLType fundamentalType : Arrays.asList(GLSLTypes.FLOAT, GLSLTypes.DOUBLE)) {
                    GLSLMatrixType matrixType = new GLSLMatrixType(x, y, fundamentalType);
                    put(matrixType.getTypename(), matrixType);
                    if (y == x) {
                        String prefix = PREFIXES.get(fundamentalType);
                        if (prefix == null) prefix = "mat";
                        put(prefix + x, matrixType);
                    }
                }
            }
        }
    }};

    public static final GLSLMatrixType MAT2X2 = getTypeFromName("mat2x2");
    public static final GLSLMatrixType MAT3X2 = getTypeFromName("mat3x2");
    public static final GLSLMatrixType MAT4X2 = getTypeFromName("mat4x2");
    public static final GLSLMatrixType MAT2X3 = getTypeFromName("mat2x3");
    public static final GLSLMatrixType MAT3X3 = getTypeFromName("mat3x3");
    public static final GLSLMatrixType MAT4X3 = getTypeFromName("mat4x3");
    public static final GLSLMatrixType MAT2X4 = getTypeFromName("mat2x4");
    public static final GLSLMatrixType MAT3X4 = getTypeFromName("mat3x4");
    public static final GLSLMatrixType MAT4X4 = getTypeFromName("mat4x4");
    public static final GLSLMatrixType DMAT2X2 = getTypeFromName("dmat2x2");
    public static final GLSLMatrixType DMAT3X2 = getTypeFromName("dmat3x2");
    public static final GLSLMatrixType DMAT4X2 = getTypeFromName("dmat4x2");
    public static final GLSLMatrixType DMAT2X3 = getTypeFromName("dmat2x3");
    public static final GLSLMatrixType DMAT3X3 = getTypeFromName("dmat3x3");
    public static final GLSLMatrixType DMAT4X3 = getTypeFromName("dmat4x3");
    public static final GLSLMatrixType DMAT2X4 = getTypeFromName("dmat2x4");
    public static final GLSLMatrixType DMAT3X4 = getTypeFromName("dmat3x4");
    public static final GLSLMatrixType DMAT4X4 = getTypeFromName("dmat4x4");

    public static GLSLMatrixType getTypeFromName(String name) {
        GLSLMatrixType type = matrixTypes.get(name);
        assert type != null : "Unknown matrix name: '" + name + "'.";
        return type;
    }

    public static GLSLMatrixType getType(int cols, int rows, GLSLType fundamentalType) {
        return getTypeFromName("mat" + cols + "x" + rows);
    }

    private int numColumns, numRows;
    private GLSLType fundamentalType;

    private GLSLMatrixType(int numColumns, int numRows, GLSLType fundamentalType) {
        //NOTE: do not fill the member map here as it needs to refer the other vector types.
        this.numColumns = numColumns;
        this.numRows = numRows;
        this.fundamentalType = fundamentalType;
    }

    @Override
    public String getTypename() {
        String prefix = PREFIXES.get(fundamentalType);
        if (prefix == null) prefix = "mat";
        return prefix + numColumns + "x" + numRows;
    }

    @Override
    public GLSLType getBaseType() {
        return GLSLVectorType.getType(numColumns, GLSLPrimitiveType.FLOAT);
    }

    public int getNumComponents() {
        return numColumns * numRows;
    }

    @NotNull
    @Override
    public GLSLFunctionType[] getConstructors() {
        return new GLSLFunctionType[]{
                new Constructor()
        };
    }

    @Override
    public boolean isConvertibleTo(GLSLType otherType) {
        if (!(otherType instanceof GLSLMatrixType)) return false;
        GLSLMatrixType other = (GLSLMatrixType) otherType;

        return numRows == other.numRows
                && numColumns == other.numColumns
                && fundamentalType.isConvertibleTo(other.fundamentalType);
    }
}
