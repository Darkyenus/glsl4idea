package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import glslplugin.GLSLHighlighter;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.preprocessor.GLSLFlowAttribute;
import org.jetbrains.annotations.NotNull;

/**
 * Highlight-only annotation for variable references which are uniforms or varyings.
 * Highlighting style is the same as normal variables by default, so for this to be visible, new style must be selected manually.
 */
public class FlowAttributeHighlightAnnotation extends Annotator<GLSLFlowAttribute> {

    @Override
    public void annotate(GLSLFlowAttribute expr, AnnotationHolder holder) {
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION).textAttributes(GLSLHighlighter.GLSL_PREPROCESSOR_DIRECTIVE[0]).create();
    }

    @NotNull
    @Override
    public Class<GLSLFlowAttribute> getElementType() {
        return GLSLFlowAttribute.class;
    }
}
