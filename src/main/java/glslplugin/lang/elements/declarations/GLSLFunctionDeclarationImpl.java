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
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.PsiTreeUtil;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.reference.GLSLAbstractReference;
import glslplugin.lang.elements.reference.GLSLReferencingElement;
import glslplugin.lang.elements.types.GLSLBasicFunctionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Represents a function declaration, without definition.
 */
public class GLSLFunctionDeclarationImpl extends GLSLElementImpl implements GLSLFunctionDeclaration, GLSLReferencingElement {

    private final CachedValue<GLSLBasicFunctionType> functionTypeCache = GLSLFunctionDeclaration.newCachedFunctionType(this);

    public GLSLFunctionDeclarationImpl(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @NotNull
    public GLSLBasicFunctionType getType() {
        return functionTypeCache.getValue();
    }

    @Nullable
    @Override
    public String getName() {
        return getFunctionName();
    }

    @Override
    public int getTextOffset() {
        final PsiElement identifier = getFunctionNameIdentifier();
        return identifier != null ? identifier.getTextOffset() : super.getTextOffset();
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, @Nullable PsiElement lastParent, @NotNull PsiElement place) {
        final boolean lookingFromInside = lastParent != null || PsiTreeUtil.isAncestor(this, place, false);

        // Can't see the function from inside
        if (lookingFromInside) {
            // Declaration variables can't be seen by anyone
            return true;
        } else {
            // Show the function declaration
            return processor.execute(this, state);
        }
    }

    @Override
    public @Nullable PsiElement getReferencingIdentifierForRenaming() {
        return getFunctionNameIdentifier();
    }

    @Override
    public FunctionDefinitionReference getReference() {
        return new FunctionDefinitionReference(this);
    }

    public static class FunctionDefinitionReference
            extends GLSLAbstractReference.Poly<GLSLFunctionDeclarationImpl>
            implements PsiScopeProcessor {

        public FunctionDefinitionReference(@NotNull GLSLFunctionDeclarationImpl source) {
            super(source);
        }

        private String onlyNamed = null;
        private final ArrayList<GLSLFunctionDefinition> functionDefinitions = new ArrayList<>();

        @Override
        public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
            try {
                final GLSLFunctionDeclarationImpl element = this.element;
                final String functionName = element.getFunctionName();
                if (functionName == null) return ResolveResult.EMPTY_ARRAY;
                onlyNamed = functionName;

                PsiTreeUtil.treeWalkUp(this, element, null, ResolveState.initial());

                if (functionDefinitions.isEmpty()) {
                    return ResolveResult.EMPTY_ARRAY;
                }

                final GLSLBasicFunctionType funcType = element.getType();
                final ResolveResult[] results = new ResolveResult[functionDefinitions.size()];
                for (int i = 0; i < results.length; i++) {
                    final GLSLFunctionDefinition def = functionDefinitions.get(i);
                    final GLSLBasicFunctionType defType = def.getType();
                    results[i] = new PsiElementResolveResult(def, funcType.definitionsMatch(defType));
                }
                return results;
            } finally {
                onlyNamed = null;
                functionDefinitions.clear();
            }
        }

        @Override
        public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state) {
            if (element instanceof GLSLFunctionDefinition def) {
                final String onlyNamed = this.onlyNamed;
                if (onlyNamed == null || onlyNamed.equals(def.getFunctionName())) {
                    functionDefinitions.add(def);
                }
            }
            return true;// Continue
        }

        @Override
        public Object @NotNull [] getVariants() {
            try {
                PsiTreeUtil.treeWalkUp(this, getElement(), null, ResolveState.initial());
                return functionDefinitions.toArray();
            } finally {
                functionDefinitions.clear();
            }
        }
    }

    @Override
    public String toString() {
        return getSignature() + ";";
    }
}
