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

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.LookupItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GLGLBuiltInTypes holds all the built-in types for easy access.
 * These types are not constructible by other classes so reference comparison can be used.
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 26, 2009
 *         Time: 4:00:42 PM
 */
@SuppressWarnings("unused")
public class GLSLTypes {

    private static final ArrayList<GLSLType> builtinTypes = new ArrayList<>();
    private static final Map<String, GLSLType> namesToTypes = new HashMap<>();
    private static final ArrayList<LookupElement> typeLookupElements = new ArrayList<>();

    static {
        // Register all built-in types

        // Scalars
        for (GLSLScalarType scalar : GLSLScalarType.SCALARS) {
            builtinTypes.add(scalar);
            register(scalar);
        }

        // Vectors
        for (GLSLVectorType[] byBaseType : GLSLVectorType.VECTOR_TYPES.values()) {
            for (GLSLVectorType vectorType : byBaseType) {
                builtinTypes.add(vectorType);
                register(vectorType);
            }
        }

        // Matrices
        for (GLSLMatrixType[][] byBaseType : GLSLMatrixType.MATRIX_TYPES.values()) {
            for (GLSLMatrixType[] byFirstDim : byBaseType) {
                for (GLSLMatrixType matrixType : byFirstDim) {
                    builtinTypes.add(matrixType);
                    register(matrixType.fullName, matrixType);
                    if (!matrixType.shortName.equals(matrixType.fullName)) {
                        register(matrixType.shortName, matrixType);
                    }
                }
            }
        }

        // Opaque types
        for (GLSLOpaqueType opaqueType : GLSLOpaqueType.ALL) {
            builtinTypes.add(opaqueType);
            register(opaqueType);
        }
        for (GLSLOpaqueType.Sampler opaqueType : GLSLOpaqueType.Sampler.ALL) {
            builtinTypes.add(opaqueType);
            register(opaqueType);
        }
        for (GLSLOpaqueType.ShadowSampler opaqueType : GLSLOpaqueType.ShadowSampler.ALL) {
            builtinTypes.add(opaqueType);
            register(opaqueType);
        }
        for (GLSLOpaqueType.Image opaqueType : GLSLOpaqueType.Image.ALL) {
            builtinTypes.add(opaqueType);
            register(opaqueType);
        }
    }

    public static final GLSLType UNKNOWN_TYPE = new GLSLType(null) {
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
    };

    private static final Map<String, GLSLType> undefinedTypes = new HashMap<>();

    public static GLSLType getUndefinedType(final String text) {
        if (!undefinedTypes.containsKey(text)) {
            undefinedTypes.put(text,
                    new GLSLType(null) {
                        private final String name = text;

                        @NotNull
                        public String getTypename() {
                            return name;
                        }
                    });
        }
        return undefinedTypes.get(text);
    }

    private static void register(GLSLType type) {
        namesToTypes.put(type.getTypename(), type);
    }

    private static void register(String name, GLSLType type) {
        namesToTypes.put(name, type);
    }

    public static GLSLType getTypeFromName(String name) {
        return namesToTypes.get(name);
    }

    public static List<GLSLType> builtinTypes() {
        return builtinTypes;
    }
}
