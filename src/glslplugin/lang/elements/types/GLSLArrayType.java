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

import glslplugin.lang.elements.declarations.GLSLArraySpecifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * NewArrayTypeImpl is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 6, 2009
 *         Time: 11:57:22 PM
 */
public class GLSLArrayType extends GLSLType {
    private GLSLType baseType;
    private GLSLArraySpecifier arraySpecifier;
    private Map<String, GLSLFunctionType> methods = new HashMap<String, GLSLFunctionType>();

    public GLSLArrayType(@NotNull GLSLType baseType, @Nullable GLSLArraySpecifier arraySpecifier) {
        this.baseType = baseType;
        this.arraySpecifier = arraySpecifier;
        this.methods.put("length", new GLSLBasicFunctionType("length", baseType));
    }

    @Override
    @NotNull
    public GLSLType getBaseType() {
        return baseType;
    }

    @NotNull
    public String getTypename() {
        return baseType.getTypename() + "[]";
    }

    @Override
    @Nullable
    public GLSLArraySpecifier getArraySpecifier() {
        return arraySpecifier;
    }

    @Override
    @NotNull
    public Map<String, GLSLFunctionType> getMethods() {
        return methods;
    }
}
