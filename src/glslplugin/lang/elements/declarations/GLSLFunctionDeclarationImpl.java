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

package glslplugin.lang.elements.declarations;

import com.intellij.lang.ASTNode;
import glslplugin.lang.elements.types.GLSLBasicFunctionType;
import glslplugin.lang.elements.types.GLSLFunctionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * GLSLFunctionDeclarationImpl is the psi implementation of a function declaration.
 */
public class GLSLFunctionDeclarationImpl extends GLSLSingleDeclarationImpl implements GLSLFunctionDeclaration {
    private GLSLFunctionType type;

    public GLSLFunctionDeclarationImpl(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @NotNull
    public GLSLParameterDeclaration[] getParameters() {
        GLSLDeclarationList parameterList = getParameterList();
        if(parameterList == null)return GLSLParameterDeclaration.NO_PARAMETER_DECLARATIONS;
        return castToParameters(parameterList.getDeclarations());
    }

    @Nullable
    public GLSLDeclarationList getParameterList() {
        return findChildByClass(GLSLDeclarationList.class);
    }

    @NotNull
    private static GLSLParameterDeclaration[] castToParameters(GLSLDeclaration[] declarations) {
        GLSLParameterDeclaration[] parameters = new GLSLParameterDeclaration[declarations.length];
        for (int i = 0; i < declarations.length; i++) {
            parameters[i] = (GLSLParameterDeclaration) declarations[i];
        }
        return parameters;
    }

    @NotNull
    public String getSignature() {
        StringBuilder b = new StringBuilder();
        b.append(getDeclaredName()).append("(");
        boolean first = true;
        for (GLSLParameterDeclaration declarator : getParameters()) {
            if (!first) {
                b.append(",");
            }
            first = false;
            b.append(declarator.getTypeSpecifierNodeTypeName());
        }
        b.append(") : ");
        b.append(getTypeSpecifierNodeTypeName());
        return b.toString();
    }

    @Override
    public String toString() {
        return "Function Declaration: " + getSignature();
    }

    @NotNull
    public GLSLFunctionType getType() {
        if (type == null) {
            type = createType();
        }
        return type;
    }

    private GLSLFunctionType createType() {
        return new GLSLBasicFunctionType(this);
    }
}
