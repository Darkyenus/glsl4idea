package glslplugin.lang.elements.preprocessor;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.ArrayUtilRt;
import com.intellij.util.IncorrectOperationException;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.reference.GLSLReferencableDeclaration;
import glslplugin.lang.elements.reference.GLSLReferenceUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class GLSLMacroReference extends PsiReferenceBase<PsiElement> {
    private final @NotNull PsiElement identifier;
    private final @NotNull String tokenName;

    public GLSLMacroReference(@NotNull PsiElement element, @NotNull PsiElement identifier) {
        super(element, GLSLReferenceUtil.rangeOfIn(identifier, element), false);
        this.identifier = identifier;
        tokenName = Objects.requireNonNullElse(GLSLElement.text(identifier), "");
    }

    @Override
    public @Nullable GLSLDefineDirective resolve() {
        return GLSLDefineDirective.findActiveDefinitionBefore(getElement(), tokenName);
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        GLSLReferencableDeclaration.replaceIdentifier(identifier, newElementName);
        return getElement();
    }

    @Override
    public Object @NotNull [] getVariants() {
        return ArrayUtilRt.EMPTY_OBJECT_ARRAY;
    }
}
