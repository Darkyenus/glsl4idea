package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import glslplugin.GLSLHighlighter;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.preprocessor.GLSLDefineDirective;
import org.jetbrains.annotations.NotNull;

public class MacroReferenceHighlightAnnotation<T extends PsiElement> extends Annotator<T> {
    private final Class<T> elementType;

    public MacroReferenceHighlightAnnotation(@NotNull Class<T> elementType) {
        this.elementType = elementType;
    }

    @Override
    public void annotate(T expr, AnnotationHolder holder) {
        for (PsiReference reference : expr.getReferences()) {
            if (!(reference.resolve() instanceof GLSLDefineDirective)) {
                continue;
            }

            final TextRange range = reference.getRangeInElement().shiftRight(expr.getTextRange().getStartOffset());
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(range)
                    .textAttributes(GLSLHighlighter.GLSL_REDEFINED_TOKEN[0])
                    .create();
        }
    }

    @NotNull
    @Override
    public Class<T> getElementType() {
        return elementType;
    }
}
