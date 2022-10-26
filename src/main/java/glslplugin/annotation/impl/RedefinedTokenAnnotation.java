package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import glslplugin.GLSLHighlighter;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.preprocessor.GLSLDefineDirective;
import glslplugin.lang.elements.preprocessor.GLSLRedefinedToken;
import org.jetbrains.annotations.NotNull;

/**
 *
 * Created by abigail on 08/07/15.
 */
public class RedefinedTokenAnnotation extends Annotator<GLSLRedefinedToken> {
    @Override
    public void annotate(GLSLRedefinedToken identifier, AnnotationHolder holder) {

        final GLSLRedefinedToken.RedefinedTokenReference reference = identifier.getReference();
        GLSLDefineDirective referent = (reference != null) ? reference.resolve() : null;
        final String simpleDefinition = (referent != null) ? referent.getBoundText() : null;

        final String recursiveDefinition = identifier.redefinedTo();

        if (simpleDefinition == null || simpleDefinition.replaceAll("\\s", "").equals(recursiveDefinition.replaceAll("\\s", ""))) {
            holder.newAnnotation(HighlightSeverity.INFORMATION, recursiveDefinition)
                    .textAttributes(GLSLHighlighter.GLSL_REDEFINED_TOKEN[0]).create();
        } else {
            holder.newAnnotation(HighlightSeverity.INFORMATION, simpleDefinition+"\n"+recursiveDefinition)
                    .tooltip("<html><p>"+simpleDefinition+"</p><p>"+recursiveDefinition+"</p></html>")
                    .textAttributes(GLSLHighlighter.GLSL_REDEFINED_TOKEN[0]).create();
        }
    }

    @NotNull
    @Override
    public Class<GLSLRedefinedToken> getElementType() {
        return GLSLRedefinedToken.class;
    }
}
