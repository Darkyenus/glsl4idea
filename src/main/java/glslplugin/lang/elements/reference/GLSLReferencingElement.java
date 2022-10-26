package glslplugin.lang.elements.reference;

import com.intellij.psi.PsiElement;
import glslplugin.lang.elements.GLSLElement;
import org.jetbrains.annotations.Nullable;

/**
 * An element which references something.
 */
public interface GLSLReferencingElement extends PsiElement {

    /** The identifier which actually creates the reference. Used only for automatic renaming. */
    @Nullable
    PsiElement getReferencingIdentifierForRenaming();

}
