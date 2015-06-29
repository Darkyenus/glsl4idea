package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.GLSLIdentifier;
import org.jetbrains.annotations.NotNull;

/**
 *
 * Created by abigail on 23/06/15.
 */
public class ReservedIdentifierAnnotation extends Annotator<GLSLIdentifier> {

    @Override
    public void annotate(GLSLIdentifier identifier, AnnotationHolder holder) {
        String name = identifier.getName();
        if (name != null && name.startsWith("__")) {
            holder.createWarningAnnotation(identifier, "This identifier is reserved for use by underlying software layers and may result in undefined behavior.");
        }
    }

    @NotNull
    @Override
    public Class<GLSLIdentifier> getElementType() {
        return GLSLIdentifier.class;
    }
}
