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
import com.intellij.psi.PsiErrorElement;
import com.intellij.psi.tree.IElementType;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.GLSLTokenTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * NewDeclarationList is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 29, 2009
 *         Time: 4:59:57 PM
 */
public class GLSLDeclarationList extends GLSLElementImpl {
    public GLSLDeclarationList(ASTNode node) {
        super(node);
    }

    @NotNull
    public GLSLDeclaration[] getDeclarations() {
        // convert the list of children to a list of GLSLStatement objects while performing sanity check.
        PsiElement[] children = getChildren();
        List<GLSLDeclaration> result = new ArrayList<>(children.length);
        for (PsiElement child : children) {
            if (child instanceof GLSLDeclaration) {
                result.add((GLSLDeclaration) child);
            } else if (!(child instanceof PsiErrorElement)) {
                final ASTNode node = child.getNode();
                if (node != null) {
                    final IElementType type = node.getElementType();
                    if (!GLSLTokenTypes.COMMENTS.contains(type)) {
                        Logger.getLogger("GLSLDeclarationList").warning("Parameter declaration list contains non-comment, non-expression element. ("+type+")");
                    }
                }
            }
        }
        return result.toArray(GLSLDeclaration.NO_DECLARATIONS);
    }

    @Override
    public String toString() {
        return "Declaration List (" + getDeclarations().length + " parameters)";
    }
}
