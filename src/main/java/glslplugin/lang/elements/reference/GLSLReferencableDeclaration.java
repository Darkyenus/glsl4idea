package glslplugin.lang.elements.reference;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiCheckedRenameElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.util.IncorrectOperationException;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLPsiElementFactory;
import glslplugin.lang.elements.declarations.GLSLQualifiedDeclaration;
import glslplugin.lang.parser.GLSLFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for elements, that are declarations to which other elements can refer to.
 * This differs from {@link GLSLQualifiedDeclaration},
 * which is a declaration from a language perspective, but not from PSI perspective.
 */
public interface GLSLReferencableDeclaration extends GLSLElement, PsiNameIdentifierOwner, PsiCheckedRenameElement {

    @Nullable
    String getName();

    @Override
    default PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        replaceIdentifier(getNameIdentifier(), name);
        return this;
    }

    @Override
    default void checkSetName(String name) throws IncorrectOperationException {
        GLSLPsiElementFactory.createIdentifier(getProject(), name);
    }

    @Override
    int getTextOffset();

    /**
     * @see com.intellij.lang.findUsages.FindUsagesProvider#getType(PsiElement)
     */
    @NotNull
    String declaredNoun();

    static void replaceIdentifier(PsiElement oldIdentifier, @NotNull String name) throws IncorrectOperationException {
        if (oldIdentifier == null) throw new IncorrectOperationException("No name to rename");
        ASTNode newNameNode = GLSLPsiElementFactory.createIdentifier(oldIdentifier.getProject(), name);
        final ASTNode oldIdentifierNode = oldIdentifier.getNode();
        oldIdentifierNode.getTreeParent().replaceChild(oldIdentifierNode, newNameNode);
    }

    static void replacePreprocessorString(PsiElement oldPreprocessorString, @NotNull String name) throws IncorrectOperationException {
        if (oldPreprocessorString == null) throw new IncorrectOperationException("No name to rename");
        ASTNode newNameNode = GLSLPsiElementFactory.createPreprocessorString(oldPreprocessorString.getProject(), name);
        final ASTNode oldIdentifierNode = oldPreprocessorString.getNode();
        oldIdentifierNode.getTreeParent().replaceChild(oldIdentifierNode, newNameNode);
    }

    static void reformat(PsiElement statement) {
        if (statement.getContainingFile() instanceof GLSLFile) {
            CodeStyleManager.getInstance(statement.getManager()).reformat(statement);
        }
    }
}
