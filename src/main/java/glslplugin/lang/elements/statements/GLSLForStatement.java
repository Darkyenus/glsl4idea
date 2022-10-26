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
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.expressions.GLSLCondition;
import glslplugin.lang.elements.expressions.GLSLExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * GLSLDeclarationStatement is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 28, 2009
 *         Time: 6:13:58 PM
 */
public class GLSLForStatement extends GLSLStatement implements LoopStatement {

    public GLSLForStatement(@NotNull ASTNode astNode) {
        super(astNode);
    }


    private @Nullable GLSLElement @Nullable[] parts;

    /**
     * Fetches the initialization, the condition and the counter elements and places them in an array.
     * The array will always have length 3 and the elements are always placed in their respective places.
     * They will be null if missing.
     *
     * @return an array containing the loop elements.
     */
    @NotNull
    private @Nullable GLSLElement @NotNull[] getForElements() {
        final GLSLElement[] currentParts = this.parts;
        if (currentParts != null) {
            return currentParts;
        }

        GLSLElement[] result = this.parts = new GLSLElement[4];

        // Get to the opening brace
        PsiElement current = findChildByType(GLSLTokenTypes.LEFT_PAREN);

        lookingForRightParen: do {
            // Skip it and any whitespace and comments
            while (current != null) {
                current = current.getNextSibling();

                if (current instanceof GLSLExpressionStatement || current instanceof GLSLDeclarationStatement) {
                    result[0] = (GLSLElement) current;
                    break;
                } else if (current instanceof LeafPsiElement leaf) {
                    if (leaf.getElementType() == GLSLTokenTypes.SEMICOLON) {
                        // No initializer
                        break;
                    } else if (leaf.getElementType() == GLSLTokenTypes.RIGHT_PAREN) {
                        // End of for header
                        break lookingForRightParen;
                    }
                }
            }

            // current is null or initializer
            // find condition
            while (current != null) {
                current = current.getNextSibling();

                if (result[1] == null && current instanceof GLSLCondition cond) {
                    result[1] = cond;
                    //break; no break, there must be a semicolon after the condition
                } else if (current instanceof LeafPsiElement leaf) {
                    if (leaf.getElementType() == GLSLTokenTypes.SEMICOLON) {
                        // After condition
                        break;
                    } else if (leaf.getElementType() == GLSLTokenTypes.RIGHT_PAREN) {
                        // End of for header
                        break lookingForRightParen;
                    }
                }
            }

            // current is null or condition semicolon
            while (current != null) {
                current = current.getNextSibling();

                if (current instanceof GLSLExpression expr) {
                    result[2] = expr;
                    //break; no break, there must be a right paren after the counter
                } else if (current instanceof LeafPsiElement leaf && leaf.getElementType() == GLSLTokenTypes.RIGHT_PAREN) {
                    // End of for header
                    break lookingForRightParen;
                }
            }

        } while(true);

        // current is null or right paren
        while (current != null) {
            current = current.getNextSibling();

            if (current instanceof GLSLStatement stat) {
                result[3] = stat;
                break;
            }
        }

        return result;
    }

    /**
     * Fetches the loop initialization element.
     * May be an GLSLExpression or GLSLVariableDeclaration subclass.
     *
     * @return the loop initialization element.
     */
    @Nullable
    public GLSLElement getInitializerElement() {
        return getForElements()[0];
    }

    /**
     * Fetches the loop condition element.
     * May be an GLSLExpression or GLSLVariableDeclaration subclass.
     *
     * @return the loop condition element.
     */
    @Nullable
    public GLSLCondition getCondition() {
        return (GLSLCondition) getForElements()[1];
    }

    /**
     * Fetches the loop counter expression.
     * It is always an GLSLExpression subclass, no declarations allowed here.
     *
     * @return the loop counter expression.
     */
    @Nullable
    public GLSLExpression getCountExpression() {
        return (GLSLExpression) getForElements()[2];
    }

    @Override
    @Nullable
    public GLSLStatement getBody() {
        return (GLSLStatement) getForElements()[3];
    }

    @Override
    public String toString() {
        return "For Loop";
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
        boolean fromInside = PsiTreeUtil.isAncestor(this, place, false);
        if (!fromInside) return true;
        // Variable declarations are only visible from inside

        final GLSLElement initializer = getInitializerElement();
        if (initializer != null && !PsiTreeUtil.isAncestor(lastParent, initializer, false) && !initializer.processDeclarations(processor, state, null, place)) return false;

        final GLSLCondition condition = getCondition();
        if (condition != null && !PsiTreeUtil.isAncestor(lastParent, condition, false) && !condition.processDeclarations(processor, state, null, place)) return false;

        return true;
    }

    // TODO some for statements can be terminating if their condition can be constant-analyzed as true
    // and don't contain any sort of break out. But that should probably be an error.
}
