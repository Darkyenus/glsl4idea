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
import glslplugin.lang.elements.statements.GLSLCompoundStatement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * NewFunctionDefinition is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 2, 2009
 *         Time: 12:32:21 PM
 */
public class GLSLFunctionDefinitionImpl extends GLSLFunctionDeclarationImpl implements GLSLFunctionDefinition {
    public GLSLFunctionDefinitionImpl(ASTNode node) {
        super(node);
    }

    @Nullable
    public GLSLCompoundStatement getBody() {
        return findChildByClass(GLSLCompoundStatement.class);
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, @Nullable PsiElement lastParent, @NotNull PsiElement place) {
        if (lastParent == null) {
            // Do not show declarations of parameters to outside scopes
            return true;
        }

        for (GLSLParameterDeclaration parameter : getParameters()) {
            if (parameter == lastParent) { // TODO(jp): sloppy, parameter is probably not direct child, so this will fail
                continue;
            }
            if (!parameter.processDeclarations(processor, state, lastParent, place)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return "Function Definition: " + getSignature();
    }
}
