package glslplugin.lang.elements.reference;

import glslplugin.lang.elements.preprocessor.GLSLPreprocessorDirective;
import glslplugin.lang.elements.preprocessor.GLSLRedefinedToken;

/**
 * Created by abigail on 08/07/15.
 */
public class GLSLMacroReference extends GLSLReferenceBase<GLSLRedefinedToken, GLSLPreprocessorDirective> {
    public GLSLMacroReference(GLSLRedefinedToken source, GLSLPreprocessorDirective target) {
        super(source, target);
    }
}
