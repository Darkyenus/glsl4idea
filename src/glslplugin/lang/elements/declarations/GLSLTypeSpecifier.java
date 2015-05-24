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

    @Nullable
    public GLSLArraySpecifier getArraySpecifierNode() {
        PsiElement[] children = getChildren();
        if (children.length == 2) {
            final PsiElement array = children[1];
            if (array instanceof GLSLArraySpecifier) {
                return ((GLSLArraySpecifier) array);
            }
        }
        return null;
    }

    @NotNull
    public GLSLType getType() {
        // GLSLTypedElement is either a type definition or type reference
        GLSLTypedElement reference = findChildByClass(GLSLTypedElement.class);
        GLSLArraySpecifier array = getArraySpecifierNode();
        if (reference != null) {
            final GLSLType type = reference.getType();
            if (array != null) {
                return new GLSLArrayType(type, array);
            }
            return type;
        } else {
            return GLSLTypes.UNKNOWN_TYPE;
        }
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
    public GLSLTypedElement getTypeDefinition() {
        return findChildByClass(GLSLTypeDefinition.class);
    }
}
