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
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.declarations.GLSLDeclaration;
import glslplugin.lang.elements.expressions.GLSLCondition;
import glslplugin.lang.elements.expressions.GLSLExpression;
import org.jetbrains.annotations.NotNull;

/**
 * GLSLDeclarationStatement is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 28, 2009
 *         Time: 6:13:58 PM
 */
public class GLSLForStatement extends GLSLStatement implements ConditionStatement {

    public GLSLForStatement(@NotNull ASTNode astNode) {
        super(astNode);
    }

    /**
     * Fetches the initialization, the condition and the counter elements and places them in an array.
     * The array will always have length 3 and the elements are always placed in their respective places.
     * They will be null if missing.
     *
     * @return an array containing the loop elements.
     */
    @NotNull
    private GLSLElement[] getForElements() {
        GLSLElement result[] = new GLSLElement[3];
        int numberOfSemicolonsPassed = 0;
        PsiElement current = getFirstChild();
        while (current != null) {
            ASTNode node = current.getNode();
            if (current instanceof GLSLExpression || current instanceof GLSLDeclaration) {
                result[numberOfSemicolonsPassed] = (GLSLElement) current;
            } else if (node != null) {
                if (node.getElementType() == GLSLTokenTypes.SEMICOLON) {
                    numberOfSemicolonsPassed++;
                }
                if (node.getElementType() == GLSLTokenTypes.RIGHT_PAREN) {
                    break;
                }
            }

            current = current.getNextSibling();
        }
        return result;
    }

    /**
     * Fetches the loop initialization element.
     * May be an GLSLExpression or GLSLVariableDeclaration subclass.
     *
     * @return the loop initialization element.
     */
    public GLSLElement getInitializerElement() {
        return getForElements()[0];
    }

    /**
     * Fetches the loop condition element.
     * May be an GLSLExpression or GLSLVariableDeclaration subclass.
     *
     * @return the loop condition element.
     */
    public GLSLCondition getCondition() {
        return (GLSLCondition) getForElements()[1];
    }

    /**
     * Fetches the loop counter expression.
     * It is always an GLSLExpression subclass, no declarations allowed here.
     *
     * @return the loop counter expression.
     */
    public GLSLExpression getCountExpression() {
        return (GLSLExpression) getForElements()[2];
    }


    public GLSLStatement getLoopStatement() {
        GLSLStatement statement = findChildByClass(GLSLStatement.class);
        assert statement != null;
        return statement;
    }


    @Override
    public String toString() {
        return "For Loop";
    }
}
