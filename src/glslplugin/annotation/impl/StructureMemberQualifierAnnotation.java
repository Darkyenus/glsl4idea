package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.declarations.GLSLQualifier;
import glslplugin.lang.elements.declarations.GLSLStructMemberDeclaration;
import glslplugin.lang.elements.types.GLSLTypeQualifier;
import org.jetbrains.annotations.NotNull;

/**
 * Annotation warning about non-precision qualifiers on struct members, which is not allowed.
 */
public class StructureMemberQualifierAnnotation extends Annotator<GLSLStructMemberDeclaration> {

    @Override
    public void annotate(GLSLStructMemberDeclaration expr, AnnotationHolder holder) {
        for (GLSLQualifier qualifier : expr.getQualifiers()) {
            final GLSLTypeQualifier type = qualifier.getQualifierType();
            if (type == null) {
                continue;
            }
            if (type != GLSLTypeQualifier.PRECISION_QUALIFIER) {
                holder.createWarningAnnotation(qualifier,
                        "GLSL 4.50: Member declarators may contain precision qualifiers, " +
                                "but use of any other qualifier results in a compile-time error." +
                                " (You may put the qualifier on the whole struct.)");
            }
        }
    }

    @NotNull
    @Override
    public Class<GLSLStructMemberDeclaration> getElementType() {
        return GLSLStructMemberDeclaration.class;
    }
}
