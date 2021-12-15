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
import org.jetbrains.annotations.Nullable;

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
        GLSLElement[] result = new GLSLElement[3];
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
    @Nullable
    public GLSLElement getInitializerElement() {
        GLSLElement[] forElements = getForElements();
        if(forElements.length > 0){
            return forElements[0];
        }else return null;
    }

    /**
     * Fetches the loop condition element.
     * May be an GLSLExpression or GLSLVariableDeclaration subclass.
     *
     * @return the loop condition element.
     */
    @Nullable
    public GLSLCondition getCondition() {
        GLSLElement[] forElements = getForElements();
        if(forElements.length > 1 && forElements[1] instanceof GLSLCondition){
            return (GLSLCondition) forElements[1];
        }else return null;
    }

    /**
     * Fetches the loop counter expression.
     * It is always an GLSLExpression subclass, no declarations allowed here.
     *
     * @return the loop counter expression.
     */
    @Nullable
    public GLSLExpression getCountExpression() {
        GLSLElement[] forElements = getForElements();
        if(forElements.length > 2 && forElements[2] instanceof GLSLExpression){
            return (GLSLExpression) forElements[2];
        }else return null;
    }

    @Nullable
    public GLSLStatement getLoopStatement() {
        return findChildByClass(GLSLStatement.class);
    }


    @Override
    public String toString() {
        return "For Loop";
    }

    // TODO some for statements can be terminating if their condition can be constant-analyzed as true
    // and don't contain any sort of break out. But that should probably be an error.
}
