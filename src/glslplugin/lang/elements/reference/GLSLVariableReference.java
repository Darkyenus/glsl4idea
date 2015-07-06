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

package glslplugin.lang.elements.reference;

import com.intellij.psi.PsiElement;
import glslplugin.lang.elements.declarations.GLSLDeclaration;
import glslplugin.lang.elements.declarations.GLSLDeclarationList;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.declarations.GLSLVariableDeclaration;
import glslplugin.lang.elements.expressions.GLSLIdentifierExpression;
import glslplugin.lang.elements.statements.GLSLDeclarationStatement;
import glslplugin.lang.parser.GLSLFile;
import org.jetbrains.annotations.Nullable;

/**
 * GLSLVariableReference is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 4, 2009
 *         Time: 1:29:50 AM
 */

public class GLSLVariableReference extends GLSLReferenceBase<GLSLIdentifierExpression, GLSLDeclarator> {
    public GLSLVariableReference(GLSLIdentifierExpression source) {
        super(source);
    }

    @Nullable
    public GLSLDeclarator resolve() {
        PsiElement current = source.getPrevSibling();
        GLSLDeclarator result = null;
        if (current == null) {
            current = source.getParent();
        }

        while (current != null) {

            // Only process it if we haven't already done so.
            if (current instanceof GLSLDeclarationList && !source.isDescendantOf(current)) {
                GLSLDeclarationList list = (GLSLDeclarationList) current;
                for (GLSLDeclaration declaration : list.getDeclarations()) {
                    result = getVariableReferenceCheckDeclaration(declaration);
                    if (result != null) {
                        break;
                    }
                }
            } else {
                GLSLVariableDeclaration declaration = null;

                if (current instanceof GLSLDeclarationStatement) {
                    declaration = ((GLSLDeclarationStatement) current).getDeclaration();
                }

                if (current instanceof GLSLVariableDeclaration) {
                    declaration = (GLSLVariableDeclaration) current;
                }

                if (declaration != null) {
                    result = getVariableReferenceCheckDeclaration(declaration);
                }
            }

            if (result != null) {
                return result;
            }

            if (current.getPrevSibling() == null) {
                current = current.getParent();
                if (current instanceof GLSLFile) {
                    current = null;
                }
            } else {
                current = current.getPrevSibling();
            }
        }

        return null;
    }

    @Nullable
    private GLSLDeclarator getVariableReferenceCheckDeclaration(GLSLDeclaration declaration) {
        for (GLSLDeclarator declarator : declaration.getDeclarators()) {
            if (declarator.getName().equals(source.getName())) {
                return declarator;
            }
        }
        return null;
    }
}
