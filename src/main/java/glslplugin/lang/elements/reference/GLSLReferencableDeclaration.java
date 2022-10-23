package glslplugin.lang.elements.reference;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.declarations.GLSLQualifiedDeclaration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for elements, that are declarations to which other elements can refer to.
 * This differs from {@link GLSLQualifiedDeclaration},
 * which is a declaration from a language perspective, but not from PSI perspective.
 */
public interface GLSLReferencableDeclaration extends GLSLElement, PsiNameIdentifierOwner {

    @Nullable
    String getName();

    PsiElement setName(@NotNull String name) throws IncorrectOperationException;

    @Override
    default int getTextOffset() {
        final PsiElement identifier = getNameIdentifier();
        if (identifier == null) {

        }
        return 0;//TODO
    }

    /**
     * @see com.intellij.lang.findUsages.FindUsagesProvider#getType(PsiElement)
     */
    @NotNull
    String declaredNoun();

}
