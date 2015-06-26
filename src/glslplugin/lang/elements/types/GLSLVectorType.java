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
public class GLSLVectorType extends GLSLType {
    private class Constructor extends GLSLFunctionType {
        protected Constructor() {
            super(GLSLVectorType.this.getTypename(), GLSLVectorType.this);
        }

        protected String generateTypename() {
            return "(...) : " + GLSLVectorType.this.getTypename();
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

    private enum BaseType {
        INT(GLSLPrimitiveType.INT, "ivec"),
        UINT(GLSLPrimitiveType.UINT, "uvec"),
        BOOL(GLSLPrimitiveType.BOOL, "bvec"),
        FLOAT(GLSLPrimitiveType.FLOAT, "vec"),
        DOUBLE(GLSLPrimitiveType.DOUBLE, "dvec");

        final GLSLType type;
        final String name;

        BaseType(GLSLType baseType, String baseName) {
            this.type = baseType;
            this.name = baseName;
        }

        static BaseType fromType(GLSLType type) {
            if (type == GLSLTypes.INT) return INT;
            if (type == GLSLTypes.UINT) return UINT;
            if (type == GLSLTypes.FLOAT) return FLOAT;
            if (type == GLSLTypes.DOUBLE) return DOUBLE;
            if (type == GLSLTypes.BOOL) return BOOL;
            throw new RuntimeException("Unsupported vector base type: '" + type.getTypename() + "'");
        }
    }


    static {
        createMemberStringBuilder = new StringBuilder();
        xyzw = "xyzw".toCharArray();
        rgba = "rgba".toCharArray();
        stpq = "stpq".toCharArray();

        vectorTypes = createVectorTypes();

        BVEC2 = getTypeFromName("bvec2");
        BVEC3 = getTypeFromName("bvec3");
        BVEC4 = getTypeFromName("bvec4");
        IVEC2 = getTypeFromName("ivec2");
        IVEC3 = getTypeFromName("ivec3");
        IVEC4 = getTypeFromName("ivec4");
        UVEC2 = getTypeFromName("uvec2");
        UVEC3 = getTypeFromName("uvec3");
        UVEC4 = getTypeFromName("uvec4");
        VEC2 = getTypeFromName("vec2");
        VEC3 = getTypeFromName("vec3");
        VEC4 = getTypeFromName("vec4");
        DVEC2 = getTypeFromName("dvec2");
        DVEC3 = getTypeFromName("dvec3");
        DVEC4 = getTypeFromName("dvec4");
    }

    private static final StringBuilder createMemberStringBuilder;
    private static final char[] xyzw;
    private static final char[] rgba;
    private static final char[] stpq;

    private static final HashMap<String, GLSLVectorType> vectorTypes;

    public static final GLSLVectorType BVEC2;
    public static final GLSLVectorType BVEC3;
    public static final GLSLVectorType BVEC4;
    public static final GLSLVectorType IVEC2;
    public static final GLSLVectorType IVEC3;
    public static final GLSLVectorType IVEC4;
    public static final GLSLVectorType UVEC2;
    public static final GLSLVectorType UVEC3;
    public static final GLSLVectorType UVEC4;
    public static final GLSLVectorType VEC2;
    public static final GLSLVectorType VEC3;
    public static final GLSLVectorType VEC4;
    public static final GLSLVectorType DVEC2;
    public static final GLSLVectorType DVEC3;
    public static final GLSLVectorType DVEC4;


    private static HashMap<String, GLSLVectorType> createVectorTypes() {
        // Create the vector types
        HashMap<String, GLSLVectorType> vectorTypes = new HashMap<String, GLSLVectorType>(50);
        for (int i = 2; i <= 4; i++) {
            for (BaseType type : BaseType.values()) {
                GLSLVectorType vectorType = new GLSLVectorType(i, type);
                vectorTypes.put(vectorType.getTypename(), vectorType);
            }
        }

        // Create the member maps
        for (GLSLVectorType type : vectorTypes.values()) {
            type.createMemberMap(vectorTypes);
        }

        return vectorTypes;
    }

    private void createMemberMap(HashMap<String, GLSLVectorType> vectorTypes) {
        GLSLType vec1 = baseType.type;
        GLSLVectorType vec2 = vectorTypes.get(baseType.name + 2);
        GLSLVectorType vec3 = vectorTypes.get(baseType.name + 3);
        GLSLVectorType vec4 = vectorTypes.get(baseType.name + 4);

        for (int i0 = 0; i0 < numComponents; i0++) {
            members.put(createMember(xyzw, i0), vec1);
            members.put(createMember(rgba, i0), vec1);
            members.put(createMember(stpq, i0), vec1);

            for (int i1 = 0; i1 < numComponents; i1++) {
                members.put(createMember(xyzw, i0, i1), vec2);
                members.put(createMember(rgba, i0, i1), vec2);
                members.put(createMember(stpq, i0, i1), vec2);

                for (int i2 = 0; i2 < numComponents; i2++) {
                    members.put(createMember(xyzw, i0, i1, i2), vec3);
                    members.put(createMember(rgba, i0, i1, i2), vec3);
                    members.put(createMember(stpq, i0, i1, i2), vec3);

                    for (int i3 = 0; i3 < numComponents; i3++) {
                        members.put(createMember(xyzw, i0, i1, i2, i3), vec4);
                        members.put(createMember(rgba, i0, i1, i2, i3), vec4);
                        members.put(createMember(stpq, i0, i1, i2, i3), vec4);
                    }
                }
            }
        }
    }


    private static String createMember(char[] set, int... indices) {
        createMemberStringBuilder.setLength(0);
        for (int i : indices) {
            createMemberStringBuilder.append(set[i]);
        }
        return createMemberStringBuilder.toString();
    }

    public static GLSLVectorType getTypeFromName(String name) {
        GLSLVectorType type = vectorTypes.get(name);
        assert type != null : "Unknown vector name: '" + name + "'.";
        return type;
    }

    public static GLSLVectorType getType(int numComponents, GLSLType baseType) {

        return getTypeFromName(BaseType.fromType(baseType).name + numComponents);
    }

    private GLSLFunctionType constructor;
    private int numComponents;
    private BaseType baseType;
    private Map<String, GLSLType> members = new HashMap<String, GLSLType>();

    private GLSLVectorType(int numComponents, BaseType baseType) {
        //NOTE: do not fill the member map here as it needs to refer the other vector types.
        this.baseType = baseType;
        this.numComponents = numComponents;
        this.constructor = new Constructor();
    }

    @NotNull
    @Override
    public String getTypename() {
        return baseType.name + numComponents;
    }

    @NotNull
    @Override
    public GLSLType getBaseType() {
        return baseType.type;
    }

    @NotNull
    public Map<String, GLSLType> getMembers() {
        return members;
    }

    @Override
    public boolean hasMembers() {
        return true;
    }

    @NotNull
    @Override
    public GLSLFunctionType[] getConstructors() {
        return new GLSLFunctionType[]{
                constructor
        };
    }

    public int getNumComponents() {
        return numComponents;
    }

    @Override
    public boolean isConvertibleTo(GLSLType otherType) {
        if (!(otherType instanceof GLSLVectorType)) return false;
        GLSLVectorType other = (GLSLVectorType) otherType;

        return other.getNumComponents() == getNumComponents()
                && getBaseType().isConvertibleTo(other.getBaseType());
    }
}
