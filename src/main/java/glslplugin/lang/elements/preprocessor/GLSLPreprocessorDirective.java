package glslplugin.lang.elements.preprocessor;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ArrayUtilRt;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.reference.GLSLReferenceUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by abigail on 08/07/15.
 */
public class GLSLPreprocessorDirective extends GLSLElementImpl {
    public GLSLPreprocessorDirective(@NotNull ASTNode astNode) {
        super(astNode);
    }

    private @NotNull List<PsiElement> getMacroReferenceIdentifiers() {
        final IElementType directiveType = getNode().getElementType();
        if (directiveType != GLSLTokenTypes.PREPROCESSOR_IF
            && directiveType != GLSLTokenTypes.PREPROCESSOR_IFDEF
            && directiveType != GLSLTokenTypes.PREPROCESSOR_IFNDEF
            && directiveType != GLSLTokenTypes.PREPROCESSOR_ELIF
            && directiveType != GLSLTokenTypes.PREPROCESSOR_UNDEF) {
            return List.of();
        }

        final ArrayList<PsiElement> result = new ArrayList<>();
        for (PsiElement child = getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getNode().getElementType() == GLSLTokenTypes.IDENTIFIER) {
                result.add(child);
                if (directiveType == GLSLTokenTypes.PREPROCESSOR_IFDEF
                    || directiveType == GLSLTokenTypes.PREPROCESSOR_IFNDEF
                    || directiveType == GLSLTokenTypes.PREPROCESSOR_UNDEF) {
                    break;
                }
            }
        }
        return result;
    }

    boolean isUndefDirectiveFor(@NotNull String tokenName) {
        if (getNode().getElementType() != GLSLTokenTypes.PREPROCESSOR_UNDEF) return false;

        final List<PsiElement> identifiers = getMacroReferenceIdentifiers();
        return !identifiers.isEmpty() && tokenName.equals(GLSLElement.text(identifiers.get(0)));
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        final List<PsiElement> identifiers = getMacroReferenceIdentifiers();
        if (identifiers.isEmpty()) return PsiReference.EMPTY_ARRAY;

        final PsiReference[] references = new PsiReference[identifiers.size()];
        for (int i = 0; i < identifiers.size(); i++) {
            references[i] = new MacroReference(this, identifiers.get(i));
        }
        return references;
    }

    @Override
    public @Nullable PsiReference getReference() {
        final PsiReference[] references = getReferences();
        return references.length == 1 ? references[0] : null;
    }

    private static final class MacroReference extends PsiReferenceBase<GLSLPreprocessorDirective> {
        private final @NotNull String tokenName;

        private MacroReference(@NotNull GLSLPreprocessorDirective directive, @NotNull PsiElement identifier) {
            super(directive, GLSLReferenceUtil.rangeOfIn(identifier, directive), false);
            tokenName = GLSLElement.text(identifier);
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
}
