package glslplugin.lang.elements.preprocessor;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.GLSLTokenTypes;
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
        //Explicitly asking for IDENTIFIER works around corner-case
        // when two replaced tokens are right next to each other and second becomes child of first

        //It shouldn't happen anymore though
        final PsiElement name = findChildByType(GLSLTokenTypes.IDENTIFIER);
        if(name == null)return getText();
        return name.getText();
    }

}
