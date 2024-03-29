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
import glslplugin.lang.elements.types.GLSLStructType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Definition of a struct. Holds its members and name, if any.
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 27, 2009
 *         Time: 10:31:13 AM
 */
public class GLSLStructDefinition extends GLSLElementImpl implements GLSLTypedElement, GLSLReferencableDeclaration {
    // Cache this one to enable equals comparison by ==
    //  this is required to be able to compare types of variables of anonymous types.
    // struct {int x;} x, y; <- how to compare types of x and y?
    private GLSLStructType typeCache = null;
    /** Struct members are recomputed on getType when dirty */
    private boolean typeCacheDirty = false;

    public GLSLStructDefinition(@NotNull ASTNode astNode) {
        super(astNode);
    }

    /** @return the element that holds the struct name */
    @Nullable
    private PsiElement getStructNameIdentifier() {
        return findChildByType(GLSLTokenTypes.IDENTIFIER);
    }

    public @Nullable String getStructName() {
        return GLSLElement.text(getStructNameIdentifier());
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
        final String name = getStructName();
        if (name == null || name.isEmpty()) return "Anonymous struct";
        return "Struct Type: '" + name + "'";
    }

    /**
     * Get the struct type declared.
     * This always returns the same instance, but internal data is refreshed on each get.
     * Returned value can therefore change when kept for long enough - don't cache it if you rely on always updated data.
     */
    @NotNull
    public GLSLStructType getType() {
        if (typeCache == null) {
            typeCache = new GLSLStructType(this);
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
        return "struct";
    }

    @Nullable
    @Override
    public String getName() {
        return getStructName();
    }

    @Override
    public int getTextOffset() {
        final PsiElement identifier = getStructNameIdentifier();
        return identifier != null ? identifier.getTextOffset() : super.getTextOffset();
    }

    @Override
    public @Nullable PsiElement getNameIdentifier() {
        return getStructNameIdentifier();
    }

    @Override
    public void subtreeChanged() {
        super.subtreeChanged();

        // Struct's subtree has changed, refresh struct members which may have changed
        typeCacheDirty = true;
    }
}
