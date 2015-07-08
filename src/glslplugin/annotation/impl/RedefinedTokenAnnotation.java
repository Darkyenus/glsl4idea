package glslplugin.annotation.impl;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import glslplugin.GLSLHighlighter;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.GLSLElementTypes;
import glslplugin.lang.elements.GLSLIdentifier;
import glslplugin.lang.elements.preprocessor.GLSLRedefinedToken;
import org.jetbrains.annotations.NotNull;

/**
 * Created by abigail on 08/07/15.
 */
public class RedefinedTokenAnnotation extends Annotator<GLSLRedefinedToken> {
    @Override
    public void annotate(GLSLRedefinedToken identifier, AnnotationHolder holder) {
        PsiReference reference = identifier.getReference();
        PsiElement referent = (reference != null) ? reference.resolve() : null;
        String definition = (referent != null) ? referent.getText() : null;
        Annotation annotation = holder.createInfoAnnotation(identifier, definition);
        annotation.setTextAttributes(GLSLHighlighter.GLSL_REDEFINED_TOKEN[0]);
    }

    @NotNull
    @Override
    public Class<GLSLRedefinedToken> getElementType() {
        return GLSLRedefinedToken.class;
    }
}
