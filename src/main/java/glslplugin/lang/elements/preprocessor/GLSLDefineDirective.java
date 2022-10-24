package glslplugin.lang.elements.preprocessor;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.reference.GLSLReferencableDeclaration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * Created by abigail on 08/07/15.
 */
public class GLSLDefineDirective extends GLSLPreprocessorDirective implements GLSLReferencableDeclaration {
    public GLSLDefineDirective(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        return findChildByType(GLSLTokenTypes.IDENTIFIER);
    }

    @NotNull
    @Override
    public String getName() {
        PsiElement nameIdentifier = getNameIdentifier();
        if (nameIdentifier == null) return "";
        return nameIdentifier.getText();
    }

    @Override
    public int getTextOffset() {
        final PsiElement identifier = getNameIdentifier();
        return identifier != null ? identifier.getTextOffset() : super.getTextOffset();
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
    public @NotNull String declaredNoun() {
        return "redefined token";
    }

    @Override
    public String toString() {
        return "#Define Directive "+getName()+" => "+getBoundText();
    }
}
