package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.declarations.GLSLParameterDeclaration;
import glslplugin.lang.elements.declarations.GLSLQualifier;
import glslplugin.lang.elements.types.GLSLType;
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
            if (qualifierType == GLSLTypeQualifier.PRECISION_QUALIFIER
                    || qualifierType == GLSLTypeQualifier.MEMORY_QUALIFIER
                    || qualifier.getQualifier() == GLSLQualifier.Qualifier.CONST
                    || qualifier.getQualifier() == GLSLQualifier.Qualifier.IN
                    || qualifier.getQualifier() == GLSLQualifier.Qualifier.OUT
                    || qualifier.getQualifier() == GLSLQualifier.Qualifier.INOUT) continue;

            holder.createErrorAnnotation(parameter, "'" + qualifier.getQualifier().toString() + "' qualifier not allowed. " +
            "Expected precision qualifier, memory qualifier, 'const', 'in', 'out', or 'inout'.");
        }
    }

    @NotNull
    @Override
    public Class<GLSLParameterDeclaration> getElementType() {
        return GLSLParameterDeclaration.class;
    }
}
