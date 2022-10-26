package glslplugin.annotation.impl;

import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLTokenTypes;
import org.jetbrains.annotations.NotNull;

/**
 *
 * Created by abigail on 23/06/15.
 */
public class ReservedIdentifierAnnotation extends Annotator<LeafPsiElement> {

    @Override
    public void annotate(LeafPsiElement identifier, AnnotationHolder holder) {
        if (identifier.getElementType() != GLSLTokenTypes.IDENTIFIER) {
            return;
        }
        String name = GLSLElement.nodeText(identifier);
        if (name.startsWith("__")) {
            holder.newAnnotation(HighlightSeverity.WARNING, "This identifier is reserved for use by underlying software layers and may result in undefined behavior.").create();
        }
    }

    @NotNull
    @Override
    public Class<LeafPsiElement> getElementType() {
        return LeafPsiElement.class;
    }
}
