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
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLIdentifier;
import glslplugin.lang.elements.GLSLReferenceElement;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.declarations.GLSLTypeDefinition;
import glslplugin.lang.elements.reference.GLSLFieldReference;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * GLSLMemberOperator is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 28, 2009
 *         Time: 4:40:28 PM
 */
public class GLSLFieldSelectionExpression extends GLSLSelectionExpressionBase implements GLSLReferenceElement {
    public GLSLFieldSelectionExpression(@NotNull ASTNode astNode) {
        super(astNode);
    }

    public GLSLIdentifier getMemberIdentifier() {
        PsiElement last = getLastChild();
        if (last instanceof GLSLIdentifier) {
            return (GLSLIdentifier) last;
        } else {
            throw new RuntimeException("Field selection operator missing identifier after '.'.");
        }
    }

    @Override
    public boolean isLValue() {
        // A member is L-Value only if its container also is L-Value
        return getLeftHandExpression().isLValue();
    }

    public String toString() {
        return "Field selection: '" + getMemberIdentifier().getIdentifierName() + "'";
    }

    public GLSLFieldReference getReferenceProxy() {
        GLSLDeclarator declarator = findDefiningDeclarator();
        if (declarator != null) {
            return new GLSLFieldReference(getMemberIdentifier(), declarator);
        }
        return null;
    }

    @Nullable
    private GLSLDeclarator findDefiningDeclarator() {
        GLSLExpression left = getLeftHandExpression();
        GLSLType type = left.getType();
        if (type == GLSLTypes.UNKNOWN_TYPE) {
            return null;
        }
        GLSLElement definition = type.getDefinition();
        if (definition instanceof GLSLTypeDefinition) {
            return ((GLSLTypeDefinition) definition).getDeclarator(getMemberIdentifier().getIdentifierName());
        } else {
            return null;
        }
    }

    @NotNull
    @Override
    public GLSLType getType() {
        GLSLDeclarator declarator = findDefiningDeclarator();
        if (declarator != null) {
            return declarator.getType();
        } else {
            // No declarator, check for built-in type
            GLSLExpression left = getLeftHandExpression();
            GLSLType type = left.getType();
            if (type == GLSLTypes.UNKNOWN_TYPE) {
                return GLSLTypes.UNKNOWN_TYPE;
            }
            if (!type.hasMembers()) {
                return GLSLTypes.INVALID_TYPE;
            }
            return type.getTypeOfMember(getMemberIdentifier().getIdentifierName());
        }
    }
}
