package glslplugin.lang.elements.types.constructors;

import glslplugin.lang.elements.types.GLSLFunctionType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypeCompatibilityLevel;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;

/**
 * Function type for specific type of matrix and vector constructors.
 *
 * Takes one scalar and:
 * - For vector fills all elements with that scalar
 * - For matrix fills diagonal starting at 0,0 with that scalar
 *
 * See GLSL specification 4.50 5.4.2 for details.
 */
public class GLSLScalarParamConstructor extends GLSLFunctionType {

    public GLSLScalarParamConstructor(@NotNull GLSLType returnType) {
        super(returnType.getTypename(), returnType, null);
    }

    @Override
    protected String generateTypename() {
        return getName()+"(scalar)";
    }

    @NotNull
    @Override
    public GLSLTypeCompatibilityLevel getParameterCompatibilityLevel(@NotNull GLSLType[] types) {
        if (types.length == 1 && GLSLTypes.isScalar(types[0])){
            return GLSLTypeCompatibilityLevel.DIRECTLY_COMPATIBLE;
        } else {
            return GLSLTypeCompatibilityLevel.INCOMPATIBLE;
        }
    }
}
