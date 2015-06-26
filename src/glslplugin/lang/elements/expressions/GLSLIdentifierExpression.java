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
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import glslplugin.lang.elements.GLSLIdentifier;
import glslplugin.lang.elements.GLSLReferenceElement;
import glslplugin.lang.elements.declarations.*;
import glslplugin.lang.elements.reference.GLSLVariableReference;
import glslplugin.lang.elements.statements.GLSLDeclarationStatement;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import glslplugin.lang.parser.GLSLFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * GLSLIdentifierExpression is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 4, 2009
 *         Time: 12:16:41 AM
 */
public class GLSLIdentifierExpression extends GLSLExpression implements GLSLReferenceElement, PsiNameIdentifierOwner {
    public GLSLIdentifierExpression(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Override
    public boolean isLValue() {
        // TODO: check for read-only-ness in the declaration qualifiers
        return true;
    }

    @Override
    @Nullable
    public GLSLIdentifier getNameIdentifier() {
        return findChildByClass(GLSLIdentifier.class);
    }

    @Override
    public String getName() {
        GLSLIdentifier identifier = getNameIdentifier();
        if(identifier != null){
            return identifier.getName();
        }else{
            return "(unknown)";
        }
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        GLSLIdentifier identifier = getNameIdentifier();
        if (identifier != null) {
            return identifier.setName(name);
        } else {
            throw new IncorrectOperationException("Declarator with no name!");
        }
    }

    @Override
    public String toString() {
        return "Identifier Expression: " + getName();
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

    @Nullable
    public GLSLVariableReference getReferenceProxy() {
        GLSLDeclarator target = getVariableReference(this);

        if (target != null) {
            return new GLSLVariableReference(this, target);
        } else {
            return null;
        }
    }

    @Nullable
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

    @Nullable
    private GLSLDeclarator getVariableReferenceCheckDeclaration(GLSLDeclaration declaration) {
        for (GLSLDeclarator declarator : declaration.getDeclarators()) {
            if (declarator.getName().equals(getName())) {
                return declarator;
            }
        }
        return null;
    }

}
