package glslplugin.lang.elements.preprocessor;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.parser.GLSLFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * When the directive is in the top-level and contains a string literal (surrounded by whatever),
 * and that string can be evaluated to a valid GLSL file, declarations from that file will be included.
 * <p>
 * Preprocessor redefinitions can't be included in this way.
 * Syntactical correctness of the file inclusion is not checked.
 */
public class GLSLPreprocessorInclude extends GLSLPreprocessorDirective {
    public GLSLPreprocessorInclude(@NotNull ASTNode astNode) {
        super(astNode);
    }

    public @Nullable GLSLFile includedFile() {
        return CachedValuesManager.getCachedValue(this, () -> CachedValueProvider.Result.create(
            resolveIncludedFile(),
            this,
            PsiModificationTracker.MODIFICATION_COUNT,
            ProjectRootManager.getInstance(getProject())
        ));
    }

    private @Nullable GLSLFile resolveIncludedFile() {
        final String pathStringRaw = GLSLElement.text(this.<PsiElement>findChildByType(GLSLTokenTypes.PREPROCESSOR_STRING));
        final String pathString = pathStringRaw == null
            || !pathStringRaw.startsWith("\"")
            || !pathStringRaw.endsWith("\"")
            || pathStringRaw.length() <= 2
            ? null : pathStringRaw.substring(1, pathStringRaw.length() - 1);
        if (pathString == null) {
            return null;
        }

        PsiFile thisFile = this.getContainingFile();
        while (thisFile.getOriginalFile() != thisFile) {
            thisFile = thisFile.getOriginalFile();
        }
        PsiDirectory dir = thisFile.getContainingDirectory();
        if (pathString.startsWith("/")) {
            VirtualFile thisVirtualFile = thisFile.getVirtualFile();
            if (thisVirtualFile == null) {
                return null;
            }
            ProjectFileIndex fileIndex = ProjectRootManager.getInstance(getProject()).getFileIndex();
            VirtualFile sourceRoot = fileIndex.getSourceRootForFile(thisVirtualFile);

            if (sourceRoot == null) {
                sourceRoot = fileIndex.getContentRootForFile(thisVirtualFile);
            }

            if (sourceRoot != null) {
                dir = getManager().findDirectory(sourceRoot);
            }
        }

        if (dir == null) {
            return null;
        }

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
    public boolean processDeclarations(
        @NotNull PsiScopeProcessor processor,
        @NotNull ResolveState state,
        PsiElement lastParent,
        @NotNull PsiElement place
    ) {
        final GLSLFile glslFile = includedFile();
        if (glslFile == null) return true;

        return glslFile.processDeclarations(processor, state, lastParent, place);
    }
}
