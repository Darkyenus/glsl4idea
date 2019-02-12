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

import glslplugin.lang.elements.types.constructors.GLSLAggregateParamConstructor;
import glslplugin.lang.elements.types.constructors.GLSLScalarParamConstructor;
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
    private static final Map<GLSLType, GLSLMatrixType[][]> MATRIX_TYPES = new HashMap<>(BaseType.values().length);

    static {
        final int len = MAX_MATRIX_DIM - MIN_MATRIX_DIM + 1;
        for(BaseType type:BaseType.values()){
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
    private final String typename;

    private GLSLMatrixType(BaseType baseType, int columns, int rows) {
        super(null);
        this.baseType = baseType.type;
        this.columns = columns;
        this.rows = rows;
        this.typename = baseType.name + (columns == rows ? columns : columns + "x" + rows);
        this.constructors = new GLSLFunctionType[]{
                new GLSLScalarParamConstructor(this),
                new GLSLAggregateParamConstructor(this, false, columns * rows),
                new GLSLFunctionType(this.typename, this) {
                    @Override
                    protected String generateTypename() {
                        return typename +"(matrix)";
                    }

                    @NotNull
                    @Override
                    public GLSLTypeCompatibilityLevel getParameterCompatibilityLevel(@NotNull GLSLType[] types) {
                        if(types.length == 1 && types[0] instanceof GLSLMatrixType){
                            return GLSLTypeCompatibilityLevel.DIRECTLY_COMPATIBLE;
                        } else return GLSLTypeCompatibilityLevel.INCOMPATIBLE;
                    }
                }};
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

    @NotNull
    @Override
    public GLSLFunctionType[] getConstructors() {
        return constructors;
    }

    @Override
    public boolean isConvertibleTo(GLSLType otherType) {
        if (!(otherType instanceof GLSLMatrixType)) return false;

        GLSLMatrixType other = (GLSLMatrixType) otherType;
        return rows == other.rows
                && columns == other.columns
                && baseType.isConvertibleTo(other.baseType);
    }
}
