package glslplugin.annotation.impl;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.tree.IElementType;
import glslplugin.GLSLHighlighter;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.GLSLElementType;
import glslplugin.lang.elements.preprocessor.GLSLDefineDirective;
import glslplugin.lang.elements.preprocessor.GLSLRedefinedToken;
import glslplugin.lang.elements.reference.GLSLMacroReference;
import org.jetbrains.annotations.NotNull;

/**
 *
 * Created by abigail on 08/07/15.
 */
public class RedefinedTokenAnnotation extends Annotator<GLSLRedefinedToken> {
    @Override
    public void annotate(GLSLRedefinedToken identifier, AnnotationHolder holder) {
        String definition;

        final IElementType identifierType = identifier.getNode().getElementType();
        if (identifierType instanceof GLSLElementType.RedefinedTokenElementType) {
            definition = ((GLSLElementType.RedefinedTokenElementType) identifierType).text;
        } else {
            GLSLMacroReference reference = identifier.getReference();
            GLSLDefineDirective referent = (reference != null) ? reference.resolve() : null;
            definition = (referent != null) ? referent.getBoundText() : null;
        }

        Annotation annotation = holder.createInfoAnnotation(identifier, definition);
        annotation.setTextAttributes(GLSLHighlighter.GLSL_REDEFINED_TOKEN[0]);
    }

    @NotNull
    @Override
    public Class<GLSLRedefinedToken> getElementType() {
        return GLSLRedefinedToken.class;
    }
}
