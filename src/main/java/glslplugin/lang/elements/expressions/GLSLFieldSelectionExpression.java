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
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.declarations.GLSLStructDefinition;
import glslplugin.lang.elements.declarations.GLSLStructMemberDeclaration;
import glslplugin.lang.elements.reference.GLSLAbstractReference;
import glslplugin.lang.elements.reference.GLSLBuiltInPsiUtilService;
import glslplugin.lang.elements.reference.GLSLReferencableDeclaration;
import glslplugin.lang.elements.reference.GLSLReferenceUtil;
import glslplugin.lang.elements.reference.GLSLReferencingElement;
import glslplugin.lang.elements.types.GLSLScalarType;
import glslplugin.lang.elements.types.GLSLStructType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import glslplugin.lang.elements.types.GLSLVectorType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;

/**
 * GLSLMemberOperator is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 28, 2009
 *         Time: 4:40:28 PM
 */
public class GLSLFieldSelectionExpression extends GLSLSelectionExpressionBase implements GLSLReferencingElement {
    public GLSLFieldSelectionExpression(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    private PsiElement getFieldIdentifier() {
        return findChildByType(GLSLTokenTypes.IDENTIFIER);
    }

    @Override
    public @Nullable PsiElement getReferencingIdentifierForRenaming() {
        return getFieldIdentifier();
    }

    /** Return the absolute range of the field identifier.
     * If the identifier does not exist due to an error, returns empty range at the end. */
    public TextRange getFieldNameRange() {
        final PsiElement fieldIdentifier = getFieldIdentifier();
        if (fieldIdentifier != null) {
            return fieldIdentifier.getTextRange();
        }
        final ASTNode node = getNode();
        final int end = node.getStartOffset() + node.getTextLength();
        return new TextRange(end, end);
    }

    @Nullable
    public String getFieldName() {
        return GLSLElement.text(getFieldIdentifier());
    }

    public void setFieldName(@NotNull String newName) throws IncorrectOperationException {
        GLSLReferencableDeclaration.replaceIdentifier(getFieldIdentifier(), newName);
    }

    @Override
    public boolean isLValue() {
        // A member is L-Value only if its container also is L-Value
        GLSLExpression leftExpression = getLeftHandExpression();
        //noinspection SimplifiableIfStatement
        if(leftExpression == null) {
            return true; //It might be, but right now it is broken.
        } else {
            if (isSwizzle()) {
                // It is a swizzle, so it may or may not be an L-value
                // If any of the components are repeated, it is not an L-value
                String components = getFieldName();
                if (components == null) return true;
                for (int i = 0; i < components.length(); i++) {
                    char c = components.charAt(i);
                    for (int j = i+1; j < components.length(); j++) {
                        if(c == components.charAt(j)){
                            //When any component is repeated, it is not a L value
                            return false;
                        }
                    }
                }
                return true;
            } else {
                return leftExpression.isLValue();
            }
        }
    }

    /**
     * Reports whether this selection operates on vector and selects more than one component.
     * Returns false if it failed to find out.
     */
    public boolean isSwizzle(){
        return getRawReference() instanceof FieldReference[];
    }

    public static final class FieldReference extends GLSLAbstractReference<GLSLFieldSelectionExpression> {

        public static final FieldReference[] EMPTY_ARRAY = new FieldReference[0];
        private final String fieldName;
        private final GLSLStructDefinition fieldStruct;

        public FieldReference(@NotNull GLSLFieldSelectionExpression element, TextRange textRange, @NotNull String fieldName, @NotNull GLSLStructDefinition fieldStruct) {
            super(element, textRange);
            this.fieldName = fieldName;
            this.fieldStruct = fieldStruct;
        }

        @Override
        public @Nullable GLSLDeclarator resolve() {
            return fieldStruct.getDeclarator(fieldName);
        }

        @Override
        public @NotNull String getCanonicalText() {
            final GLSLDeclarator resolve = resolve();
            if (resolve != null) {
                return resolve.getHierarchicalVariableName();
            }
            return super.getCanonicalText();
        }
    }

    @Override
    public FieldReference getReference() {
        final Object rawReference = getRawReference();
        if (rawReference == null) {
            return null;
        }
        if (rawReference instanceof FieldReference) {
            return (FieldReference) rawReference;
        }
        return ((FieldReference[]) rawReference)[0];
    }

    @Override
    public FieldReference @NotNull [] getReferences() {
        final Object rawReference = getRawReference();
        if (rawReference == null) {
            return FieldReference.EMPTY_ARRAY;
        }
        if (rawReference instanceof FieldReference) {
            return new FieldReference[] {(FieldReference) rawReference};
        }
        return (FieldReference[]) rawReference;
    }

    private Object getRawReference() {
        GLSLExpression leftHandExpression = getLeftHandExpression();
        PsiElement memberIdentifier = getFieldIdentifier();
        if (leftHandExpression == null || memberIdentifier == null) return null;
        GLSLType leftHandType = leftHandExpression.getType();
        if (!leftHandType.isValidType()) return null;

        String fieldName = GLSLElement.text(memberIdentifier);
        if (fieldName.isEmpty()) {
            return null;
        }
        final TextRange baseRange = GLSLReferenceUtil.rangeOfIn(memberIdentifier, this);

        if (leftHandType instanceof GLSLVectorType || leftHandType instanceof GLSLScalarType) {
            // Resolving swizzling
            final GLSLBuiltInPsiUtilService bipus = getProject().getService(GLSLBuiltInPsiUtilService.class);
            final GLSLStructDefinition fieldStruct;
            if (leftHandType instanceof GLSLVectorType) {
                fieldStruct = bipus.getVecDefinition((GLSLVectorType) leftHandType);
            } else {
                fieldStruct = bipus.getScalarDefinition((GLSLScalarType) leftHandType);
            }

            if (fieldName.length() == 1) {
                // No swizzle
                return new FieldReference(this, baseRange, fieldName, fieldStruct);
            } else {
                // Swizzle
                final FieldReference[] result = new FieldReference[fieldName.length()];
                for (int i = 0; i < fieldName.length(); i++) {
                    final String swizzleFieldName = Character.toString(fieldName.charAt(i));
                    result[i] = new FieldReference(this, new TextRange(baseRange.getStartOffset() + i, baseRange.getStartOffset() + i + 1), swizzleFieldName, fieldStruct);
                }
                return result;
            }
        }

        if (leftHandType instanceof GLSLStructType) {
            return new FieldReference(this, baseRange, fieldName, ((GLSLStructType) leftHandType).getDefinition());
        }

        return null;
    }

    @NotNull
    @Override
    public GLSLType getType() {
        final Object rawReference = getRawReference();
        if (rawReference == null) {
            return GLSLTypes.UNKNOWN_TYPE;
        }
        if (rawReference instanceof FieldReference) {
            final GLSLDeclarator resolved = ((FieldReference) rawReference).resolve();
            if (resolved == null) {
                return GLSLTypes.UNKNOWN_TYPE;
            }
            return resolved.getType();
        }
        final FieldReference[] references = (FieldReference[]) rawReference;
        GLSLType baseType = null;
        for (FieldReference ref : references) {
            final GLSLDeclarator resolve = ref.resolve();
            if (resolve != null) {
                baseType = resolve.getType();
                break;
            }
        }
        if (baseType == null) {
            return GLSLTypes.UNKNOWN_TYPE;
        }
        return GLSLVectorType.getType(baseType, Math.min(Math.max(GLSLVectorType.MIN_VECTOR_DIM, references.length), GLSLVectorType.MAX_VECTOR_DIM));
    }

    public String toString() {
        String field = getFieldName();
        if(field == null){
            return "Field selection: '(unknown)'";
        }else{
            return "Field selection: '" + field + "'";
        }
    }
}
