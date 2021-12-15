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

import java.util.Collections;
import java.util.Map;

/**
 * GLSLArrayType defines the type of one or multidimensional array.
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 6, 2009
 *         Time: 11:57:22 PM
 * @author Jan Polák
 */
public class GLSLArrayType extends GLSLType {

    public static final int UNDEFINED_SIZE_DIMENSION = Integer.MIN_VALUE;
    public static final int DYNAMIC_SIZE_DIMENSION = Integer.MIN_VALUE+1;
    public static final Map<String, GLSLFunctionType> ARRAY_LIKE_FUNCTIONS = Collections.singletonMap("length", new GLSLBasicFunctionType("length", GLSLTypes.INT));

    private final GLSLType baseType;
    private final int[] dimensions;

    public GLSLArrayType(@NotNull GLSLType baseType, int...dimensions) {
        super(null);
        this.baseType = baseType;
        this.dimensions = dimensions;
    }

    @Override
    public boolean isIndexable() {
        return true;
    }

    @Override
    @NotNull
    public GLSLType getIndexType() {
        if(dimensions.length == 1){
            return baseType;
        }else{
            int[] indexTypeDimensions = new int[dimensions.length - 1];
            System.arraycopy(dimensions, 1, indexTypeDimensions, 0, indexTypeDimensions.length);
            return new GLSLArrayType(baseType, indexTypeDimensions);
        }
    }

    @NotNull
    @Override
    public GLSLType getBaseType() {
        return baseType;
    }

    @NotNull
    public String getTypename() {
        StringBuilder result = new StringBuilder(baseType.getTypename());
        for (int dimension : dimensions) {
            result.append('[');
            //noinspection StatementWithEmptyBody
            if(dimension == UNDEFINED_SIZE_DIMENSION){
                // Unknown during declaration: []
            }else if(dimension == DYNAMIC_SIZE_DIMENSION){
                result.append('?');
            }else{
                result.append(dimension);
            }
            result.append(']');
        }
        return result.toString();
    }

    /**
     * Retrieve the number of dimensions this array has and their sizes.
     * Returned array has as many elements as the type has dimensions.
     * Elements of returned array denote the sizes of type's dimensions.
     * For dimension of yet unknown length, {@link GLSLArrayType#UNDEFINED_SIZE_DIMENSION} is returned.
     * For dimension of length known only at runtime, {@link GLSLArrayType#DYNAMIC_SIZE_DIMENSION} is returned.
     *
     * For example for "int[3] exampleArray", this will return "new int[]{3}".
     */
    @NotNull
    public int[] getDimensions() {
        return dimensions;
    }

    @NotNull
    @Override
    public Map<String, GLSLFunctionType> getMemberFunctions() {
        return ARRAY_LIKE_FUNCTIONS;
    }

    private boolean dimensionSizeEquals(int first, int second){
        if(first == UNDEFINED_SIZE_DIMENSION || first == DYNAMIC_SIZE_DIMENSION
                || second == UNDEFINED_SIZE_DIMENSION || second == DYNAMIC_SIZE_DIMENSION){
            //If the size is not known, consider them same, because they might be the same
            //So don't show error if not 100% certain there is one
            return true;
        }else{
            return first == second;
        }
    }

    @Override
    public boolean typeEquals(GLSLType otherType) {
        if(otherType instanceof GLSLArrayType){
            GLSLArrayType other = (GLSLArrayType) otherType;
            if(dimensions.length == other.dimensions.length){
                for (int i = 0; i < dimensions.length; i++) {
                    if(!dimensionSizeEquals(dimensions[i], other.dimensions[i])){
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
