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

import glslplugin.lang.elements.declarations.GLSLFunctionDeclaration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * FunctionType for functions (= not constructors)
 *
 * @author Yngve Devik Hammersland
 *         Date: Mar 2, 2009
 *         Time: 12:20:32 PM
 */
public class GLSLBasicFunctionType extends GLSLFunctionType {

    private final GLSLType[] parameterTypes;

    public GLSLBasicFunctionType(@Nullable GLSLFunctionDeclaration definition,
                                 @NotNull String name, @NotNull GLSLType returnType,
                                 @NotNull GLSLType... parameterTypes) {
        super(name, returnType, definition);
        this.parameterTypes = parameterTypes;
    }

    public GLSLBasicFunctionType(@NotNull String name, @NotNull GLSLType returnType,
                                 @NotNull GLSLType... parameterTypes) {
        super(name, returnType, null);
        this.parameterTypes = parameterTypes;
    }

    protected String generateTypename() {
        StringBuilder b = new StringBuilder();
        b.append(getReturnType().getTypename()).append(' ').append(getName());
        b.append('(');
        boolean first = true;
        for (GLSLType type : parameterTypes) {
            if (!first) {
                b.append(',');
            }
            first = false;
            b.append(type.getTypename());
        }
        b.append(")");
        return b.toString();
    }

    @NotNull
    public GLSLTypeCompatibilityLevel getParameterCompatibilityLevel(@NotNull GLSLType[] types) {
        return GLSLTypeCompatibilityLevel.getCompatibilityLevel(types, parameterTypes);
    }

    @NotNull
    public GLSLType[] getParameterTypes() {
        return parameterTypes;
    }
}
