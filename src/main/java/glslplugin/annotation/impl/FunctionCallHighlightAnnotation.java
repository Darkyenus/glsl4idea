package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import glslplugin.GLSLHighlighter;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.expressions.GLSLFunctionOrConstructorCallExpression;
import org.jetbrains.annotations.NotNull;

public class FunctionCallHighlightAnnotation extends Annotator<GLSLFunctionOrConstructorCallExpression> {
    @Override
    public void annotate(GLSLFunctionOrConstructorCallExpression expression, AnnotationHolder holder) {
        final PsiElement identifier = expression.getFunctionOrConstructedTypeNameIdentifier();
        if (identifier == null) return;

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(identifier.getTextRange())
                .textAttributes(expression.isConstructor() ? GLSLHighlighter.GLSL_CONSTRUCTOR_CALL[0] : GLSLHighlighter.GLSL_FUNCTION_CALL[0])
                .create();
    }

    @NotNull
    @Override
    public Class<GLSLFunctionOrConstructorCallExpression> getElementType() {
        return GLSLFunctionOrConstructorCallExpression.class;
    }
}
