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

package glslplugin.lang.elements.expressions;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLIdentifier;
import glslplugin.lang.elements.GLSLReferenceElement;
import glslplugin.lang.elements.GLSLTypedElement;
import glslplugin.lang.elements.declarations.GLSLFunctionDeclaration;
import glslplugin.lang.elements.declarations.GLSLFunctionDefinition;
import glslplugin.lang.elements.declarations.GLSLTypeSpecifier;
import glslplugin.lang.elements.declarations.GLSLVariableDeclaration;
import glslplugin.lang.elements.reference.GLSLFunctionReference;
import glslplugin.lang.elements.types.GLSLFunctionType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * GLSLFunctionCallExpression is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 29, 2009
 *         Time: 10:34:04 AM
 */
public class GLSLFunctionCallExpression extends GLSLExpression implements GLSLReferenceElement {
    public GLSLFunctionCallExpression(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    public GLSLIdentifier getFunctionNameIdentifier() {
        final PsiElement first = getFirstChild();
        if(first instanceof GLSLIdentifier) return (GLSLIdentifier) first;
        else return null;
    }

    @NotNull
    public String getFunctionName() {
        GLSLIdentifier identifier = getFunctionNameIdentifier();
        if (identifier != null) {
            String name = identifier.getName();
            if (name != null) return name;
        }
        return "(unknown)";
    }

    @Nullable
    public GLSLParameterList getParameterList() {
        return findChildByClass(GLSLParameterList.class);
    }

    @NotNull
    public GLSLType[] getParameterTypes(){
        GLSLParameterList parameterList = getParameterList();
        if(parameterList != null)return parameterList.getParameterTypes();
        else return GLSLType.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public GLSLType getType() {
        GLSLElement declaration = getReferenceProxy().resolve();

        if (declaration instanceof GLSLTypedElement) return ((GLSLTypedElement) declaration).getType();
        return GLSLTypes.UNKNOWN_TYPE;
    }

    @NotNull
    public GLSLFunctionReference getReferenceProxy() {
        return new GLSLFunctionReference(this);
    }

    @Override
    public String toString() {
        return "Function Call: " + getFunctionName();
    }
}
