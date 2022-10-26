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

package glslplugin.lang.elements.statements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import glslplugin.lang.elements.expressions.GLSLCondition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * GLSLDeclarationStatement is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 28, 2009
 *         Time: 6:13:58 PM
 */
public class GLSLWhileStatement extends GLSLStatement implements LoopStatement {
    public GLSLWhileStatement(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    public GLSLCondition getCondition() {
        return findChildByClass(GLSLCondition.class);
    }

    @Override
    @Nullable
    public GLSLStatement getBody() {
        return findChildByClass(GLSLStatement.class);
    }

    @Override
    public String toString() {
        return "While Loop";
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
        boolean fromInside = PsiTreeUtil.isAncestor(this, place, false);
        if (!fromInside) return true;
        // Variable declarations are only visible from inside

        final GLSLCondition condition = getCondition();
        if (condition == null || PsiTreeUtil.isAncestor(lastParent, condition, false)) return true;

        return condition.processDeclarations(processor, state, null, place);
    }

    // TODO some while statements can be terminating if their condition can be constant-analyzed as true
}
