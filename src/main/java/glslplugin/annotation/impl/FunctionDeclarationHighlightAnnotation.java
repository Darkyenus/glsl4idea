package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.TextRange;
import glslplugin.GLSLHighlighter;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.declarations.GLSLFunctionDeclaration;
import org.jetbrains.annotations.NotNull;

public class FunctionDeclarationHighlightAnnotation extends Annotator<GLSLFunctionDeclaration> {
    @Override
    public void annotate(GLSLFunctionDeclaration declaration, AnnotationHolder holder) {
        final String functionName = declaration.getFunctionName();
        if (functionName == null) return;
        final int startOffset = declaration.getTextOffset();
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(new TextRange(startOffset, startOffset + functionName.length()))
                .textAttributes(GLSLHighlighter.GLSL_FUNCTION_DECLARATION[0])
                .create();
    }

    @NotNull
    @Override
    public Class<GLSLFunctionDeclaration> getElementType() {
        return GLSLFunctionDeclaration.class;
    }
}
