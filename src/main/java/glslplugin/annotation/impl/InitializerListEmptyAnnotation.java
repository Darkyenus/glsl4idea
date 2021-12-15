package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.declarations.GLSLInitializerList;
import org.jetbrains.annotations.NotNull;

/**
 * Checks for empty initializer lists.
 *
 * Created by abigail on 28/06/15.
 */
public class InitializerListEmptyAnnotation extends Annotator<GLSLInitializerList> {
    @Override
    public void annotate(GLSLInitializerList expr, AnnotationHolder holder) {
        if (expr.getInitializers().length == 0) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Empty initializer lists are not allowed").create();
        }
    }

    @NotNull
    @Override
    public Class<GLSLInitializerList> getElementType() {
        return GLSLInitializerList.class;
    }
}
