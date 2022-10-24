package glslplugin.extensions;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.reference.GLSLReferencableDeclaration;
import glslplugin.lang.scanner.GLSLFlexAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 * Created by abigail on 26/06/15.
 */
public class GLSLFindUsagesProvider implements FindUsagesProvider {

    private static final TokenSet identifierTokenSet = TokenSet.orSet(TokenSet.create(GLSLTokenTypes.IDENTIFIER), GLSLTokenTypes.TYPE_SPECIFIER_NONARRAY_TOKENS);
    private static final TokenSet literalTokenSet = TokenSet.create(GLSLTokenTypes.PREPROCESSOR_STRING);

    @Nullable
    @Override
    public WordsScanner getWordsScanner() {
        /*
        This method must either return thread safe instance (which DefaultWordsScanner is not!)
        or a new instance. This is required, otherwise errors will happen.
         */
        return new DefaultWordsScanner(new GLSLFlexAdapter(),
                identifierTokenSet,
                GLSLTokenTypes.COMMENTS,
                literalTokenSet);
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof GLSLReferencableDeclaration;
    }

    @Nullable
    @Override
    public String getHelpId(@NotNull PsiElement psiElement) {
        return null;
    }

    @NotNull
    @Override
    public String getType(@NotNull PsiElement element) {
        if (element instanceof GLSLReferencableDeclaration ref) {
            return ref.declaredNoun();
        }
        return "GLSL Usage";
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement element) {
        return element.toString();//TODO
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        return element.toString();
    }
}
