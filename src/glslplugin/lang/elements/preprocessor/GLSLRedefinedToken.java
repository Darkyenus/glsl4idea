package glslplugin.lang.elements.preprocessor;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.GLSLPsiElementFactory;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.reference.GLSLMacroReference;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * Created by abigail on 08/07/15.
 */
public class GLSLRedefinedToken extends GLSLElementImpl implements PsiNameIdentifierOwner {

    public GLSLRedefinedToken(@NotNull ASTNode astNode) {
        super(astNode);
    }

    /**
     * @return reference to the #define which caused the redefinition of this token
     */
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
        final PsiElement name = getNameIdentifier();
        if (name == null) {
            return getText();
        }
        return name.getText();
    }

    @Override
    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        //TODO Check name by spec
        final PsiElement currentIdentifier = getNameIdentifier();
        if (currentIdentifier == null) {
            throw new IncorrectOperationException("Invalid reference");
        }
        PsiElement newName = GLSLPsiElementFactory.createLeafElement(getProject(), name);
        getNode().replaceChild(currentIdentifier.getNode(), newName.getNode());
        return newName;
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        return findChildByType(GLSLTokenTypes.IDENTIFIER);
    }
}
