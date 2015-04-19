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

    public GLSLStatement[] getStatements() {
        // convert the list of children to a list of GLSLStatement objects while performing sanity check.
        PsiElement[] children = getChildren();
        List<GLSLStatement> result = new ArrayList<GLSLStatement>(children.length);
        for (PsiElement child : children) {
            if (child instanceof GLSLStatement) {
                result.add((GLSLStatement) child);
            } else {
                final ASTNode node = child.getNode();
                if (node != null) {
                    final IElementType type = node.getElementType();
                    if (!GLSLTokenTypes.COMMENTS.contains(type)) {
                        throw new RuntimeException("Compound statement contains non-comment, non-statement element.");
                    }
                }
            }
        }
        return result.toArray(new GLSLStatement[result.size()]);
    }

    public String toString() {
        return "Compound Statement (" + getStatements().length + " statements)";
    }
}
