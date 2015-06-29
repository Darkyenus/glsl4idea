package glslplugin.lang.elements.expressions;

import com.intellij.lang.ASTNode;
import glslplugin.lang.elements.declarations.GLSLTypeSpecifier;
import glslplugin.lang.elements.types.GLSLArrayType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Constructor expression looks a bit like function call, but instead of function identifier,
 * there is a type identifier and it can be followed by one or more array declarators ("[]").
 *
 * @author Darkyen
 */
public class GLSLConstructorExpression extends GLSLExpression {

    public GLSLConstructorExpression(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    public GLSLParameterList getParameterList() {
        return findChildByClass(GLSLParameterList.class);
    }

    @NotNull
    @Override
    public GLSLType getType() {
        GLSLTypeSpecifier typeSpecifier = findChildByClass(GLSLTypeSpecifier.class);
        if (typeSpecifier == null) return GLSLTypes.UNKNOWN_TYPE;
        GLSLType specifiedType = typeSpecifier.getType();
        GLSLParameterList parameterList = getParameterList();
        if (specifiedType instanceof GLSLArrayType && parameterList != null) {
            //That array may be implicitly sized, if so - clarify it
            GLSLArrayType arrayType = (GLSLArrayType) specifiedType;
            final int[] dimensions = arrayType.getDimensions();
            if (dimensions.length >= 1) {
                if (dimensions[0] == GLSLArrayType.UNDEFINED_SIZE_DIMENSION) {
                    dimensions[0] = parameterList.getParameters().length;
                }
            }
            for (int i = 1; i < dimensions.length; i++) {
                if (dimensions[i] == GLSLArrayType.UNDEFINED_SIZE_DIMENSION) {
                    //Clarify further
                    //TODO
                }
            }
        }
        return specifiedType;
    }

    @Override
    public String toString() {
        return "Constructor call: " + getType();
    }
}