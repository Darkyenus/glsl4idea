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
import com.intellij.psi.PsiCheckedRenameElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import glslplugin.lang.elements.GLSLIdentifier;
import glslplugin.lang.elements.types.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * GLSLFunctionDeclarationImpl is the psi implementation of a function declaration.
 */
public class GLSLFunctionDeclarationImpl extends GLSLSingleDeclarationImpl implements GLSLFunctionDeclaration, PsiNameIdentifierOwner, PsiCheckedRenameElement {
    private GLSLFunctionType typeCache;
    private boolean typeCacheDirty = false;

    public GLSLFunctionDeclarationImpl(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @NotNull
    public GLSLParameterDeclaration[] getParameters() {
        GLSLDeclarationList parameterList = getParameterList();
        if(parameterList == null)return GLSLParameterDeclaration.NO_PARAMETER_DECLARATIONS;
        return castToParameters(parameterList.getDeclarations());
    }

    @NotNull
    @Override
    public GLSLType getReturnType() {
        GLSLTypeSpecifier typeSpecifier = findChildByClass(GLSLTypeSpecifier.class);
        if(typeSpecifier == null){
            return GLSLTypes.UNKNOWN_TYPE;
        }else{
            return typeSpecifier.getType();
        }
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
        b.append(getName()).append("(");
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
        if (typeCache == null || typeCacheDirty) {
            typeCache = createType();
            typeCacheDirty = false;
        }
        return typeCache;
    }

    private GLSLFunctionType createType() {
        final GLSLParameterDeclaration[] parameterDeclarations = getParameters();
        final GLSLType[] parameterTypes = new GLSLType[parameterDeclarations.length];
        for (int i = 0; i < parameterDeclarations.length; i++) {
            GLSLDeclarator declarator = parameterDeclarations[i].getDeclarator();
            if(declarator == null){
                parameterTypes[i] = GLSLTypes.UNKNOWN_TYPE;
            }else{
                parameterTypes[i] = declarator.getType();
            }
        }
        return new GLSLBasicFunctionType(this, getName(), getReturnType(), parameterTypes);
    }

    @NotNull
    @Override
    public String getDeclarationDescription() {
        return "function";
    }

    @Nullable
    @Override
    public GLSLIdentifier getNameIdentifier() {
        final GLSLDeclarator declarator = getDeclarator();
        if(declarator == null)return null;
        return declarator.getNameIdentifier();
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        final GLSLIdentifier nameIdentifier = getNameIdentifier();
        if(nameIdentifier == null)throw new IncorrectOperationException("GLSLDeclarator is null");
        return nameIdentifier.setName(name);
    }

    @Override
    public void checkSetName(String name) throws IncorrectOperationException {
        final GLSLIdentifier nameIdentifier = getNameIdentifier();
        if(nameIdentifier == null)throw new IncorrectOperationException("GLSLDeclarator is null");
        nameIdentifier.checkSetName(name);
    }

    @Override
    public void subtreeChanged() {
        super.subtreeChanged();
        typeCacheDirty = true;
    }
}
