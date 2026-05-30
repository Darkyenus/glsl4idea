package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import org.jetbrains.annotations.NotNull;

public class DeclarationTypeHighlightAnnotation extends Annotator<GLSLDeclarator> {
    @Override
    public void annotate(GLSLDeclarator declarator, AnnotationHolder holder) {
        final GLSLHighlightClassifier.HighlightKind kind = GLSLHighlightClassifier.classify(declarator);
        final var key = GLSLHighlightClassifier.keyFor(kind);
        if (key != null && declarator.getNameIdentifier() != null) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION).range(declarator.getNameIdentifier()).textAttributes(key).create();
        }
    }

    @NotNull
    @Override
    public Class<GLSLDeclarator> getElementType() {
        return GLSLDeclarator.class;
    }
}
