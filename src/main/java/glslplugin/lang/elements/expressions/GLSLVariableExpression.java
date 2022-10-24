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

package glslplugin.lang.elements.expressions;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.reference.GLSLReferenceUtil;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Expression that consists just of a variable reference.
 */
public class GLSLVariableExpression extends GLSLExpression {
    public GLSLVariableExpression(@NotNull ASTNode astNode) {
        super(astNode);
    }

    private @Nullable PsiElement getVariableNameIdentifier() {
        return findChildByType(GLSLTokenTypes.IDENTIFIER);
    }

    public @Nullable String getVariableName() {
        final PsiElement identifier = getVariableNameIdentifier();
        return identifier == null ? null : identifier.getText();
    }

    @Override
    public boolean isLValue() {
        final GLSLDeclarator declarator = getReference().resolve();
        if (declarator == null) {
            return true;// Probably
        }

        return declarator.getQualifiedType().isLValue();
    }

    //TODO Is this necessary?
    @Override
    public String getName() {
        return getVariableName();
    }

    @Override
    public String toString() {
        return "Identifier Expression: " + getName();
    }

    @NotNull
    @Override
    public GLSLType getType() {
        final GLSLDeclarator declarator = getReference().resolve();
        if (declarator == null) {
            return GLSLTypes.UNKNOWN_TYPE;
        }
        return declarator.getType();
    }

    public static final class VariableReference
            extends PsiReferenceBase<GLSLVariableExpression>
            implements PsiScopeProcessor
    {

        public VariableReference(@NotNull GLSLVariableExpression element) {
            super(element, GLSLReferenceUtil.rangeOfIn(element.getVariableNameIdentifier(), element), false);
        }

        private final ArrayList<GLSLDeclarator> visitedDeclarations = new ArrayList<>();
        private String onlyNamed = null;

        // PSI Scope processor
        @Override
        public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state) {
            if (!(element instanceof final GLSLDeclarator declarator)) {
                return true;// Continue
            }

            final String onlyNamed = this.onlyNamed;
            if (onlyNamed != null) {
                if (onlyNamed.equals(declarator.getName())) {
                    visitedDeclarations.add(declarator);
                    return false; // We found what we were looking for
                }
            } else {
                visitedDeclarations.add(declarator);
            }
            return true;// Continue, keep looking
        }

        @Override
        public synchronized @Nullable GLSLDeclarator resolve() {
            String onlyNamed = this.onlyNamed = getElement().getName();
            if (onlyNamed == null || onlyNamed.isEmpty()) {
                return null;
            }
            PsiTreeUtil.treeWalkUp(this, getElement(), null, ResolveState.initial());
            if (!visitedDeclarations.isEmpty()) {
                final GLSLDeclarator declaration = visitedDeclarations.get(0);
                visitedDeclarations.clear();
                return declaration;
            }
            return null;
        }

        @Override
        public synchronized Object @NotNull [] getVariants() {
            onlyNamed = null;
            PsiTreeUtil.treeWalkUp(this, getElement(), null, ResolveState.initial());
            final PsiElement[] result = visitedDeclarations.toArray(PsiElement.EMPTY_ARRAY);
            visitedDeclarations.clear();
            return result;
        }

        @Override
        public String toString() {
            return GLSLReferenceUtil.toString(this);
        }
    }

    private VariableReference referenceCache = null;

    @NotNull
    @Override
    public GLSLVariableExpression.VariableReference getReference() {
        VariableReference reference = referenceCache;
        if (reference == null) {
            reference = referenceCache = new VariableReference(this);
        }
        return reference;
    }
}
