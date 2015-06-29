package glslplugin.extensions;

import com.intellij.psi.ElementDescriptionLocation;
import com.intellij.psi.ElementDescriptionProvider;
import com.intellij.psi.PsiElement;
import com.intellij.usageView.UsageViewLongNameLocation;
import com.intellij.usageView.UsageViewNodeTextLocation;
import com.intellij.usageView.UsageViewTypeLocation;
import glslplugin.lang.elements.declarations.GLSLDeclaration;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by abigail on 28/06/15.
 */
public class GLSLDescriptionProvider implements ElementDescriptionProvider {
    @Override
    @Nullable
    public String getElementDescription(@NotNull PsiElement element, @NotNull ElementDescriptionLocation location) {
        if (!(element instanceof GLSLDeclarator)) return null;
        GLSLDeclarator declarator = (GLSLDeclarator) element;
        GLSLDeclaration declaration = declarator.getParentDeclaration();
        if (declaration == null) return null;

        if (location instanceof UsageViewTypeLocation) {
            return declaration.getDeclarationDescription();
        }

        if (location instanceof UsageViewLongNameLocation) {
            return declaration.getDeclarationDescription() + " '" + declarator.getName() + "'";
        }

        if (location instanceof UsageViewNodeTextLocation) {
            return declarator.getName();
        }
        return null;
//        return location.toString();
    }
}
