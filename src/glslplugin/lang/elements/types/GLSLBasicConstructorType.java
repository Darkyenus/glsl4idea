package glslplugin.lang.elements.types;

import glslplugin.lang.elements.declarations.GLSLTypeDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * FunctionType for most constructors (= not functions)
 */
public class GLSLBasicConstructorType extends GLSLFunctionType {

    private final GLSLType[] parameterTypes;

    public GLSLBasicConstructorType(@Nullable GLSLTypeDefinition definition, @NotNull GLSLType returnType, @NotNull GLSLType... parameterTypes) {
        super(returnType.getTypename(), returnType);
        this.parameterTypes = parameterTypes;
        this.definition = definition;
    }

    protected String generateTypename() {
        StringBuilder b = new StringBuilder();
        b.append(getName()).append('(');
        boolean first = true;
        for (GLSLType type : parameterTypes) {
            if (!first) {
                b.append(',');
            }
            first = false;
            b.append(type.getTypename());
        }
        b.append(")");
        return b.toString();
    }

    @NotNull
    public GLSLTypeCompatibilityLevel getParameterCompatibilityLevel(@NotNull GLSLType[] types) {
        return GLSLTypeCompatibilityLevel.getCompatibilityLevel(types, parameterTypes);
    }

    @NotNull
    public GLSLType[] getParameterTypes() {
        return parameterTypes;
    }
}
