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
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.PsiTreeUtil;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.types.GLSLFunctionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A function declaration with a definition.
 * Opposed to just declaration, this is actually a referencable declaration.
 * To make this work, file declarations are seen everywhere,
 * and there is an inspection that checks that the order is correct.
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 2, 2009
 *         Time: 12:32:21 PM
 */
public class GLSLFunctionDefinitionImpl extends GLSLElementImpl implements GLSLFunctionDefinition {

    private final CachedValue<GLSLFunctionType> functionTypeCache = GLSLFunctionDeclaration.newCachedFunctionType(this);

    public GLSLFunctionDefinitionImpl(ASTNode node) {
        super(node);
    }

    @NotNull
    public GLSLFunctionType getType() {
        return functionTypeCache.getValue();
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, @Nullable PsiElement lastParent, @NotNull PsiElement place) {
        final boolean lookingFromInside = lastParent != null || PsiTreeUtil.isAncestor(this, place, false);

        // Can't see the function from inside
        if (lookingFromInside) {
            // Show parameter declarations
            for (GLSLParameterDeclaration parameter : getParameters()) {
                if (PsiTreeUtil.isAncestor(lastParent, parameter, false))
                    continue;
                if (!parameter.processDeclarations(processor, state, lastParent, place)) return false;
            }
            return true;
        } else {
            // Show the function declaration
            return processor.execute(this, state);
        }
    }

    @Override
    public String toString() {
        return "Function Definition: " + getSignature();
    }
}
