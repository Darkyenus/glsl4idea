package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.tree.IElementType;
import glslplugin.GLSLHighlighter;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.GLSLElementTypes;
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
        String definition;

        final IElementType identifierType = identifier.getNode().getElementType();
        if(identifierType instanceof GLSLElementTypes.RedefinedTokenElementType){
            definition = ((GLSLElementTypes.RedefinedTokenElementType) identifierType).text;
        }else{
            final GLSLRedefinedToken.RedefinedTokenReference reference = identifier.getReference();
            GLSLDefineDirective referent = (reference != null) ? reference.resolve() : null;
            definition = (referent != null) ? referent.getBoundText() : null;
        }

        if (definition == null) {
            return;
        }
        holder.newAnnotation(HighlightSeverity.INFORMATION, definition).textAttributes(GLSLHighlighter.GLSL_REDEFINED_TOKEN[0]).create();
    }

    @NotNull
    @Override
    public Class<GLSLRedefinedToken> getElementType() {
        return GLSLRedefinedToken.class;
    }
}
