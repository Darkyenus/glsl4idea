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
import com.intellij.psi.PsiReference;
import com.intellij.psi.tree.IElementType;
import glslplugin.lang.elements.*;
import glslplugin.lang.elements.reference.GLSLTypeReference;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

/**
 * GLSLTypeReference represents a type-specifier which specifies a custom type.
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 5, 2009
 *         Time: 9:54:19 PM
 */
public class GLSLTypename extends GLSLElementImpl implements GLSLTypedElement, GLSLReferenceElement {

    public GLSLTypename(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    public GLSLTypeDefinition getTypeDefinition() {
        GLSLIdentifier id = findChildByClass(GLSLIdentifier.class);
        if(id == null)return null;
        PsiReference ref = id.getReference();
        if (ref != null) {
            PsiElement elt = ref.resolve();
            return (GLSLTypeDefinition) elt;
        } else {
            // Failed to resolve the type.
            return null;
        }
    }

    @NotNull
    public GLSLType getType() {
        final ASTNode node = getNode();
        final IElementType type = node.getElementType();

        if (type == GLSLElementTypes.TYPE_SPECIFIER_STRUCT_REFERENCE) {
            GLSLTypeDefinition def = getTypeDefinition();
            if (def != null) {
                return def.getType();
            } else {
                // TODO: Check built-in structures
                return GLSLTypes.getUndefinedType(getText());
            }
        }

        if (type == GLSLElementTypes.TYPE_SPECIFIER_PRIMITIVE) {
            final ASTNode child = node.getFirstChildNode();
            GLSLType t = GLSLTypes.getTypeFromName(child.getText());
            if (t != null) return t;
            return GLSLTypes.UNKNOWN_TYPE;
        }

        Logger.getLogger("GLSLTypename").warning("Unknown element type: '" + type + "'");
        return GLSLTypes.UNKNOWN_TYPE;
    }

    @NotNull
    public GLSLTypeReference getReferenceProxy() {
        return new GLSLTypeReference(this);
    }

    @Override
    public String toString() {
        return "Struct Reference: '" + getTypename() + "'";
    }

    @NotNull
    public GLSLDeclaration[] getDeclarations() {
        final GLSLTypeDefinition definition = getTypeDefinition();
        if (definition != null) {
            return definition.getDeclarations();
        } else {
            return GLSLDeclaration.NO_DECLARATIONS;
        }
    }

    @NotNull
    public GLSLDeclarator[] getDeclarators() {
        final GLSLTypeDefinition definition = getTypeDefinition();
        if (definition != null) {
            return definition.getDeclarators();
        } else {
            return GLSLDeclarator.NO_DECLARATORS;
        }
    }

    @NotNull
    public String getTypename() {
        return getText();
    }
}
