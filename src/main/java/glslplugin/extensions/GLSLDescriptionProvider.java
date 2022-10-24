package glslplugin.extensions;

import com.intellij.psi.ElementDescriptionLocation;
import com.intellij.psi.ElementDescriptionProvider;
import com.intellij.psi.PsiElement;
import com.intellij.usageView.UsageViewLongNameLocation;
import com.intellij.usageView.UsageViewNodeTextLocation;
import com.intellij.usageView.UsageViewTypeLocation;
import glslplugin.lang.elements.declarations.GLSLQualifiedDeclaration;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * Created by abigail on 28/06/15.
 */
public class GLSLDescriptionProvider implements ElementDescriptionProvider {
    @Override
    @Nullable
    public String getElementDescription(@NotNull PsiElement element, @NotNull ElementDescriptionLocation location) {
        if (!(element instanceof GLSLDeclarator declarator)) return null;

        if (location instanceof UsageViewTypeLocation) {
            return declarator.declaredNoun();
        }

        if (location instanceof UsageViewLongNameLocation) {
            return declarator.getHierarchicalVariableName();
        }

        if (location instanceof UsageViewNodeTextLocation) {
            return declarator.getVariableName();
        }
        return null;
//        return location.toString();
    }
}
