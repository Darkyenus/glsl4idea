package glslplugin.annotation.impl;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import glslplugin.GLSLHighlighter;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.declarations.GLSLQualifier;
import glslplugin.lang.elements.expressions.GLSLIdentifierExpression;
import glslplugin.lang.elements.types.GLSLQualifiedType;
import org.jetbrains.annotations.NotNull;

/**
 * Highlight-only annotation for variable references which are uniforms or varyings.
 * Highlighting style is the same as normal variables by default, so for this to be visible, new style must be selected manually.
 */
public class VariableTypeHighlightAnnotation extends Annotator<GLSLIdentifierExpression> {

    @Override
    public void annotate(GLSLIdentifierExpression expr, AnnotationHolder holder) {
        final GLSLDeclarator declarator = expr.getReferenceProxy().resolve();
        if (declarator == null) return;
        final GLSLQualifiedType qualifiedType = declarator.getQualifiedType();
        if (qualifiedType.hasQualifier(GLSLQualifier.Qualifier.UNIFORM)) {
            Annotation annotation = holder.createAnnotation(HighlightSeverity.INFORMATION, expr.getTextRange(), null);
            annotation.setTextAttributes(GLSLHighlighter.GLSL_IDENTIFIER_UNIFORM[0]);
        } else if (qualifiedType.hasQualifier(GLSLQualifier.Qualifier.VARYING)) {
            Annotation annotation = holder.createAnnotation(HighlightSeverity.INFORMATION, expr.getTextRange(), null);
            annotation.setTextAttributes(GLSLHighlighter.GLSL_IDENTIFIER_VARYING[0]);
        }
    }

    @NotNull
    @Override
    public Class<GLSLIdentifierExpression> getElementType() {
        return GLSLIdentifierExpression.class;
    }
}
