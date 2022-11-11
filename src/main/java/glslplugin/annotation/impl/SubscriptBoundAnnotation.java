package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.expressions.GLSLExpression;
import glslplugin.lang.elements.expressions.GLSLSubscriptExpression;
import glslplugin.lang.elements.types.*;
import org.jetbrains.annotations.NotNull;

/**
 * Created by abigail on 08/07/15.
 */
public class SubscriptBoundAnnotation extends Annotator<GLSLSubscriptExpression> {
    @Override
    public void annotate(GLSLSubscriptExpression expr, AnnotationHolder holder) {
        GLSLExpression expression = expr.getArrayExpression();
        GLSLExpression subscript = expr.getSubscript();
        if (expression == null || subscript == null) return;
        if (!expression.getType().isValidType() || !subscript.getType().isValidType()) return;

        GLSLType type = subscript.getType();
        if (!type.typeEquals(GLSLScalarType.INT) && !type.typeEquals(GLSLScalarType.UINT)) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Subscript must be of int or uint type, found " + type.getTypename()).create();
            return;
        }

        int dimension;

        if (expression.getType() instanceof GLSLArrayType) {
            dimension = ((GLSLArrayType) expression.getType()).getDimensions()[0];

            // Disabled, because constant value detection is not perfect yet
            if (!subscript.isConstantValue() && false) {
                if (dimension == GLSLArrayType.UNDEFINED_SIZE_DIMENSION) {
                    holder.newAnnotation(HighlightSeverity.ERROR, "Unsized arrays may only be indexed with constant expressions").create();
                    return;
                }
            }
        } else if (expression.getType() instanceof GLSLVectorType) {
            dimension = ((GLSLVectorType) expression.getType()).getNumComponents();
        } else if (expression.getType() instanceof GLSLMatrixType) {
            dimension = ((GLSLMatrixType) expression.getType()).getNumColumns();
        } else {
            holder.newAnnotation(HighlightSeverity.ERROR, "Subscripted expression must be of array, matrix or vector type").create();
            return;
        }

        if (dimension == GLSLArrayType.UNDEFINED_SIZE_DIMENSION || dimension == GLSLArrayType.DYNAMIC_SIZE_DIMENSION) return;
        if (!subscript.isConstantValue()) return;

        Long value = (Long) subscript.getConstantValue();
        assert value != null; // if !isConstantValue we've already returned
        if (value < 0 || value >= dimension) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Subscript index out-of-bounds").create();
        }
    }

    @NotNull
    @Override
    public Class<GLSLSubscriptExpression> getElementType() {
        return GLSLSubscriptExpression.class;
    }
}
