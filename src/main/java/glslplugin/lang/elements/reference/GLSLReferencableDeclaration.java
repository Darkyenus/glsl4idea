package glslplugin.lang.elements.reference;

import com.intellij.psi.PsiElement;
import glslplugin.lang.elements.GLSLElement;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for elements, that are declarations to which other elements can refer to.
 * This differs from {@link glslplugin.lang.elements.declarations.GLSLDeclaration},
 * which is a declaration from a language perspective, but not from PSI perspective.
 */
public interface GLSLReferencableDeclaration extends GLSLElement {

    /**
     * @see com.intellij.lang.findUsages.FindUsagesProvider#getType(PsiElement)
     */
    @NotNull
    String declaredNoun();

}
