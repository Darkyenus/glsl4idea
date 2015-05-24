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

import java.util.logging.Logger;

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

    @Nullable
    public GLSLIdentifier getMemberIdentifier() {
        PsiElement last = getLastChild();
        if (last instanceof GLSLIdentifier) {
            return (GLSLIdentifier) last;
        } else {
            Logger.getLogger("GLSLFieldSelectionExpression").warning("Field selection operator missing identifier after '.'.");
            return null;
        }
    }

    @Override
    public boolean isLValue() {
        // A member is L-Value only if its container also is L-Value
        GLSLExpression leftExpression = getLeftHandExpression();
        //noinspection SimplifiableIfStatement
        if(leftExpression == null)return true; //It might be, but right now it is broken.
        else return leftExpression.isLValue();
    }

    @Nullable
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
        if(left == null)return null;
        GLSLType type = left.getType();
        if (type == GLSLTypes.UNKNOWN_TYPE) {
            return null;
        }
        GLSLElement definition = type.getDefinition();
        if (definition instanceof GLSLTypeDefinition) {
            GLSLIdentifier memberIdentifier = getMemberIdentifier();
            if(memberIdentifier == null){
                return null;
            }else{
                return ((GLSLTypeDefinition) definition).getDeclarator(memberIdentifier.getIdentifierName());
            }
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
            if(left == null)return GLSLTypes.UNKNOWN_TYPE;
            GLSLType type = left.getType();
            if (type == GLSLTypes.UNKNOWN_TYPE) {
                return GLSLTypes.UNKNOWN_TYPE;
            }
            if (!type.hasMembers()) {
                return GLSLTypes.INVALID_TYPE;
            }
            GLSLIdentifier memberIdentifier = getMemberIdentifier();
            if(memberIdentifier == null)return GLSLTypes.UNKNOWN_TYPE;
            else return type.getTypeOfMember(memberIdentifier.getIdentifierName());
        }
    }

    public String toString() {
        GLSLIdentifier memberIdentifier = getMemberIdentifier();
        if(memberIdentifier == null){
            return "Field selection: '(unknown)'";
        }else{
            return "Field selection: '" + memberIdentifier.getIdentifierName() + "'";
        }
    }
}
