/*
 * Copyright 2010 Jean-Paul Balabanian and Yngve Devik Hammersland
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
import com.intellij.psi.tree.TokenSet;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an expression of the form `condition ? trueBranch : falseBranch`.
 * @author abigail
 */
public class GLSLConditionalExpression extends GLSLExpression {

    private static final TokenSet QUESTION = TokenSet.create(GLSLTokenTypes.QUESTION);
    private static final TokenSet COLON = TokenSet.create(GLSLTokenTypes.COLON);

    public GLSLConditionalExpression(@NotNull ASTNode astNode) {
        super(astNode);
    }

    // For robustness, we find each of the parts of the conditional by starting at the question mark
    // or the colon and walking left or right.
    @Nullable
    public GLSLExpression getCondition() {
        PsiElement condition = findChildByType(QUESTION);
        while (condition != null && !(condition instanceof GLSLExpression)) {
            condition = condition.getPrevSibling();
        }
        return (GLSLExpression) condition;
    }

    @Nullable
    public GLSLExpression getTrueBranch() {
        PsiElement condition = findChildByType(QUESTION);
        while (condition != null && !(condition instanceof GLSLExpression)) {
            condition = condition.getNextSibling();
        }
        return (GLSLExpression) condition;
    }

    @Nullable
    public GLSLExpression getFalseBranch() {
        PsiElement condition = findChildByType(COLON);
        while (condition != null && !(condition instanceof GLSLExpression)) {
            condition = condition.getNextSibling();
        }
        return (GLSLExpression) condition;
    }

    @NotNull
    @Override
    public GLSLType getType() {
        final GLSLExpression trueBranch = getTrueBranch();
        final GLSLType trueType = trueBranch != null ? trueBranch.getType() : GLSLTypes.UNKNOWN_TYPE;
        final GLSLExpression falseBranch = getFalseBranch();
        final GLSLType falseType = falseBranch != null ? falseBranch.getType() : GLSLTypes.UNKNOWN_TYPE;

        return GLSLType.unifyTypes(trueType, falseType);
    }
}
