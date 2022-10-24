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

import com.intellij.openapi.util.ModificationTracker;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.util.CachedValueImpl;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.types.GLSLBasicFunctionType;
import glslplugin.lang.elements.types.GLSLFunctionType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * GLSLFunctionDeclaration represents a function declaration.
 * It inherits the name, qualifier and (return) type from {@link GLSLQualifiedDeclaration}
 * and adds the parameter list.
 */
public interface GLSLFunctionDeclaration extends GLSLQualifiedDeclaration {

    /** @return the element that holds the function name */
    @Nullable
    default PsiElement getFunctionNameIdentifier() {
        return findChildByType(GLSLTokenTypes.IDENTIFIER);
    }

    default @Nullable String getFunctionName() {
        final PsiElement identifier = getFunctionNameIdentifier();
        return identifier == null ? null : identifier.getText();
    }

    @NotNull
    default GLSLParameterDeclaration[] getParameters() {
        return findChildrenByClass(GLSLParameterDeclaration.class);
    }

    @NotNull
    default GLSLType getReturnType() {
        GLSLTypeSpecifier typeSpecifier = findChildByClass(GLSLTypeSpecifier.class);
        if(typeSpecifier == null){
            return GLSLTypes.UNKNOWN_TYPE;
        }else{
            return typeSpecifier.getType();
        }
    }

    @NotNull
    default String getSignature() {
        StringBuilder b = new StringBuilder();
        b.append(getFunctionName()).append("(");
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

    @NotNull
    GLSLFunctionType getType();

    static CachedValue<@NotNull GLSLFunctionType> newCachedFunctionType(GLSLFunctionDeclaration declaration) {
        return new CachedValueImpl<>(() -> {
            final String functionName = declaration.getFunctionName();
            final GLSLParameterDeclaration[] parameterDeclarations = declaration.getParameters();
            final GLSLType[] parameterTypes = new GLSLType[parameterDeclarations.length];
            for (int i = 0; i < parameterDeclarations.length; i++) {
                GLSLDeclarator declarator = parameterDeclarations[i].getDeclarator();
                if(declarator == null){
                    parameterTypes[i] = GLSLTypes.UNKNOWN_TYPE;
                }else{
                    parameterTypes[i] = declarator.getType();
                }
            }
            final VirtualFile virtualFile = declaration.getContainingFile().getVirtualFile();
            return new CachedValueProvider.Result<>(
                    new GLSLBasicFunctionType(declaration, functionName == null ? "<no name>" : functionName, declaration.getReturnType(), parameterTypes),
                    Objects.requireNonNullElse(virtualFile, ModificationTracker.NEVER_CHANGED));
        });
    }
}
