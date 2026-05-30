package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.expressions.GLSLVariableExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Highlight-only annotation for variable references which are uniforms or varyings.
 * Highlighting style is the same as normal variables by default, so for this to be visible, new style must be selected manually.
 */
public class VariableTypeHighlightAnnotation extends Annotator<GLSLVariableExpression> {

    @Override
    public void annotate(GLSLVariableExpression expr, AnnotationHolder holder) {
        final var declarator = expr.getReference().resolve();
        if (declarator == null) return;
        final GLSLHighlightClassifier.HighlightKind kind = GLSLHighlightClassifier.classify(declarator);
        final var key = GLSLHighlightClassifier.keyFor(kind);
        if (key != null) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION).textAttributes(key).create();
        }
    }

    @NotNull
    @Override
    public Class<GLSLVariableExpression> getElementType() {
        return GLSLVariableExpression.class;
    }
}
