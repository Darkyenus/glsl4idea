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

import com.intellij.psi.util.PsiTreeUtil;
import glslplugin.lang.elements.expressions.GLSLCondition;
import org.jetbrains.annotations.NotNull;
import com.intellij.lang.ASTNode;

/**
 * GLSLDeclarationStatement is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 28, 2009
 *         Time: 6:13:58 PM
 */
public class GLSLIfStatement extends GLSLStatement implements ConditionStatement {
    public GLSLIfStatement(@NotNull ASTNode astNode) {
        super(astNode);
    }

    // TODO: Implement

    @Override
    public String toString() {
        return "If Statement";
    }

    public GLSLCondition getCondition() {
        GLSLCondition condition = findChildByClass(GLSLCondition.class);
        assert condition != null;
        return condition;
    }

    @NotNull
    @Override
    public TerminatorScope getTerminatorScope() {
        // The terminator scope of an if statement is NONE if it only has one branch, otherwise it's the minimum
        // terminator scope of its two branches.
        GLSLStatement[] branches = PsiTreeUtil.getChildrenOfType(this, GLSLStatement.class);
        if (branches == null || branches.length < 2) {
            return TerminatorScope.NONE;
        }

        TerminatorScope minScope = TerminatorScope.SHADER;
        for (GLSLStatement statement : branches) {
            TerminatorScope childScope = statement.getTerminatorScope();
            minScope = minScope.compareTo(childScope) < 0 ? minScope : childScope;
        }
        return minScope;
    }
}
