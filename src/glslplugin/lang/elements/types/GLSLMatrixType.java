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
 * Base type for all matrices.
 *
 * All possible matrices are created once, then stored.
 * To retrieve them, use static getType().
 *
 * @author Yngve Devik Hammersland
 * @author Jan Polák
 */
public class GLSLMatrixType extends GLSLType {

    //region Static

    private enum MatrixType {
        FLOAT(GLSLTypes.FLOAT, "mat"),
        DOUBLE(GLSLTypes.DOUBLE, "dmat");

        final GLSLType type;
        final String name;

        MatrixType(GLSLType baseType, String baseName) {
            this.type = baseType;
            this.name = baseName;
        }
    }

    private static final int MIN_MATRIX_DIM = 2, MAX_MATRIX_DIM = 4;
    private static final Map<GLSLType, GLSLMatrixType[][]> MATRIX_TYPES = new HashMap<GLSLType, GLSLMatrixType[][]>(MatrixType.values().length);

    static {
        final int len = MAX_MATRIX_DIM - MIN_MATRIX_DIM + 1;
        for(MatrixType type: MatrixType.values()){
            GLSLMatrixType[][] dimensions = new GLSLMatrixType[len][len];
            for (int column = 0; column < len; column++) {
                for (int row = 0; row < len; row++) {
                    dimensions[column][row] = new GLSLMatrixType(type, MIN_MATRIX_DIM + column, MIN_MATRIX_DIM + row);
                }
            }
            MATRIX_TYPES.put(type.type, dimensions);
        }
    }

    public static GLSLMatrixType getType(GLSLType baseType, int columns, int rows){
        return MATRIX_TYPES.get(baseType)[columns - MIN_MATRIX_DIM][rows - MIN_MATRIX_DIM];
    }

    //endregion

    private final GLSLType baseType;
    private final int columns, rows;
    private final GLSLFunctionType[] constructors;

    private final MatrixType matrixType;
    private final String typename;

    private GLSLMatrixType(MatrixType matrixType, int columns, int rows) {
        super(null);
        this.matrixType = matrixType;
        this.baseType = matrixType.type;
        this.columns = columns;
        this.rows = rows;
        this.typename = matrixType.name + (columns == rows ? columns : columns + "x" + rows);
        this.constructors = new GLSLFunctionType[]{new BaseTypeConstructor(), new OtherMatrixConstructor()};
    }

    @Override
    @NotNull
    public String getTypename() {
        return typename;
    }

    /**
     * This base type is what you get by indexing, that is a vector.
     */
    @NotNull
    @Override
    public GLSLType getIndexType() {
        return GLSLVectorType.getType(baseType, rows);
    }

    @NotNull
    @Override
    public GLSLType getBaseType() {
        return baseType;
    }

    public int getNumComponents() {
        return columns * rows;
    }

    public int getNumColumns(){
        return columns;
    }

    public int getNumRows(){
        return rows;
    }

    @NotNull
    @Override
    public Map<String, GLSLFunctionType> getMemberFunctions() {
        return GLSLArrayType.ARRAY_LIKE_FUNCTIONS;//Matrices have, like arrays, .length() function
    }

    //region Constructor

    private class BaseTypeConstructor extends GLSLFunctionType {

        protected BaseTypeConstructor() {
            super(GLSLMatrixType.this.getTypename(), GLSLMatrixType.this);
        }

        @Override
        protected String generateTypename() {
            return GLSLMatrixType.this.getTypename()+"(1 or "+(columns * rows)+" elements of type "+baseType.getTypename()+")";
        }

        @Override
        @NotNull
        public GLSLTypeCompatibilityLevel getParameterCompatibilityLevel(@NotNull GLSLType[] types) {
            return GLSLVectorType.getParameterCompatibilityLevelForMatrixOrVector(types, getNumComponents());
        }
    }

    private class OtherMatrixConstructor extends GLSLFunctionType {
        protected OtherMatrixConstructor() {
            super(GLSLMatrixType.this.getTypename(), GLSLMatrixType.this);
        }

        @Override
        protected String generateTypename() {
            return GLSLMatrixType.this.getTypename()+"(1 element of type "+GLSLMatrixType.this.matrixType.name+")";
        }

        @NotNull
        @Override
        public GLSLTypeCompatibilityLevel getParameterCompatibilityLevel(@NotNull GLSLType[] types) {
            if (types.length != 1)
                return GLSLTypeCompatibilityLevel.INCOMPATIBLE;
            GLSLType type = types[0];
            if (type instanceof GLSLMatrixType && ((GLSLMatrixType) type).matrixType == GLSLMatrixType.this.matrixType) {
                return GLSLTypeCompatibilityLevel.DIRECTLY_COMPATIBLE;
            } else {
                return GLSLTypeCompatibilityLevel.INCOMPATIBLE;
            }
        }
    }

    @NotNull
    @Override
    public GLSLFunctionType[] getConstructors() {
        return constructors;
    }

    //endregion

    @Override
    public boolean isConvertibleTo(GLSLType otherType) {
        if (!(otherType instanceof GLSLMatrixType)) return false;

        GLSLMatrixType other = (GLSLMatrixType) otherType;
        return rows == other.rows
                && columns == other.columns
                && baseType.isConvertibleTo(other.baseType);
    }
}
