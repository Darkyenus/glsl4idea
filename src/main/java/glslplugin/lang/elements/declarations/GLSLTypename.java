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
import com.intellij.psi.util.PsiTreeUtil;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.GLSLTypedElement;
import glslplugin.lang.elements.reference.GLSLAbstractReference;
import glslplugin.lang.elements.reference.GLSLBuiltInPsiUtilService;
import glslplugin.lang.elements.reference.GLSLReferencingElement;
import glslplugin.lang.elements.types.GLSLMatrixType;
import glslplugin.lang.elements.types.GLSLOpaqueType;
import glslplugin.lang.elements.types.GLSLScalarType;
import glslplugin.lang.elements.types.GLSLStructType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import glslplugin.lang.elements.types.GLSLVectorType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Represents a type-specifier which specifies a built-in or custom type.
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 5, 2009
 *         Time: 9:54:19 PM
 */
public class GLSLTypename extends GLSLElementImpl implements GLSLTypedElement, GLSLReferencingElement {

    public GLSLTypename(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Override
    public @Nullable PsiElement getReferencingIdentifierForRenaming() {
        return findChildByType(GLSLTokenTypes.IDENTIFIER);
    }

    @Nullable
    public String getReferencedTypeName() {
        return GLSLElement.text(getReferencingIdentifierForRenaming());
    }

    /** If this refers to a struct, return its definition. */
    @Nullable
    public GLSLStructDefinition getStructDefinition() {
        final GLSLType type = getReference().resolveType();
        if (type instanceof GLSLStructType) {
            return ((GLSLStructType) type).getDefinition();
        }
        return null;
    }

    @NotNull
    public GLSLType getType() {
        return getReference().resolveType();
    }

    public static final class TypeReference extends GLSLAbstractReference<GLSLTypename> implements PsiScopeProcessor {

        public TypeReference(@NotNull GLSLTypename element) {
            super(element);
        }

        private String onlyNamed = null;
        private final ArrayList<GLSLStructDefinition> definitions = new ArrayList<>();

        @Override
        public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state) {
            if (element instanceof final GLSLStructDefinition def) {
                if (onlyNamed == null || onlyNamed.equals(def.getStructName())) {
                    definitions.add(def);
                    return onlyNamed == null;// Done if we found the one named
                }
            }
            return true;// Continue
        }

        public @NotNull GLSLType resolveType() {
            final String typeName = GLSLElement.text(getElement());

            GLSLType builtIn = GLSLTypes.getTypeFromName(typeName);
            if (builtIn != null) {
                return builtIn;
            }

            try {
                onlyNamed = typeName;
                PsiTreeUtil.treeWalkUp(this, getElement(), null, ResolveState.initial());
                if (!definitions.isEmpty()) {
                    return definitions.get(0).getType();
                }
            } finally {
                definitions.clear();
            }

            // TODO: Check built-in structures
            return GLSLTypes.getUndefinedType(typeName);
        }

        @Override
        public @Nullable GLSLStructDefinition resolve() {
            final GLSLType type = resolveType();
            if (type instanceof GLSLStructType) {
                return ((GLSLStructType) type).getDefinition();
            }

            final GLSLBuiltInPsiUtilService bipus = getElement().getProject().getService(GLSLBuiltInPsiUtilService.class);
            if (type instanceof GLSLScalarType) {
                return bipus.getScalarDefinition((GLSLScalarType) type);
            }
            if (type instanceof GLSLVectorType) {
                return bipus.getVecDefinition((GLSLVectorType) type);
            }
            if (type instanceof GLSLMatrixType) {
                return bipus.getMatrixDefinition((GLSLMatrixType) type);
            }
            if (type instanceof GLSLOpaqueType) {
                return bipus.getOpaqueDefinition((GLSLOpaqueType) type);
            }
            return null;
        }
    }

    @NotNull
    @Override
    public TypeReference getReference() {
        return new TypeReference(this);
    }

    @Override
    public String toString() {
        return "Struct Reference: '" + getReferencedTypeName() + "'";
    }

    @NotNull
    public GLSLQualifiedDeclaration[] getDeclarations() {
        final GLSLStructDefinition definition = getStructDefinition();
        if (definition != null) {
            return definition.getDeclarations();
        } else {
            return GLSLQualifiedDeclaration.NO_DECLARATIONS;
        }
    }

    @NotNull
    public GLSLDeclarator[] getDeclarators() {
        final GLSLStructDefinition definition = getStructDefinition();
        if (definition != null) {
            return definition.getDeclarators();
        } else {
            return GLSLDeclarator.NO_DECLARATORS;
        }
    }
}
