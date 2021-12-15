package glslplugin.lang.elements.reference;

import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLIdentifier;
import glslplugin.lang.elements.expressions.GLSLFunctionCallExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A reference returned by GLSLFunctionCallExpression when it represents a constructor
 *
 * @author Jan Polák
 */
public class GLSLConstructorReference extends GLSLReferenceBase<GLSLIdentifier, GLSLElement> {

    private final GLSLFunctionCallExpression source;

    public GLSLConstructorReference(@NotNull GLSLFunctionCallExpression source) {
        super(source.getIdentifier());
        this.source = source;
    }

    @Nullable
    @Override
    public GLSLElement resolve() {
        return source.getType().getBaseType().getDefinition();
    }
}
