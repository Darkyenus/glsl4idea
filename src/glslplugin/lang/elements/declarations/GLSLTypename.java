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
import glslplugin.lang.elements.statements.GLSLDeclarationStatement;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import glslplugin.lang.parser.GLSLFile;
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
            final IElementType childType = child.getElementType();

            if (childType == GLSLTokenTypes.VOID_TYPE) return GLSLTypes.VOID;
            if (childType == GLSLTokenTypes.INT_TYPE) return GLSLTypes.INT;
            if (childType == GLSLTokenTypes.UINT_TYPE) return GLSLTypes.UINT;
            if (childType == GLSLTokenTypes.FLOAT_TYPE) return GLSLTypes.FLOAT;
            if (childType == GLSLTokenTypes.DOUBLE_TYPE) return GLSLTypes.DOUBLE;
            if (childType == GLSLTokenTypes.BOOL_TYPE) return GLSLTypes.BOOL;

            if (childType == GLSLTokenTypes.VEC2_TYPE) return GLSLTypes.VEC2;
            if (childType == GLSLTokenTypes.VEC3_TYPE) return GLSLTypes.VEC3;
            if (childType == GLSLTokenTypes.VEC4_TYPE) return GLSLTypes.VEC4;
            if (childType == GLSLTokenTypes.DVEC2_TYPE) return GLSLTypes.DVEC2;
            if (childType == GLSLTokenTypes.DVEC3_TYPE) return GLSLTypes.DVEC3;
            if (childType == GLSLTokenTypes.DVEC4_TYPE) return GLSLTypes.DVEC4;
            if (childType == GLSLTokenTypes.IVEC2_TYPE) return GLSLTypes.IVEC2;
            if (childType == GLSLTokenTypes.IVEC3_TYPE) return GLSLTypes.IVEC3;
            if (childType == GLSLTokenTypes.IVEC4_TYPE) return GLSLTypes.IVEC4;
            if (childType == GLSLTokenTypes.UVEC2_TYPE) return GLSLTypes.UVEC2;
            if (childType == GLSLTokenTypes.UVEC3_TYPE) return GLSLTypes.UVEC3;
            if (childType == GLSLTokenTypes.UVEC4_TYPE) return GLSLTypes.UVEC4;
            if (childType == GLSLTokenTypes.BVEC2_TYPE) return GLSLTypes.BVEC2;
            if (childType == GLSLTokenTypes.BVEC3_TYPE) return GLSLTypes.BVEC3;
            if (childType == GLSLTokenTypes.BVEC4_TYPE) return GLSLTypes.BVEC4;

            if (childType == GLSLTokenTypes.MAT2_TYPE) return GLSLTypes.MAT2;
            if (childType == GLSLTokenTypes.MAT3_TYPE) return GLSLTypes.MAT3;
            if (childType == GLSLTokenTypes.MAT4_TYPE) return GLSLTypes.MAT4;
            if (childType == GLSLTokenTypes.MAT2X2_TYPE) return GLSLTypes.MAT2x2;
            if (childType == GLSLTokenTypes.MAT2X3_TYPE) return GLSLTypes.MAT2x3;
            if (childType == GLSLTokenTypes.MAT2X4_TYPE) return GLSLTypes.MAT2x4;
            if (childType == GLSLTokenTypes.MAT3X2_TYPE) return GLSLTypes.MAT3x2;
            if (childType == GLSLTokenTypes.MAT3X3_TYPE) return GLSLTypes.MAT3x3;
            if (childType == GLSLTokenTypes.MAT3X4_TYPE) return GLSLTypes.MAT3x4;
            if (childType == GLSLTokenTypes.MAT4X2_TYPE) return GLSLTypes.MAT4x2;
            if (childType == GLSLTokenTypes.MAT4X3_TYPE) return GLSLTypes.MAT4x3;
            if (childType == GLSLTokenTypes.MAT4X4_TYPE) return GLSLTypes.MAT4x4;
            if (childType == GLSLTokenTypes.DMAT2_TYPE) return GLSLTypes.DMAT2;
            if (childType == GLSLTokenTypes.DMAT3_TYPE) return GLSLTypes.DMAT3;
            if (childType == GLSLTokenTypes.DMAT4_TYPE) return GLSLTypes.DMAT4;
            if (childType == GLSLTokenTypes.DMAT2X2_TYPE) return GLSLTypes.DMAT2x2;
            if (childType == GLSLTokenTypes.DMAT2X3_TYPE) return GLSLTypes.DMAT2x3;
            if (childType == GLSLTokenTypes.DMAT2X4_TYPE) return GLSLTypes.DMAT2x4;
            if (childType == GLSLTokenTypes.DMAT3X2_TYPE) return GLSLTypes.DMAT3x2;
            if (childType == GLSLTokenTypes.DMAT3X3_TYPE) return GLSLTypes.DMAT3x3;
            if (childType == GLSLTokenTypes.DMAT3X4_TYPE) return GLSLTypes.DMAT3x4;
            if (childType == GLSLTokenTypes.DMAT4X2_TYPE) return GLSLTypes.DMAT4x2;
            if (childType == GLSLTokenTypes.DMAT4X3_TYPE) return GLSLTypes.DMAT4x3;
            if (childType == GLSLTokenTypes.DMAT4X4_TYPE) return GLSLTypes.DMAT4x4;

            if (childType == GLSLTokenTypes.SAMPLER1D_TYPE) return GLSLTypes.SAMPLER1D;
            if (childType == GLSLTokenTypes.SAMPLER2D_TYPE) return GLSLTypes.SAMPLER2D;
            if (childType == GLSLTokenTypes.SAMPLER3D_TYPE) return GLSLTypes.SAMPLER3D;
            if (childType == GLSLTokenTypes.SAMPLERCUBE_TYPE) return GLSLTypes.SAMPLER_CUBE;
            if (childType == GLSLTokenTypes.SAMPLER1DSHADOW_TYPE) return GLSLTypes.SAMPLER1D_SHADOW;
            if (childType == GLSLTokenTypes.SAMPLER2DSHADOW_TYPE) return GLSLTypes.SAMPLER2D_SHADOW;
        }

        Logger.getLogger("GLSLTypename").warning("Unknown element type: '" + type + "'");
        return GLSLTypes.UNKNOWN_TYPE;
    }

    @Nullable
    public GLSLDeclarationList getDeclarationList() {
        final GLSLTypeDefinition definition = getTypeDefinition();
        if (definition != null) {
            return definition.getDeclarationList();
        } else {
            return null;
        }
    }

    @Nullable
    public GLSLTypeReference getReferenceProxy() {
        GLSLTypeDefinition definition = findTypeReference();
        if (definition != null) {
            return new GLSLTypeReference(this, definition);
        } else {
            return null;
        }
    }

    @Nullable
    private GLSLTypeDefinition findTypeReference() {
        PsiElement current = getPrevSibling();
        GLSLTypeDefinition result = null;
        if (current == null) {
            current = getParent();
        }

        while (current != null) {

            // Only process it if we haven't already done so.
            if (current instanceof GLSLDeclarationList && !isDescendantOf(current)) {
                GLSLDeclarationList list = (GLSLDeclarationList) current;
                for (GLSLDeclaration declaration : list.getDeclarations()) {
                    result = checkDeclarationForType(declaration);
                    if (result != null) {
                        break;
                    }
                }
            } else {
                GLSLDeclaration declaration = null;

                if (current instanceof GLSLDeclarationStatement) {
                    if (!isDescendantOf(current)) {
                        declaration = ((GLSLDeclarationStatement) current).getDeclaration();
                    }
                }

                if (current instanceof GLSLDeclaration && !(current instanceof GLSLFunctionDeclaration)) {
                    // Do not check if this is contained in the declaration.
                    if (!isDescendantOf(current)) {
                        declaration = (GLSLDeclaration) current;
                    }
                }

                if (declaration != null) {
                    result = checkDeclarationForType(declaration);
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
    private GLSLTypeDefinition checkDeclarationForType(GLSLDeclaration declaration) {
        final GLSLTypeSpecifier specifier = declaration.getTypeSpecifierNode();
        if(specifier == null)return null;
        GLSLTypedElement definition = specifier.getTypeDefinition();
        if (definition instanceof GLSLTypeDefinition) {
            GLSLTypeDefinition typedef = (GLSLTypeDefinition) definition;
            if (typedef.isNamed() && typedef.getTypeName().equals(getTypename())) {
                return typedef;
            }
        }
        return null;
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
