package glslplugin.lang.elements.preprocessor;

import com.intellij.lang.ASTNode;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.reference.GLSLMacroReference;
import org.jetbrains.annotations.NotNull;

/**
 *
 * Created by abigail on 08/07/15.
 */
public class GLSLRedefinedToken extends GLSLElementImpl {
    public GLSLRedefinedToken(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Override
    public GLSLMacroReference getReference() {
        return new GLSLMacroReference(this);
    }

    @Override
    @NotNull
    public String getName() {
        return getText();
    }

}
