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
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.GLSLTypedElement;
import glslplugin.lang.elements.types.GLSLArrayType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 28, 2009
 *         Time: 11:21:30 PM
 */
public class GLSLTypeSpecifier extends GLSLElementImpl {
    public GLSLTypeSpecifier(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @NotNull
    public GLSLType getType() {
        // GLSLTypedElement is either a type definition or type reference
        GLSLTypedElement reference = findChildByClass(GLSLTypedElement.class);
        GLSLArraySpecifier[] arrayDimensions = findChildrenByClass(GLSLArraySpecifier.class);
        if (reference != null) {
            final GLSLType type = reference.getType();
            if(arrayDimensions.length == 0){
                //It is not an array type
                return type;
            }else{
                //It is an array type
                int[] dimensions = new int[arrayDimensions.length];
                for (int i = 0; i < dimensions.length; i++) {
                    dimensions[i] = arrayDimensions[i].getDimensionSize();
                }
                return new GLSLArrayType(type, dimensions);
            }
        } else {
            return GLSLTypes.UNKNOWN_TYPE;
        }
    }

    /**
     * @return true if this type specifier actually defines its own type (as possible with structs)
     */
    public boolean isTypeDeclaration(){
        final GLSLTypedElement reference = findChildByClass(GLSLTypedElement.class);
        return reference instanceof GLSLTypeDefinition;
    }

    @NotNull
    public String getTypeName() {
        return getType().getTypename();
    }

    @Override
    public String toString() {
        return "Type Specifier: " + getTypeName();
    }

    @Nullable
    public GLSLTypeDefinition getTypeDefinition() {
        return findChildByClass(GLSLTypeDefinition.class);
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, @Nullable PsiElement lastParent, @NotNull PsiElement place) {
        GLSLTypeDefinition typeDefinition = getTypeDefinition();
        if (typeDefinition != null && !typeDefinition.processDeclarations(processor, state, lastParent, place)) return false;
        return true;
    }
}
