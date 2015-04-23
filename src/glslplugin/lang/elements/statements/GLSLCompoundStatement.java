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
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import glslplugin.lang.elements.GLSLTokenTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
        return (statements == null) ? new GLSLStatement[0] : statements;
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
}
