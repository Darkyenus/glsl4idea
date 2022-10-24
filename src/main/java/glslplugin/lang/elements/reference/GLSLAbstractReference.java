package glslplugin.lang.elements.reference;

import com.intellij.diagnostic.PluginException;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Custom variant of {@link com.intellij.psi.PsiReferenceBase}
 */
public abstract class GLSLAbstractReference<T extends GLSLReferencingElement> implements PsiReference {

    private static final Logger LOG = Logger.getInstance(PsiReferenceBase.class);

    protected final T element;
    private TextRange rangeInElement;

    /**
     * @param element which is the reference. {@link GLSLReferencingElement#getReferencingIdentifierForRenaming()} is used as source text range.
     */
    public GLSLAbstractReference(@NotNull T element) {
        this.element = element;
        this.rangeInElement = GLSLReferenceUtil.rangeOfIn(element.getReferencingIdentifierForRenaming(), element);
    }

    /**
     * @param element        which is the reference
     * @param rangeInElement Reference range {@link PsiReference#getRangeInElement() relative to given element}. Must be fully contained in range of {@link GLSLReferencingElement#getReferencingIdentifierForRenaming()}.
     */
    public GLSLAbstractReference(@NotNull T element, @NotNull TextRange rangeInElement) {
        this.element = element;
        this.rangeInElement = rangeInElement;
    }

    @NotNull
    @Override
    public T getElement() {
        return element;
    }

    @NotNull
    @Override
    public TextRange getRangeInElement() {
        return rangeInElement;
    }

    @Override
    public @NotNull String getCanonicalText() {
        String text = element.getText();
        final TextRange range = rangeInElement;
        try {
            return range.substring(text);
        }
        catch (StringIndexOutOfBoundsException e) {
            LOG.error("Wrong range in reference " + this + ": " + range + ". Reference text: '" + text + "'", e);
            return text;
        }
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        final PsiElement identifier = element.getReferencingIdentifierForRenaming();
        if (identifier == null) {
            throw new IncorrectOperationException(element+" can't be renamed");
        }
        final TextRange identifierRange = GLSLReferenceUtil.rangeOfIn(identifier, element);
        final TextRange renameRange = rangeInElement;

        final String newCombinedName;
        if (identifierRange.equals(renameRange)) {
            newCombinedName = newElementName;
        } else if (!identifierRange.contains(renameRange)) {
            throw new IncorrectOperationException("Can't rename, rename range "+rangeInElement+" exceeds the identifier range "+identifierRange+" in '"+element.getText()+"' ");
        } else {
            final String oldName = identifier.getText();
            final String keptPrefix = oldName.substring(0, renameRange.getStartOffset() - identifierRange.getStartOffset());
            final String keptSuffix = oldName.substring(renameRange.getEndOffset() - identifierRange.getStartOffset());
            newCombinedName = keptPrefix + newElementName + keptSuffix;
        }
        final TextRange newRange = new TextRange(renameRange.getStartOffset(), renameRange.getStartOffset() + newCombinedName.length());

        GLSLReferencableDeclaration.replaceIdentifier(identifier, newCombinedName);
        rangeInElement = newRange;
        return element;
    }

    @Override
    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        Class<?> aClass = getClass();
        throw new IncorrectOperationException("Rebind cannot be performed for " + aClass,
                (Throwable) PluginException.createByClass("", null, aClass));
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        return getElement().getManager().areElementsEquivalent(resolve(), element);
    }

    @Override
    public boolean isSoft() {
        return false;
    }

    @Override
    public String toString() {
        return GLSLReferenceUtil.toString(this);
    }

    /** Variant of {@link PsiPolyVariantReferenceBase} */
    public static abstract class Poly<T extends GLSLReferencingElement> extends GLSLAbstractReference<T> implements PsiPolyVariantReference {

        public Poly(@NotNull T element) {
            super(element);
        }

        public Poly(@NotNull T element, @NotNull TextRange rangeInElement) {
            super(element, rangeInElement);
        }

        @Override
        @Nullable
        public PsiElement resolve() {
            ResolveResult[] resolveResults = multiResolve(false);
            return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
        }

        @Override
        public boolean isReferenceTo(@NotNull PsiElement element) {
            final ResolveResult[] results = multiResolve(false);
            for (ResolveResult result : results) {
                if (getElement().getManager().areElementsEquivalent(result.getElement(), element)) {
                    return true;
                }
            }
            return false;
        }
    }
}
