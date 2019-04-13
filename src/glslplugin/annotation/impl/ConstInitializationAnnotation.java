package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.declarations.GLSLQualifier;
import glslplugin.lang.elements.declarations.GLSLVariableDeclaration;
import org.jetbrains.annotations.NotNull;

/**
 * Created by abigail on 09/07/15.
 */
public class ConstInitializationAnnotation extends Annotator<GLSLDeclarator> {
    @Override
    public void annotate(GLSLDeclarator declarator, AnnotationHolder holder) {
        if (declarator.findParentByClass(GLSLVariableDeclaration.class) == null) {
            return;
        }
        if (declarator.getInitializer() != null || declarator.getInitializerExpression() != null) {
            return;
        }

        if (declarator.getQualifiedType().hasQualifier(GLSLQualifier.Qualifier.CONST)) {
            holder.createErrorAnnotation(declarator, "Const variable declared with no initializer");
        }
    }

    @NotNull
    @Override
    public Class<GLSLDeclarator> getElementType() {
        return GLSLDeclarator.class;
    }
}
