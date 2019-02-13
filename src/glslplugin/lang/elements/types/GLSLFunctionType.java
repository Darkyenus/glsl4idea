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

/**
 * Base for all function-call-likes, that is function calls and constructors.
 *
 * @author Yngve Devik Hammersland
 *         Date: Mar 4, 2009
 *         Time: 6:47:33 PM
 */
public abstract class GLSLFunctionType {

    public static final GLSLFunctionType[] EMPTY_ARRAY = new GLSLFunctionType[0];

    private final String name;
    protected final GLSLType returnType;
    protected GLSLElement definition;
    private String typenameCache;

    protected GLSLFunctionType(@NotNull String name, @NotNull GLSLType returnType) {
        this.returnType = returnType;
        this.name = name;
    }

    @NotNull
    public String getName() {
        return name;
    }

    /** Create user readable representation of this function, can contain `help text` */
    protected abstract String generateTypename();

    @NotNull
    public String getTypename() {
        if (typenameCache == null) {
            typenameCache = generateTypename();
        }
        return typenameCache;
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
}
