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
 * GLSLVectorType is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 26, 2009
 *         Time: 11:48:00 AM
 */
public class GLSLMatrixType extends GLSLType {

    private enum BaseType {
        FLOAT(GLSLTypes.FLOAT, "mat"),
        DOUBLE(GLSLTypes.DOUBLE, "dmat");

        final GLSLType type;
        final String name;

        BaseType(GLSLType baseType, String baseName) {
            this.type = baseType;
            this.name = baseName;
        }
    }

    private static final int MIN_MATRIX_DIM = 2, MAX_MATRIX_DIM = 4;
    private static final Map<GLSLType, GLSLMatrixType[][]> MATRIX_TYPES = new HashMap<GLSLType, GLSLMatrixType[][]>(BaseType.values().length);

    static {
        final int len = MAX_MATRIX_DIM - MIN_MATRIX_DIM + 1;
        for(BaseType type:BaseType.values()){
            GLSLMatrixType[][] dimensions = new GLSLMatrixType[len][len];
            for (int x = 0; x < len; x++) {
                for (int y = 0; y < len; y++) {
                    dimensions[x][y] = new GLSLMatrixType(x,y,type);
                }
            }
            MATRIX_TYPES.put(type.type, dimensions);
        }
    }

    public static GLSLMatrixType getType(int cols, int rows, GLSLType fundamentalType){
        return MATRIX_TYPES.get(fundamentalType)[cols - MIN_MATRIX_DIM][rows - MIN_MATRIX_DIM];
    }

    public static final GLSLMatrixType MAT2X2 = getType(2, 2, GLSLTypes.FLOAT);
    public static final GLSLMatrixType MAT3X2 = getType(3, 2, GLSLTypes.FLOAT);
    public static final GLSLMatrixType MAT4X2 = getType(4, 2, GLSLTypes.FLOAT);
    public static final GLSLMatrixType MAT2X3 = getType(2, 3, GLSLTypes.FLOAT);
    public static final GLSLMatrixType MAT3X3 = getType(3, 3, GLSLTypes.FLOAT);
    public static final GLSLMatrixType MAT4X3 = getType(4, 3, GLSLTypes.FLOAT);
    public static final GLSLMatrixType MAT2X4 = getType(2, 4, GLSLTypes.FLOAT);
    public static final GLSLMatrixType MAT3X4 = getType(3, 4, GLSLTypes.FLOAT);
    public static final GLSLMatrixType MAT4X4 = getType(4, 4, GLSLTypes.FLOAT);
    public static final GLSLMatrixType DMAT2X2 = getType(2, 2, GLSLTypes.DOUBLE);
    public static final GLSLMatrixType DMAT3X2 = getType(3, 2, GLSLTypes.DOUBLE);
    public static final GLSLMatrixType DMAT4X2 = getType(4, 2, GLSLTypes.DOUBLE);
    public static final GLSLMatrixType DMAT2X3 = getType(2, 3, GLSLTypes.DOUBLE);
    public static final GLSLMatrixType DMAT3X3 = getType(3, 3, GLSLTypes.DOUBLE);
    public static final GLSLMatrixType DMAT4X3 = getType(4, 3, GLSLTypes.DOUBLE);
    public static final GLSLMatrixType DMAT2X4 = getType(2, 4, GLSLTypes.DOUBLE);
    public static final GLSLMatrixType DMAT3X4 = getType(3, 4, GLSLTypes.DOUBLE);
    public static final GLSLMatrixType DMAT4X4 = getType(4, 4, GLSLTypes.DOUBLE);

    private int numColumns, numRows;
    private BaseType fundamentalType;

    private GLSLMatrixType(int numColumns, int numRows, BaseType fundamentalType) {
        this.numColumns = numColumns;
        this.numRows = numRows;
        this.fundamentalType = fundamentalType;
    }

    @Override
    @NotNull
    public String getTypename() {
        return fundamentalType.name + numColumns + "x" + numRows;
    }

    @NotNull
    @Override
    public GLSLType getBaseType() {
        return GLSLVectorType.getType(numColumns, fundamentalType.type);
    }

    public int getNumComponents() {
        return numColumns * numRows;
    }

    //region Constructor

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

    @NotNull
    @Override
    public GLSLFunctionType[] getConstructors() {
        return new GLSLFunctionType[]{
                new Constructor()
        };
    }

    //endregion

    @Override
    public boolean isConvertibleTo(GLSLType otherType) {
        if (!(otherType instanceof GLSLMatrixType)) return false;
        GLSLMatrixType other = (GLSLMatrixType) otherType;

        return numRows == other.numRows
                && numColumns == other.numColumns
                && fundamentalType.type.isConvertibleTo(other.fundamentalType.type);
    }
}
