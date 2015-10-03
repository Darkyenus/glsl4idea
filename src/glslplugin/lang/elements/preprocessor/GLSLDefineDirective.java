package glslplugin.lang.elements.preprocessor;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import glslplugin.lang.elements.GLSLPsiElementFactory;
import glslplugin.lang.elements.GLSLTokenTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * Created by abigail on 08/07/15.
 */
public class GLSLDefineDirective extends GLSLPreprocessorDirective implements PsiNameIdentifierOwner {
    public GLSLDefineDirective(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        PsiElement child = getFirstChild();
        while (child != null) { // we can't iterate over getChildren(), as that ignores leaf elements
            if (child.getNode().getElementType() == GLSLTokenTypes.IDENTIFIER) return child;
            child = child.getNextSibling();
        }
        return null;
    }

    @Override
    public String getName() {
        PsiElement nameIdentifier = getNameIdentifier();
        return (nameIdentifier != null) ? nameIdentifier.getText() : null;
    }

    /**
     * @return text after the name
     */
    @NotNull
    public String getBoundText(){
        final PsiElement name = getNameIdentifier();
        if(name == null)return "";

        int textStart = name.getTextOffset() + name.getTextLength();
        int textEnd = getTextOffset() + getTextLength();
        final String text = getContainingFile().getText();
        if(textStart >= textEnd || textStart < 0 || textEnd > text.length()) return "";
        return text.substring(textStart, textEnd).trim();
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        final PsiElement oldName = getNameIdentifier();
        if (oldName == null) throw new IncorrectOperationException();
        PsiElement newName = GLSLPsiElementFactory.createLeafElement(getProject(), name);
        getNode().replaceChild(oldName.getNode(), newName.getNode());
        return this;
    }

    @Override
    public String toString() {
        return "#Define Directive "+getName()+" => "+getBoundText();
    }
}
