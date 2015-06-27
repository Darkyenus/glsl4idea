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

import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.declarations.GLSLFunctionDeclaration;
import glslplugin.lang.elements.declarations.GLSLParameterDeclaration;
import org.jetbrains.annotations.NotNull;

/**
 * GLSLFunctionType is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Mar 2, 2009
 *         Time: 12:20:32 PM
 */
public class GLSLBasicFunctionType extends GLSLFunctionType {

    private final GLSLType[] parameterTypes;

    public GLSLBasicFunctionType(GLSLFunctionDeclaration declaration) {
        super(declaration.getDeclaredName(), declaration.getTypeSpecifierNode().getType());
        final GLSLParameterDeclaration[] parameterDeclarations = declaration.getParameters();
        definition = declaration;

        parameterTypes = new GLSLType[parameterDeclarations.length];
        for (int i = 0; i < parameterDeclarations.length; i++) {
            GLSLDeclarator declarator = parameterDeclarations[i].getDeclarator();
            if(declarator == null){
                parameterTypes[i] = GLSLTypes.UNKNOWN_TYPE;
            }else{
                parameterTypes[i] = declarator.getType();
            }
        }

        this.typename = generateTypename();
    }

    public GLSLBasicFunctionType(@NotNull String name, @NotNull GLSLType type, @NotNull GLSLType... parameterTypes) {
        super(name, type);
        this.parameterTypes = parameterTypes;
        this.typename = generateTypename();
    }

    protected String generateTypename() {
        StringBuilder b = new StringBuilder();
        b.append('(');
        boolean first = true;
        for (GLSLType type : parameterTypes) {
            if (!first) {
                b.append(',');
            }
            first = false;
            b.append(type.getTypename());
        }
        b.append(") : ").append(getReturnType().getTypename());
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
