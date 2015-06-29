package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
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
        if (expr.getInitializers().size() == 0) {
            holder.createErrorAnnotation(expr, "Empty initializer lists are not allowed");
        }
    }

    @NotNull
    @Override
    public Class<GLSLInitializerList> getElementType() {
        return GLSLInitializerList.class;
    }
}
