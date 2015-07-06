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
import glslplugin.lang.elements.types.GLSLVectorType;
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
        else {
            if(isSwizzle()){
                //It is a swizzle, so it may or may not be a L value
                //If any of the components are repeated, it is not a L value
                GLSLIdentifier memberIdentifier = getMemberIdentifier();
                if(memberIdentifier == null)return true; //This should not happen
                String components = memberIdentifier.getName();
                if(components == null)return true; // This absolutely shouldn't happen
                for (int i = 0; i < components.length(); i++) {
                    char c = components.charAt(i);
                    for (int j = i+1; j < components.length(); j++) {
                        if(c == components.charAt(j)){
                            //When any component is repeated, it is not a L value
                            return false;
                        }
                    }
                }
                return true;
            }else{
                return leftExpression.isLValue();
            }
        }
    }

    /**
     * Reports whether this selection operates on vector and selects more than one component.
     * Returns false if it failed to find out.
     */
    public boolean isSwizzle(){
        GLSLExpression leftHandExpression = getLeftHandExpression();
        GLSLIdentifier memberIdentifier = getMemberIdentifier();
        if(leftHandExpression == null || memberIdentifier == null)return false;
        GLSLType leftHandType = leftHandExpression.getType();
        if(!leftHandType.isValidType())return false;
        if(!(leftHandType instanceof GLSLVectorType))return false;
        //If it got here, it is picking a component(s) from a vector. But how many?
        return memberIdentifier.getName().length() > 1;
        //Because all vector components are marked as only one letter, having more letters means swizzling
    }

    @NotNull
    public GLSLFieldReference getReferenceProxy() {
        return new GLSLFieldReference(this);
    }

    @NotNull
    @Override
    public GLSLType getType() {
        GLSLDeclarator declarator = getReferenceProxy().resolve();
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
                return GLSLTypes.UNKNOWN_TYPE;
            }
            GLSLIdentifier memberIdentifier = getMemberIdentifier();
            if(memberIdentifier == null)return GLSLTypes.UNKNOWN_TYPE;
            else return type.getMemberType(memberIdentifier.getName());
        }
    }

    public String toString() {
        GLSLIdentifier memberIdentifier = getMemberIdentifier();
        if(memberIdentifier == null){
            return "Field selection: '(unknown)'";
        }else{
            return "Field selection: '" + memberIdentifier.getName() + "'";
        }
    }
}
