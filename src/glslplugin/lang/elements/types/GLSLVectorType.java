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
 * Base type for GLSL vectors.
 *
 * All possible vectors are created once, then stored.
 * To retrieve them, use static getType().
 *
 * @author Yngve Devik Hammersland
 * @author Jan Polák
 */
public class GLSLVectorType extends GLSLType {

    //region Static

    private enum BaseType {
        INT(GLSLScalarType.INT, "ivec"),
        UINT(GLSLScalarType.UINT, "uvec"),
        BOOL(GLSLScalarType.BOOL, "bvec"),
        FLOAT(GLSLScalarType.FLOAT, "vec"),
        DOUBLE(GLSLScalarType.DOUBLE, "dvec");

        final GLSLType type;
        final String name;

        BaseType(GLSLType baseType, String baseName) {
            this.type = baseType;
            this.name = baseName;
        }

    }

    private static final int MIN_VECTOR_DIM = 2, MAX_VECTOR_DIM = 4;
    private static final Map<GLSLType, GLSLVectorType[]> VECTOR_TYPES = new HashMap<GLSLType, GLSLVectorType[]>(BaseType.values().length);

    static {
        for(BaseType type:BaseType.values()){
            GLSLVectorType[] dimensions = new GLSLVectorType[MAX_VECTOR_DIM - MIN_VECTOR_DIM + 1];
            for (int i = 0; i < dimensions.length; i++) {
                dimensions[i] = new GLSLVectorType(type, i + MIN_VECTOR_DIM);
            }
            VECTOR_TYPES.put(type.type, dimensions);
        }
    }

    public static GLSLVectorType getType(GLSLType baseType, int componentCount) {
        return VECTOR_TYPES.get(baseType)[componentCount - MIN_VECTOR_DIM];
    }

    //endregion

    private final GLSLType baseType;
    private final int numComponents;
    private final GLSLFunctionType[] constructor;
    private final String typeName;

    private GLSLVectorType(BaseType baseType, int numComponents) {
        super(null);
        this.baseType = baseType.type;
        this.numComponents = numComponents;
        this.typeName = baseType.name + numComponents;
        this.constructor = new GLSLFunctionType[]{new Constructor()};
    }

    @NotNull
    @Override
    public String getTypename() {
        return typeName;
    }

    @NotNull
    @Override
    public GLSLType getIndexType() {
        return baseType;
    }

    @Override
    public boolean isIndexable() {
        return true;//Vectors are indexable
    }

    @Override
    public boolean hasMembers() {
        return true;
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static final String[] SWIZZLE_SETS = new String[]{"xyzw","rgba","stpq"};

    @Override
    public boolean hasMember(String member) {
        if(member == null || member.isEmpty())return false; //Just in case
        if(member.length() > MAX_VECTOR_DIM)return false; //There is no vec5 or more elements
        String swizzleSet = null;
        // Determine swizzle set
        {
            char first = member.charAt(0);
            for(String set:SWIZZLE_SETS){
                if(set.indexOf(first) != -1){
                    swizzleSet = set;
                    break;
                }
            }
        }
        if(swizzleSet == null)return false; //Not selecting any component
        //Check for just component selection, no swizzling then
        if(member.length() == 1) return true;
        //Check if all remaining components are from the same swizzle set
        for (int i = 1; i < member.length(); i++) {
            if(swizzleSet.indexOf(member.charAt(i)) == -1){
                //Component is not from this swizzle set (or completely invalid)
                return false;
            }
        }
        //Check if not accessing member of larger vector (ie .b on vec2)
        for (int i = 0; i < member.length(); i++) {
            if(swizzleSet.indexOf(member.charAt(i)) >= numComponents){
                //This is only part of larger vectors
                return false;
            }
        }
        return true;
    }

    @NotNull
    @Override
    public GLSLType getMemberType(String member) {
        if(hasMember(member)){
            if(member.length() == 1){
                return baseType;
            }else{
                return getType(baseType, member.length());
            }
        }else{
            return GLSLTypes.UNKNOWN_TYPE;
        }
    }

    @NotNull
    @Override
    public Map<String, GLSLFunctionType> getMemberFunctions() {
        return GLSLArrayType.ARRAY_LIKE_FUNCTIONS;//Vectors have, like arrays, .length() function
    }

    //region Constructor

    private class Constructor extends GLSLFunctionType {
        protected Constructor() {
            super(GLSLVectorType.this.getTypename(), GLSLVectorType.this);
        }

        @Override
        protected String generateTypename() {
            return "(...) : " + GLSLVectorType.this.getTypename();
        }

        @Override
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
        return constructor;
    }

    //endregion

    public int getNumComponents() {
        return numComponents;
    }

    @Override
    public boolean isConvertibleTo(GLSLType otherType) {
        if (!(otherType instanceof GLSLVectorType)) return false;

        GLSLVectorType other = (GLSLVectorType) otherType;
        return other.numComponents == numComponents
                && baseType.isConvertibleTo(other.baseType);
    }
}
