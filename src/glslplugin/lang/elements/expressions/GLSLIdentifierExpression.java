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
import com.intellij.util.IncorrectOperationException;
import glslplugin.lang.elements.GLSLIdentifier;
import glslplugin.lang.elements.GLSLReferenceElement;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.reference.GLSLVariableReference;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
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
        final GLSLDeclarator declaration = ref.resolve();
        if (declaration != null) {
            return declaration.getType();
        }
        return GLSLTypes.UNKNOWN_TYPE;
    }

    @Override
    @NotNull
    public GLSLVariableReference getReferenceProxy() {
        return new GLSLVariableReference(this.getNameIdentifier());
    }
}
