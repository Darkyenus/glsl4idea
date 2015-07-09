package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.GLSLReferenceElement;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.declarations.GLSLQualifier;
import glslplugin.lang.elements.expressions.GLSLAssignmentExpression;
import glslplugin.lang.elements.expressions.GLSLExpression;
import org.jetbrains.annotations.NotNull;

/**
 * Created by abigail on 09/07/15.
 */
public class ConstModificationAnnotation extends Annotator<GLSLAssignmentExpression> {
    @Override
    public void annotate(GLSLAssignmentExpression assignment, AnnotationHolder holder) {
        GLSLExpression expr = assignment.getLeftOperand();
        if (expr == null) return;

        PsiReference reference = expr.getReference();
        if (reference == null && expr instanceof GLSLReferenceElement)
            reference = ((GLSLReferenceElement) expr).getReferenceProxy();

        if (reference == null) return;

        PsiElement declarator = reference.resolve();
        if (declarator == null || !(declarator instanceof GLSLDeclarator)) return;

        if (((GLSLDeclarator) declarator).getQualifiedType().hasQualifier(GLSLQualifier.Qualifier.CONST)) {
            holder.createErrorAnnotation(expr, "Cannot assign to const variable");
        }
    }

    @NotNull
    @Override
    public Class<GLSLAssignmentExpression> getElementType() {
        return GLSLAssignmentExpression.class;
    }
}
