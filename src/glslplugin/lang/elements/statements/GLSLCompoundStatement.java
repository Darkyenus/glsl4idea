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
import org.jetbrains.annotations.NotNull;

/**
 * GLSLCompoundStatement is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 28, 2009
 *         Time: 6:00:00 PM
 */
public class GLSLCompoundStatement extends GLSLStatement {
    public GLSLCompoundStatement(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @NotNull
    public GLSLStatement[] getStatements() {
        GLSLStatement[] statements = PsiTreeUtil.getChildrenOfType(this, GLSLStatement.class);
        return (statements == null) ? GLSLStatement.NO_STATEMENTS : statements;
    }

    public String toString() {
        return "Compound Statement (" + getStatements().length + " statements)";
    }

    @NotNull
    @Override
    public TerminatorScope getTerminatorScope() {
        // The terminator scope of a compound statement is scope of the first terminating statement inside it.

        for (GLSLStatement statement : getStatements()) {
            TerminatorScope childScope = statement.getTerminatorScope();
            if (childScope != TerminatorScope.NONE) return childScope;
        }
        return TerminatorScope.NONE;
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
        PsiElement child = lastParent.getPrevSibling();
        while (child != null) {
            if (!child.processDeclarations(processor, state, lastParent, place)) return false;
            child = child.getPrevSibling();
        }
        return true;
    }
}
