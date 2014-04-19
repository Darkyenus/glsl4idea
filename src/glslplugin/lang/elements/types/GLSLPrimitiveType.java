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

/**
 * GLSLPrimitiveType represents a primitive type,
 * and includes, bool, float, int, vectors, matrices and samplers.
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 5, 2009
 *         Time: 8:34:45 PM
 */
public class GLSLPrimitiveType extends GLSLType {
    // void
    public static final GLSLPrimitiveType VOID = new GLSLPrimitiveType("void");

    // scalars
    public static final GLSLPrimitiveType BOOL = new GLSLPrimitiveType("bool");
    public static final GLSLPrimitiveType INT = new GLSLPrimitiveType("int");
    public static final GLSLPrimitiveType FLOAT = new GLSLPrimitiveType("float");

    // matrices
    public static final GLSLPrimitiveType MAT2 = new GLSLPrimitiveType("mat2");
    public static final GLSLPrimitiveType MAT3 = new GLSLPrimitiveType("mat3");
    public static final GLSLPrimitiveType MAT4 = new GLSLPrimitiveType("mat4");

    // non-square matrices
    public static final GLSLPrimitiveType MAT2X3 = new GLSLPrimitiveType("mat2x3");
    public static final GLSLPrimitiveType MAT2X4 = new GLSLPrimitiveType("mat2x4");
    public static final GLSLPrimitiveType MAT3X2 = new GLSLPrimitiveType("mat3x2");
    public static final GLSLPrimitiveType MAT3X4 = new GLSLPrimitiveType("mat3x4");
    public static final GLSLPrimitiveType MAT4X2 = new GLSLPrimitiveType("mat4x2");
    public static final GLSLPrimitiveType MAT4X3 = new GLSLPrimitiveType("mat4x3");

    // samplers
    public static final GLSLPrimitiveType SAMPLER1D = new GLSLPrimitiveType("sampler1D");
    public static final GLSLPrimitiveType SAMPLER2D = new GLSLPrimitiveType("sampler2D");
    public static final GLSLPrimitiveType SAMPLER3D = new GLSLPrimitiveType("sampler3D");
    public static final GLSLPrimitiveType SAMPLER1D_SHADOW = new GLSLPrimitiveType("sampler1DShadow");
    public static final GLSLPrimitiveType SAMPLER2D_SHADOW = new GLSLPrimitiveType("sampler2DShadow");
    public static final GLSLPrimitiveType SAMPLER_CUBE = new GLSLPrimitiveType("samplerCube");

    private final String typename;

    private GLSLPrimitiveType(String typename) {
        this.typename = typename;
    }

    public String getTypename() {
        return typename;
    }

    public boolean typeEquals(GLSLType otherType) {
        if (otherType instanceof GLSLPrimitiveType) {
            return typeEquals((GLSLPrimitiveType) otherType);
        }
        return false;
    }

    public boolean typeEquals(GLSLPrimitiveType otherType) {
        return this == otherType;
    }

    public boolean isConvertibleTo(GLSLType otherType) {
        if (!otherType.isValidType()) return true;
        if (otherType instanceof GLSLPrimitiveType) {
            GLSLPrimitiveType otherPrimitiveType = (GLSLPrimitiveType) otherType;
            if (typeEquals(otherPrimitiveType)) {
                // equal types are assignable
                return true;
            } else if (this == INT && otherPrimitiveType == FLOAT) {
                // int -> float conversion is allowed
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return "Primitive Type: " + getTypename();
    }
}
