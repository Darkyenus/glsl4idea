package glslplugin.lang.elements.reference;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Created by abigail on 06/07/15.
 */
public class NamedElementCollector implements PsiScopeProcessor {

    private final Collection<PsiNamedElement> identifiers;

    public NamedElementCollector(Collection<PsiNamedElement> identifiers) {
        this.identifiers = identifiers;
    }

    @Override
    public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state) {
        if (element instanceof PsiNamedElement) {
            identifiers.add((PsiNamedElement) element);
        }
        return true;
    }

    // Following methods are implemented just for backwards compatibility
    @Nullable
    @Override
    public <T> T getHint(@NotNull Key<T> hintKey) {
        return null;
    }

    @Override
    public void handleEvent(@NotNull Event event, @Nullable Object associated) {}
}
