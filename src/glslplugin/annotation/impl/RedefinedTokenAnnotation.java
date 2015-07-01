package glslplugin.annotation.impl;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import glslplugin.GLSLHighlighter;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.GLSLElementTypes;
import glslplugin.lang.elements.GLSLIdentifier;
import org.jetbrains.annotations.NotNull;

/**
 * Created by abigail on 08/07/15.
 */
public class RedefinedTokenAnnotation extends Annotator<GLSLIdentifier> {
    @Override
    public void annotate(GLSLIdentifier identifier, AnnotationHolder holder) {
        if (identifier.getNode().getElementType() == GLSLElementTypes.REDEFINED_TOKEN) {
            Annotation annotation = holder.createInfoAnnotation(identifier, null);
            annotation.setTextAttributes(GLSLHighlighter.GLSL_REDEFINED_TOKEN[0]);
        }
    }

    @NotNull
    @Override
    public Class<GLSLIdentifier> getElementType() {
        return GLSLIdentifier.class;
    }
}
