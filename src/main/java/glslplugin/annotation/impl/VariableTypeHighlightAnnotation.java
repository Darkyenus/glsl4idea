package glslplugin.annotation.impl;

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
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION).textAttributes(GLSLHighlighter.GLSL_IDENTIFIER_UNIFORM[0]).create();
        } else if (qualifiedType.hasQualifier(GLSLQualifier.Qualifier.VARYING)) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION).textAttributes(GLSLHighlighter.GLSL_IDENTIFIER_VARYING[0]).create();
        } else if (qualifiedType.hasQualifier(GLSLQualifier.Qualifier.ATTRIBUTE)) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION).textAttributes(GLSLHighlighter.GLSL_IDENTIFIER_ATTRIBUTE[0]).create();
        }
    }

    @NotNull
    @Override
    public Class<GLSLIdentifierExpression> getElementType() {
        return GLSLIdentifierExpression.class;
    }
}
