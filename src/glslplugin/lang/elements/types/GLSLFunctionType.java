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

import glslplugin.lang.elements.GLSLElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * GLSLFunctionType is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Mar 4, 2009
 *         Time: 6:47:33 PM
 */
public abstract class GLSLFunctionType {
    public static final GLSLFunctionType[] EMPTY_ARRAY = {};

    protected String typename;
    private final String name;
    protected final GLSLType returnType;
    GLSLElement definition;

    protected GLSLFunctionType(@NotNull String name, @NotNull GLSLType returnType) {
        this.returnType = returnType;
        this.name = name;
    }

    @NotNull
    public String getName() {
        return name;
    }

    protected abstract String generateTypename();

    @NotNull
    public String getTypename() {
        if (typename == null) {
            typename = generateTypename();
        }
        return typename;
    }

    /**
     * @return The return type of the function.
     */
    @NotNull
    public GLSLType getReturnType() {
        return returnType;
    }

    public GLSLElement getDefinition() {
        return definition;
    }

    @NotNull
    public abstract GLSLTypeCompatibilityLevel getParameterCompatibilityLevel(@NotNull GLSLType[] types);

    /**
     * Filters a list of function-type alternatives to the set of types matching the parameter types.
     *
     * @param alternatives   the alternatives to filter
     * @param parameterTypes the parameter-types to match
     * @return the alternatives which is applicable to parameterTypes
     */
    public static GLSLFunctionType[] findApplicableTypes(GLSLFunctionType[] alternatives, GLSLType[] parameterTypes) {
        List<GLSLFunctionType> filteredAlternatives = new ArrayList<GLSLFunctionType>(alternatives.length);
        for (GLSLFunctionType alternative : alternatives) {
            GLSLTypeCompatibilityLevel compatibility = alternative.getParameterCompatibilityLevel(parameterTypes);
            switch (compatibility) {
                case COMPATIBLE_WITH_IMPLICIT_CONVERSION:
                    filteredAlternatives.add(alternative);
                    break;

                case DIRECTLY_COMPATIBLE:
                    return new GLSLFunctionType[]{alternative};

                case INCOMPATIBLE:
                    // do nothing
                    break;
            }
        }
        return filteredAlternatives.toArray(new GLSLFunctionType[filteredAlternatives.size()]);
    }
}
