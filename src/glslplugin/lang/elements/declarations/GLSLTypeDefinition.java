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
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.util.IncorrectOperationException;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.GLSLIdentifier;
import glslplugin.lang.elements.GLSLTypedElement;
import glslplugin.lang.elements.types.GLSLStructType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * GLSLDeclarator is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 27, 2009
 *         Time: 10:31:13 AM
 */
public class GLSLTypeDefinition extends GLSLElementImpl implements GLSLTypedElement, PsiNameIdentifierOwner {
    // Cache this one to enable equals comparison by ==
    //  this is required to be able to compare types of variables of anonymous types.
    // struct {int x;} x, y; <- how to compare types of x and y?
    private GLSLStructType typeCache = null;
    /** Struct members are recomputed on getType when dirty */
    private boolean typeCacheDirty = false;

    public GLSLTypeDefinition(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    public GLSLDeclarationList getDeclarationList() {
        return findChildByClass(GLSLDeclarationList.class);
    }

    @NotNull
    public GLSLDeclaration[] getDeclarations() {
        GLSLDeclarationList declarationList = getDeclarationList();
        if(declarationList == null){
            return GLSLDeclaration.NO_DECLARATIONS;
        }else{
            return declarationList.getDeclarations();
        }
    }

    @NotNull
    public GLSLDeclarator[] getDeclarators() {
        List<GLSLDeclarator> declarators = new ArrayList<GLSLDeclarator>();
        for (GLSLDeclaration declaration : getDeclarations()) {
            Collections.addAll(declarators, declaration.getDeclarators());
        }
        return declarators.toArray(new GLSLDeclarator[declarators.size()]);
    }

    @Override
    public String toString() {
        if (getName() == null) return "Anonymous struct";
        return "Struct Type: '" + getName() + "'";
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
            typeCache.updateMembers();
            typeCacheDirty = false;
        }
        return typeCache;
    }

    @Nullable
    public GLSLDeclarator getDeclarator(@NotNull String name) {
        for (GLSLDeclarator declarator : getDeclarators()) {
            if (name.equals(declarator.getName())) {
                return declarator;
            }
        }
        return null;
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
        if (!processor.execute(this, state)) return false;

        for (GLSLDeclarator declarator : getDeclarators()) {
            if (!processor.execute(declarator, state)) return false;
        }
        return true;
    }

    @Nullable
    @Override
    public GLSLIdentifier getNameIdentifier() {
        return findChildByClass(GLSLIdentifier.class);
    }

    @Override
    public String getName() {
        GLSLIdentifier identifier = getNameIdentifier();
        if (identifier == null) return null;
        return identifier.getName();
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        GLSLIdentifier identifier = getNameIdentifier();
        if (identifier == null) return null;
        return identifier.setName(name);
    }

    @Override
    public void subtreeChanged() {
        super.subtreeChanged();

        // Struct's subtree has changed, refresh struct members which may have changed
        typeCacheDirty = true;
    }
}
