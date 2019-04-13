package glslplugin.lang.elements.types.constructors;

import glslplugin.lang.elements.types.*;
import org.jetbrains.annotations.NotNull;

/**
 * Function type for specific type of matrix and vector constructors.
 *
 * Takes scalars, vectors and optionally matrices (vectors only) and fills resulting
 * vector or matrix with them, element for element.
 *
 * When result type takes N elements, at least N elements must be supplied. There can be more,
 * but there must be no parameters which are not used at all.
 *
 * See GLSL specification 5.4.2 4.50 for details.
 */
public class GLSLAggregateParamConstructor extends GLSLFunctionType {

    private final boolean allowMatrices;
    private final int requiredElements;

    public GLSLAggregateParamConstructor(@NotNull GLSLType returnType, boolean allowMatrices, int requiredElements) {
        super(returnType.getTypename(), returnType, null);
        this.allowMatrices = allowMatrices;
        this.requiredElements = requiredElements;
    }

    @Override
    protected String generateTypename() {
        return getName()+"("+ requiredElements +" scalar elements)";
    }

    @NotNull
    @Override
    public GLSLTypeCompatibilityLevel getParameterCompatibilityLevel(@NotNull GLSLType[] types) {
        if (!allowMatrices && containsMatrixType(types)) {
            return GLSLTypeCompatibilityLevel.INCOMPATIBLE;
        }

        final int numComponents = countVectorOrMatrixConstructorElements(types);
        if (numComponents == -1) {
            return GLSLTypeCompatibilityLevel.INCOMPATIBLE;
        } else if (numComponents == requiredElements) {
            return GLSLTypeCompatibilityLevel.DIRECTLY_COMPATIBLE;
        } else if (numComponents < requiredElements) {
            return GLSLTypeCompatibilityLevel.INCOMPATIBLE;
        } else {
            //Shortening is allowed, but no extra elements can be present
            final int componentsWithoutLast = countVectorOrMatrixConstructorElements(types, types.length - 1);
            if (componentsWithoutLast < requiredElements) {
                //Does not fit into the limit without the last element => valid
                return GLSLTypeCompatibilityLevel.DIRECTLY_COMPATIBLE;
            } else {
                //Last element is extra, that is an error
                return GLSLTypeCompatibilityLevel.INCOMPATIBLE;
            }
        }
    }

    public static boolean containsMatrixType(@NotNull GLSLType[] types){
        for (GLSLType type : types) {
            if (type instanceof GLSLMatrixType) {
                return true;
            }
        }
        return false;
    }

    /**
     * Count the amount of scalar elements in given parameter types.
     * @return Amount of scalars & elements of vectors and matrices or -1 if one of types is not scalar, vector or matrix.
     */
    public static int countVectorOrMatrixConstructorElements(@NotNull GLSLType[] types){
        return countVectorOrMatrixConstructorElements(types, types.length);
    }

    /** @param length only first `length` types will be consumed */
    private static int countVectorOrMatrixConstructorElements(@NotNull GLSLType[] types, int length){
        int numComponents = 0;
        for (int i = 0; i < length; i++) {
            final GLSLType type = types[i];

            if (GLSLTypes.isScalar(type)) {
                numComponents++;
            } else if (type instanceof GLSLVectorType) {
                numComponents += ((GLSLVectorType) type).getNumComponents();
            } else if (type instanceof GLSLMatrixType) {
                numComponents += ((GLSLMatrixType) type).getNumComponents();
            } else {
                return -1;
            }
        }
        return numComponents;
    }
}
