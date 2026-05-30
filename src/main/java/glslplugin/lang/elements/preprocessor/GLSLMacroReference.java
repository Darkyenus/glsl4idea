package glslplugin.lang.elements.preprocessor;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.ArrayUtilRt;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.reference.GLSLReferenceUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class GLSLMacroReference extends PsiReferenceBase<PsiElement> {
    private final @NotNull String tokenName;

    public GLSLMacroReference(@NotNull PsiElement element, @NotNull PsiElement identifier) {
        super(element, GLSLReferenceUtil.rangeOfIn(identifier, element), false);
        tokenName = Objects.requireNonNullElse(GLSLElement.text(identifier), "");
    }

    @Override
    public @Nullable GLSLDefineDirective resolve() {
        return GLSLDefineDirective.findActiveDefinitionBefore(getElement(), tokenName);
    }

    @Override
    public Object @NotNull [] getVariants() {
        return ArrayUtilRt.EMPTY_OBJECT_ARRAY;
    }
}
