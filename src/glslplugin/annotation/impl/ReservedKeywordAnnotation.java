package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.GLSLTokenTypes;
import org.jetbrains.annotations.NotNull;

/**
 * Created by abigail on 23/06/15.
 */
public class ReservedKeywordAnnotation extends Annotator<PsiElement> {
    @Override
    public void annotate(PsiElement element, AnnotationHolder holder) {
        if (element.getNode().getElementType() == GLSLTokenTypes.RESERVED_KEYWORD) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Reserved keyword").create();
        }
    }

    @NotNull
    @Override
    public Class<PsiElement> getElementType() {
        return PsiElement.class;
    }
}
