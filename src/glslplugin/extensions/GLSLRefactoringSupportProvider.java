package glslplugin.extensions;

import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;

/**
 * Created by abigail on 26/06/15.
 */
public class GLSLRefactoringSupportProvider extends RefactoringSupportProvider {
    @Override
    public boolean isMemberInplaceRenameAvailable(PsiElement element, PsiElement context) {
        return element instanceof PsiNamedElement;
    }
}
