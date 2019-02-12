package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.declarations.GLSLParameterDeclaration;
import glslplugin.lang.elements.declarations.GLSLQualifier;
import glslplugin.lang.elements.types.GLSLTypeQualifier;
import org.jetbrains.annotations.NotNull;

/**
 * Created by abigail on 10/07/15.
 */
public class ParameterQualifierAnnotation extends Annotator<GLSLParameterDeclaration> {
    @Override
    public void annotate(GLSLParameterDeclaration parameter, AnnotationHolder holder) {
        for (GLSLQualifier qualifier : parameter.getQualifiers()) {
            GLSLTypeQualifier qualifierType = qualifier.getQualifierType();
            final GLSLQualifier.Qualifier q = qualifier.getQualifier();

            if (q == null
                    || qualifierType == GLSLTypeQualifier.PRECISION_QUALIFIER
                    || qualifierType == GLSLTypeQualifier.MEMORY_QUALIFIER
                    || q == GLSLQualifier.Qualifier.CONST
                    || q == GLSLQualifier.Qualifier.IN
                    || q == GLSLQualifier.Qualifier.OUT
                    || q == GLSLQualifier.Qualifier.INOUT) continue;

            holder.createErrorAnnotation(parameter, "'" + q.toString() + "' qualifier not allowed. " +
            "Expected precision qualifier, memory qualifier, 'const', 'in', 'out', or 'inout'.");
        }
    }

    @NotNull
    @Override
    public Class<GLSLParameterDeclaration> getElementType() {
        return GLSLParameterDeclaration.class;
    }
}
