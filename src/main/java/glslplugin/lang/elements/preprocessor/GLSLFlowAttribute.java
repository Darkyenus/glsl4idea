package glslplugin.lang.elements.preprocessor;

import com.intellij.lang.ASTNode;
import glslplugin.lang.elements.GLSLElementImpl;
import org.jetbrains.annotations.NotNull;

/**
 * See https://github.com/KhronosGroup/GLSL/blob/master/extensions/ext/GL_EXT_control_flow_attributes.txt
 */
public class GLSLFlowAttribute extends GLSLElementImpl {

    public GLSLFlowAttribute(@NotNull ASTNode astNode) {
        super(astNode);
    }

}
