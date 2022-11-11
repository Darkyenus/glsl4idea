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

package glslplugin.lang.elements.expressions;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.types.GLSLType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * GLSLParameterList is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 29, 2009
 *         Time: 10:36:17 AM
 */
public class GLSLParameterList extends GLSLElementImpl implements Iterable<GLSLExpression> {
    public GLSLParameterList(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @NotNull
    public GLSLExpression[] getParameters() {
        // convert the list of children to a list of GLSLStatement objects while performing sanity check.
        PsiElement[] children = getChildren();
        List<GLSLExpression> result = new ArrayList<>(children.length);
        for (PsiElement child : children) {
            if (child instanceof GLSLExpression) {
                result.add((GLSLExpression) child);
            }
        }
        return result.toArray(GLSLExpression.EMPTY_ARRAY);
    }

    @NotNull
    public GLSLType[] getParameterTypes() {
        // convert the list of children to a list of GLSLStatement objects while performing sanity check.
        PsiElement[] children = getChildren();
        List<GLSLType> result = new ArrayList<>(children.length);
        for (PsiElement child : children) {
            if (child instanceof GLSLExpression) {
                result.add(((GLSLExpression) child).getType());
            } else {
                final ASTNode node = child.getNode();
                if (node != null) {
                    final IElementType type = node.getElementType();
                    if (!GLSLTokenTypes.COMMENTS.contains(type)) {
                        Logger.getLogger("GLSLParameterList").warning("Parameter list contains non-comment, non-expression element.");
                    }
                }
            }
        }
        return result.toArray(GLSLType.EMPTY_ARRAY);
    }

    @NotNull
    @Override
    public Iterator<GLSLExpression> iterator() {
        return java.util.Arrays.asList(getParameters()).iterator();
    }

    @Override
    public String toString() {
        return "Parameter List (" + getParameters().length + " parameters)";
    }
}
