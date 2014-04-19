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

package glslplugin.intentions.vectorcomponents;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import glslplugin.lang.elements.GLSLIdentifier;
import glslplugin.lang.elements.expressions.GLSLFieldSelectionExpression;
import glslplugin.lang.elements.types.GLSLVectorType;

import java.util.regex.Pattern;

public class VectorComponentsPredicate {

    static final Pattern xyzw = Pattern.compile("^[xyzw]{1,4}$");
    static final Pattern rgba = Pattern.compile("^[rgba]{1,4}$");
    static final Pattern stpq = Pattern.compile("^[stpq]{1,4}$");

    public boolean satisfiedBy(PsiElement psiElement) {

        if (psiElement instanceof GLSLIdentifier) {
            ASTNode node = psiElement.getNode();
            if (node != null) {
                PsiElement parent = node.getTreeParent().getPsi();
                if (parent instanceof GLSLFieldSelectionExpression) {
                    GLSLIdentifier identifier = (GLSLIdentifier) psiElement;

                    GLSLFieldSelectionExpression fse = (GLSLFieldSelectionExpression) parent;
                    if (fse.getLeftHandExpression().getType() instanceof GLSLVectorType) {

                        String parameters = identifier.getIdentifierName();
                        if (checkForMatch(parameters)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    boolean checkForMatch(String parameters) {
        return xyzw.matcher(parameters).matches() || rgba.matcher(parameters).matches() || stpq.matcher(parameters).matches();
    }
}
