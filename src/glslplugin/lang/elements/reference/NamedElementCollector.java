package glslplugin.lang.elements.reference;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.BaseScopeProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by abigail on 06/07/15.
 */
public class NamedElementCollector extends BaseScopeProcessor {

    private final Collection<PsiNamedElement> identifiers;

    public NamedElementCollector(Collection<PsiNamedElement> identifiers) {
        this.identifiers = identifiers;
    }

    @Override
    public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state) {
        if (element instanceof PsiNamedElement) identifiers.add((PsiNamedElement) element);
        return true;
    }
}
