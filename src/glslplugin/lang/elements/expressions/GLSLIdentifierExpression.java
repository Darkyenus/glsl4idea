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
import glslplugin.lang.elements.GLSLIdentifier;
import glslplugin.lang.elements.GLSLReferenceElement;
import glslplugin.lang.elements.declarations.*;
import glslplugin.lang.elements.reference.GLSLVariableReference;
import glslplugin.lang.elements.statements.GLSLDeclarationStatement;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import glslplugin.lang.parser.GLSLFile;
import org.jetbrains.annotations.NotNull;

/**
 * GLSLIdentifierExpression is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 4, 2009
 *         Time: 12:16:41 AM
 */
public class GLSLIdentifierExpression extends GLSLExpression implements GLSLReferenceElement {
    public GLSLIdentifierExpression(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Override
    public boolean isLValue() {
        // TODO: check for read-only-ness in the declaration qualifiers
        return true;
    }

    public GLSLIdentifier getIdentifier() {
        GLSLIdentifier id = findChildByClass(GLSLIdentifier.class);
        assert id != null;
        return id;
    }

    public String getIdentifierName() {
        return getIdentifier().getIdentifierName();
    }

    @Override
    public String toString() {
        return "Identifier Expression: " + getIdentifierName();
    }

    @NotNull
    @Override
    public GLSLType getType() {
        GLSLVariableReference ref = getReferenceProxy();
        if (ref != null) {
            final GLSLDeclarator declaration = ref.resolve();
            if (declaration != null) {
                return declaration.getType();
            }
        }
        return GLSLTypes.UNKNOWN_TYPE;
    }

    public GLSLVariableReference getReferenceProxy() {
        GLSLDeclarator target = getVariableReference(this);

        if (target != null) {
            return new GLSLVariableReference(this, target);
        } else {
            return null;
        }
    }

    private GLSLDeclarator getVariableReference(PsiElement start) {
        PsiElement current = start.getPrevSibling();
        GLSLDeclarator result = null;
        if (current == null) {
            current = start.getParent();
        }

        while (current != null) {

            // Only process it if we haven't already done so.
            if (current instanceof GLSLDeclarationList && !isDescendantOf(current)) {
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

    private GLSLDeclarator getVariableReferenceCheckDeclaration(GLSLDeclaration declaration) {
        for (GLSLDeclarator declarator : declaration.getDeclarators()) {
            if (declarator.getIdentifierName().equals(getIdentifierName())) {
                return declarator;
            }
        }
        return null;
    }

}
