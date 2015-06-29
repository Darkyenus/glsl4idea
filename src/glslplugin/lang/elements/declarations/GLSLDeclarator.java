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
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.GLSLIdentifier;
import glslplugin.lang.elements.expressions.GLSLExpression;
import glslplugin.lang.elements.types.GLSLArrayType;
import glslplugin.lang.elements.types.GLSLQualifiedType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * GLSLDeclarator represents a local or global variable declaration.
 * It may contain one or more declarators.
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 29, 2009
 *         Time: 7:29:46 PM
 */
public class GLSLDeclarator extends GLSLElementImpl {
    public static final GLSLDeclarator[] NO_DECLARATORS = new GLSLDeclarator[0];

    public GLSLDeclarator(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    public GLSLExpression getInitializerExpression() {
        final GLSLInitializer init = findChildByClass(GLSLInitializer.class);
        if (init != null) {
            return init.getInitializerExpression();
        } else {
            return null;
        }
    }

    @Nullable
    public GLSLIdentifier getIdentifier() {
        PsiElement idElement = getFirstChild();
        if (idElement instanceof GLSLIdentifier) {
            return (GLSLIdentifier) idElement;
        } else {
            return null; //May trigger on malformed code
        }
    }

    @NotNull
    public String getIdentifierName() {
        PsiElement idElement = getFirstChild();
        if (idElement instanceof GLSLIdentifier) {
            return ((GLSLIdentifier) idElement).getIdentifierName();
        } else {
            return "(anonymous)";
        }
    }

    @Nullable
    public GLSLDeclaration getParentDeclaration() {
        return findParentByClass(GLSLDeclarationImpl.class);
    }

    @NotNull
    public GLSLType getType() {
        GLSLDeclaration declaration = getParentDeclaration();
        if(declaration == null)return GLSLTypes.UNKNOWN_TYPE;
        GLSLTypeSpecifier declarationType = declaration.getTypeSpecifierNode();
        if(declarationType == null)return GLSLTypes.UNKNOWN_TYPE;

        GLSLType declaredType = declarationType.getType();
        if(!declaredType.isValidType())return GLSLTypes.UNKNOWN_TYPE;

        GLSLArraySpecifier[] arraySpecifiers = findChildrenByClass(GLSLArraySpecifier.class);
        if(arraySpecifiers.length == 0){
            return declaredType;
        }else{
            //Must append some dimensions to the type
            if(declaredType instanceof GLSLArrayType){
                //Already an array, must append the dimensions
                GLSLArrayType declaredArrayType = (GLSLArrayType) declaredType;
                int[] existingDimensions = declaredArrayType.getDimensions();
                int[] combinedDimensions = new int[existingDimensions.length + arraySpecifiers.length];
                System.arraycopy(existingDimensions, 0, combinedDimensions, 0, existingDimensions.length);
                for (int i = 0; i < arraySpecifiers.length; i++) {
                    combinedDimensions[i + existingDimensions.length] = arraySpecifiers[i].getDimensionSize();
                }
                return new GLSLArrayType(declaredArrayType.getBaseType(), combinedDimensions);
            }else{
                int[] dimensions = new int[arraySpecifiers.length];
                for (int i = 0; i < dimensions.length; i++) {
                    dimensions[i] = arraySpecifiers[i].getDimensionSize();
                }
                return new GLSLArrayType(declaredType, dimensions);
            }
        }
    }

    @NotNull
    public GLSLQualifiedType getQualifiedType() {
        final GLSLType type = getType();
        final GLSLDeclaration declaration = getParentDeclaration();
        if(declaration == null || declaration.getQualifierList() == null)return new GLSLQualifiedType(type);
        return new GLSLQualifiedType(type, declaration.getQualifierList().getQualifiers());
    }

    @Override
    public String toString() {
        return "Declarator: " + getIdentifierName() + " : " + getType().getTypename();
    }
}
