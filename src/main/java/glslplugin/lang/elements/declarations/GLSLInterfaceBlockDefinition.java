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
import glslplugin.lang.elements.reference.GLSLReferencableDeclaration;
import glslplugin.lang.elements.types.GLSLInterfaceBlockType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GLSLInterfaceBlockDefinition extends GLSLElementImpl implements GLSLTypedElement, GLSLReferencableDeclaration {
    private GLSLInterfaceBlockType typeCache = null;
    /** Interface block members are recomputed on getType when dirty */
    private boolean typeCacheDirty = false;

    public GLSLInterfaceBlockDefinition(@NotNull ASTNode astNode) {
        super(astNode);
    }

    /** @return the element that holds the interface block name */
    @Nullable
    private PsiElement getInterfaceBlockNameIdentifier() {
        return findChildByType(GLSLTokenTypes.IDENTIFIER);
    }

    public @Nullable String getInterfaceBlockName() {
        return GLSLElement.text(getInterfaceBlockNameIdentifier());
    }

    @NotNull
    public GLSLStructMemberDeclaration[] getDeclarations() {
        return findChildrenByClass(GLSLStructMemberDeclaration.class);
    }

    @NotNull
    public GLSLDeclarator[] getDeclarators() {
        List<GLSLDeclarator> declarators = new ArrayList<>();
        for (GLSLStructMemberDeclaration declaration : getDeclarations()) {
            Collections.addAll(declarators, declaration.getDeclarators());
        }
        return declarators.toArray(GLSLDeclarator.NO_DECLARATORS);
    }

    @Override
    public String toString() {
        final String name = getInterfaceBlockName();
        if (name == null || name.isEmpty()) return "Anonymous interface block";
        return "Interface Block: '" + name + "'";
    }

    /**
     * Get the interface block declared.
     * This always returns the same instance, but internal data is refreshed on each get.
     * Returned value can therefore change when kept for long enough - don't cache it if you rely on always updated data.
     */
    @NotNull
    public GLSLInterfaceBlockType getType() {
        if (typeCache == null) {
            typeCache = new GLSLInterfaceBlockType(this);
            typeCacheDirty = false;
        } else if (typeCacheDirty){
            typeCache.updateNameAndMembers();
            typeCacheDirty = false;
        }
        return typeCache;
    }

    @Nullable
    public GLSLDeclarator getDeclarator(@NotNull String name) {
        for (GLSLDeclarator declarator : getDeclarators()) {
            if (name.equals(declarator.getVariableName())) {
                return declarator;
            }
        }
        return null;
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, @Nullable PsiElement lastParent, @NotNull PsiElement place) {
        if (!processor.execute(this, state))
            return false;

        if (lastParent == null || !PsiTreeUtil.isAncestor(this,place, false)) {
            // Do not show declarations of parameters to outside scopes
            return true;
        }

        for (GLSLDeclarator declarator : getDeclarators()) {
            if (PsiTreeUtil.isAncestor(lastParent, declarator, false))
                continue;
            if (!processor.execute(declarator, state))
                return false;
        }
        return true;
    }

    @Override
    public @NotNull String declaredNoun() {
        return "interface block";
    }

    @Nullable
    @Override
    public String getName() {
        return getInterfaceBlockName();
    }

    @Override
    public int getTextOffset() {
        final PsiElement identifier = getInterfaceBlockNameIdentifier();
        return identifier != null ? identifier.getTextOffset() : super.getTextOffset();
    }

    @Override
    public @Nullable PsiElement getNameIdentifier() {
        return getInterfaceBlockNameIdentifier();
    }

    @Override
    public void subtreeChanged() {
        super.subtreeChanged();
        typeCacheDirty = true;
    }
}
