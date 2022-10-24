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
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.CachedValue;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.types.GLSLFunctionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * GLSLFunctionDeclarationImpl is the psi implementation of a function declaration.
 */
public class GLSLFunctionDeclarationImpl extends GLSLElementImpl implements GLSLFunctionDeclaration {

    private final CachedValue<GLSLFunctionType> functionTypeCache = GLSLFunctionDeclaration.newCachedFunctionType(this);

    public GLSLFunctionDeclarationImpl(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Override
    public String toString() {
        return "Function Declaration: " + getSignature();
    }

    @NotNull
    public GLSLFunctionType getType() {
        return functionTypeCache.getValue();
    }

    @Nullable
    @Override
    public String getName() {
        return getFunctionName();
    }

    @Override
    public int getTextOffset() {
        final PsiElement identifier = getFunctionNameIdentifier();
        return identifier != null ? identifier.getTextOffset() : super.getTextOffset();
    }
}
