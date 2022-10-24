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
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.declarations.GLSLFunctionDeclaration;
import glslplugin.lang.elements.reference.GLSLAbstractReference;
import glslplugin.lang.elements.reference.GLSLBuiltInPsiUtilService;
import glslplugin.lang.elements.reference.GLSLReferencingElement;
import glslplugin.lang.elements.types.GLSLArrayType;
import glslplugin.lang.elements.types.GLSLMatrixType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import glslplugin.lang.elements.types.GLSLVectorType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A call of function that is defined as a part of struct.
 * Standard GLSL defines only one method: length() on vectors, matrices and arrays.
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 3, 2009
 *         Time: 12:41:53 PM
 */
public class GLSLMethodCallExpression extends GLSLSelectionExpressionBase implements GLSLReferencingElement {

    public GLSLMethodCallExpression(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    private PsiElement getMethodIdentifier() {
        return findChildByType(GLSLTokenTypes.IDENTIFIER);
    }

    @Override
    public @Nullable PsiElement getReferencingIdentifierForRenaming() {
        // length() can't be renamed, but the renaming will be stopped because it is in a read-only file.
        // This is here for any possible future extensions of user-defined methods.
        return getMethodIdentifier();
    }

    @Nullable
    public String getMethodName() {
        PsiElement identifier = getMethodIdentifier();
        return identifier == null ? null : identifier.getText();
    }

    @Nullable
    public GLSLParameterList getParameterList() {
        final PsiElement last = getLastChild();
        if(last instanceof GLSLParameterList){
            return (GLSLParameterList) last;
        }else{
            return null;
        }
    }

    public static final class MethodCallReference extends GLSLAbstractReference<GLSLMethodCallExpression> {

        public MethodCallReference(@NotNull GLSLMethodCallExpression element) {
            super(element);
        }

        @Override
        public boolean isSoft() {
            return true;
        }

        @Override
        public @Nullable GLSLFunctionDeclaration resolve() {
            if (getElement().isValidLengthMethod()) {
                final GLSLBuiltInPsiUtilService bipus = getElement().getProject().getService(GLSLBuiltInPsiUtilService.class);
                return bipus.getLengthMethodDeclaration();
            }
            return null;
        }
    }

    private boolean isValidLengthMethod() {
        if (!"length".equals(getMethodName())) {
            return false;
        }

        final GLSLParameterList parameterList = getParameterList();
        if (parameterList == null) {
            return false;
        }
        if (parameterList.getParameters().length != 0) {
            return false;
        }

        final GLSLExpression target = getLeftHandExpression();
        if (target == null) {
            return false;
        }
        final GLSLType type = target.getType();
        return type instanceof GLSLVectorType || type instanceof GLSLArrayType || type instanceof GLSLMatrixType;
    }

    @Override
    public MethodCallReference getReference() {
        return new MethodCallReference(this);
    }

    @Override
    public boolean isConstantValue() {
        return getConstantValue() != null;
    }

    @Override
    public @Nullable Object getConstantValue() {
        if (isValidLengthMethod()) {
            final GLSLExpression target = getLeftHandExpression();
            if (target == null) {
                return false;
            }
            final GLSLType type = target.getType();
            if (type instanceof GLSLVectorType) {
                return ((GLSLVectorType) type).getNumComponents();
            }
            if (type instanceof GLSLMatrixType) {
                return ((GLSLMatrixType) type).getNumColumns();
            }
            if (type instanceof GLSLArrayType) {
                final int[] dims = ((GLSLArrayType) type).getDimensions();
                if (dims.length > 0 && dims[0] >= 0) {
                    return dims[0];
                }
            }
        }
        return null;
    }

    @Override
    public @NotNull GLSLType getType() {
        final MethodCallReference ref = getReference();
        if (ref == null) return GLSLTypes.UNKNOWN_TYPE;
        final GLSLFunctionDeclaration resolve = ref.resolve();
        if (resolve == null) return GLSLTypes.UNKNOWN_TYPE;
        return resolve.getType().getReturnType();
    }

    @Override
    public String toString() {
        return "Method Call: " + getMethodName();
    }
}
