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
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.reference.GLSLAbstractReference;
import glslplugin.lang.elements.reference.GLSLReferencingElement;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Expression that consists just of a variable reference.
 */
public class GLSLVariableExpression extends GLSLExpression implements GLSLReferencingElement {
    public GLSLVariableExpression(@NotNull ASTNode astNode) {
        super(astNode);
    }

    private @Nullable PsiElement getVariableNameIdentifier() {
        return findChildByType(GLSLTokenTypes.IDENTIFIER);
    }

    @Override
    public @Nullable PsiElement getReferencingIdentifierForRenaming() {
        return getVariableNameIdentifier();
    }

    public @Nullable String getVariableName() {
        return GLSLElement.text(getVariableNameIdentifier());
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
        return "Variable Expression: " + getVariableName();
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
            extends GLSLAbstractReference<GLSLVariableExpression>
    {

        public VariableReference(@NotNull GLSLVariableExpression element) {
            super(element);
        }

        @Override
        public synchronized @Nullable GLSLDeclarator resolve() {
            String onlyNamed = getElement().getVariableName();
            if (onlyNamed == null || onlyNamed.isEmpty()) {
                return null;
            }
            final VariableWalkResult result = VariableWalkResult.walkPossibleReferences(element, onlyNamed);
            if (!result.visitedDeclarations.isEmpty()) {
                return result.visitedDeclarations.get(0);
            }
            return null;
        }

        @Override
        public synchronized Object @NotNull [] getVariants() {
            final VariableWalkResult result = VariableWalkResult.walkPossibleReferences(element, null);
            return result.visitedDeclarations.toArray(PsiElement.EMPTY_ARRAY);
        }
    }

    public static final class VariableWalkResult implements PsiScopeProcessor {

        public static VariableWalkResult walkPossibleReferences(PsiElement from, String onlyNamed) {
            final VariableWalkResult result = new VariableWalkResult(onlyNamed);
            PsiTreeUtil.treeWalkUp(result, from, null, ResolveState.initial());
            return result;
        }

        private final String onlyNamed;
        public final ArrayList<GLSLDeclarator> visitedDeclarations = new ArrayList<>();

        public VariableWalkResult(String onlyNamed) {
            this.onlyNamed = onlyNamed;
        }

        @Override
        public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state) {
            if (!(element instanceof final GLSLDeclarator declarator)) {
                return true;// Continue
            }

            final String onlyNamed = this.onlyNamed;
            if (onlyNamed != null) {
                if (onlyNamed.equals(declarator.getVariableName())) {
                    visitedDeclarations.add(declarator);
                    return false; // We found what we were looking for
                }
            } else {
                visitedDeclarations.add(declarator);
            }
            return true;// Continue, keep looking
        }

    }

    @NotNull
    @Override
    public GLSLVariableExpression.VariableReference getReference() {
        return new VariableReference(this);
    }
}
