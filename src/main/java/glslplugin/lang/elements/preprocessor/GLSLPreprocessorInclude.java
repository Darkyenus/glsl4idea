package glslplugin.lang.elements.preprocessor;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.util.IncorrectOperationException;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.reference.GLSLReferencableDeclaration;
import glslplugin.lang.elements.reference.GLSLReferenceUtil;
import glslplugin.lang.parser.GLSLFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * When the directive is in the top-level and contains a string literal (surrounded by whatever),
 * and that string can be evaluated to a valid GLSL file, declarations from that file will be included.
 *
 * Preprocessor redefinitions can't be included in this way.
 * Syntactical correctness of the file inclusion is not checked.
 */
public class GLSLPreprocessorInclude extends GLSLPreprocessorDirective {
    public GLSLPreprocessorInclude(@NotNull ASTNode astNode) {
        super(astNode);
    }

    public @Nullable GLSLFile includedFile() {
        PsiDirectory dir = this.getContainingFile().getContainingDirectory();
        if (dir == null) {
            return null;
        }

        final String pathStringRaw = GLSLElement.text(this.<PsiElement>findChildByType(GLSLTokenTypes.PREPROCESSOR_STRING));
        final String pathString = pathStringRaw == null
                || !pathStringRaw.startsWith("\"")
                || !pathStringRaw.endsWith("\"")
                || pathStringRaw.length() <= 2
                ? null : pathStringRaw.substring(1, pathStringRaw.length() - 1);
        if (pathString == null) return null;

        // Split into / or \ separated parts.
        // String.split() is too slow for this.
        int partBegin = 0;
        final int pathLength = pathString.length();
        for (int partEnd = 0; partEnd < pathLength; partEnd++) {
            final char c = pathString.charAt(partEnd);
            if (c == '/' || c == '\\') {
                // Got a part

                final int partLength = partEnd - partBegin;

                // /./ or //
                boolean skipPart = partLength == 0 || (partLength == 1 && pathString.charAt(partBegin) == '.');

                if (!skipPart) {
                    if (partLength == 2 && pathString.charAt(partBegin) == '.' && pathString.charAt(partBegin + 1) == '.') {
                        // /../
                        dir = dir.getParentDirectory();
                    } else {
                        dir = dir.findSubdirectory(pathString.substring(partBegin, partEnd));
                    }
                    if (dir == null) {
                        return null;
                    }
                }

                partBegin = partEnd + 1;
            }
        }
        final String lastPart = pathString.substring(partBegin);

        final PsiFile includedFile = dir.findFile(lastPart);
        if (includedFile instanceof GLSLFile glslFile) {
            return glslFile;
        } else {
            return null;
        }
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
        final GLSLFile glslFile = includedFile();
        if (glslFile == null) return true;

        return glslFile.processDeclarations(processor, state, lastParent, place);
    }

    @Override
    public PsiReference getReference() {
        final PsiElement fileString = findChildByType(GLSLTokenTypes.PREPROCESSOR_STRING);
        if (fileString == null) return null;
        final TextRange textRange = GLSLReferenceUtil.rangeOfIn(fileString, this);
        return new PsiReferenceBase<>(this, textRange, true) {

            @Override
            public @Nullable PsiElement resolve() {
                return includedFile();
            }

            @Override
            public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
                newElementName = "\"" + newElementName.replace("\"", "").replace("\n", "").replace("\r", "") + "\"";

                final GLSLPreprocessorInclude element = getElement();
                final PsiElement string = element.findChildByType(GLSLTokenTypes.PREPROCESSOR_STRING);
                if (string == null) {
                    throw new IncorrectOperationException(element+" can't be renamed");
                }

                GLSLReferencableDeclaration.replacePreprocessorString(string, newElementName);
                setRangeInElement(GLSLReferenceUtil.rangeOfIn(element.findChildByType(GLSLTokenTypes.PREPROCESSOR_STRING), element));
                return element;
            }
        };
    }
}
