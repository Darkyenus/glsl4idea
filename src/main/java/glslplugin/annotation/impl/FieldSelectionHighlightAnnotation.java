package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.TextRange;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.expressions.GLSLFieldSelectionExpression;
import org.jetbrains.annotations.NotNull;

public class FieldSelectionHighlightAnnotation extends Annotator<GLSLFieldSelectionExpression> {
    @Override
    public void annotate(GLSLFieldSelectionExpression expr, AnnotationHolder holder) {
        for (GLSLFieldSelectionExpression.FieldReference reference : expr.getReferences()) {
            final GLSLHighlightClassifier.HighlightKind kind = GLSLHighlightClassifier.classify(reference);
            final var key = GLSLHighlightClassifier.keyFor(kind);
            if (key == null) continue;
            final TextRange range = reference.getRangeInElement().shiftRight(expr.getTextRange().getStartOffset());
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(range).textAttributes(key).create();
        }
    }

    @NotNull
    @Override
    public Class<GLSLFieldSelectionExpression> getElementType() {
        return GLSLFieldSelectionExpression.class;
    }
}
