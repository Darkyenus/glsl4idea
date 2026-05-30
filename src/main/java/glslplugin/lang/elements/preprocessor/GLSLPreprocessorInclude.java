package glslplugin.lang.elements.preprocessor;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
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
                final GLSLPreprocessorInclude element = getElement();
                final PsiElement string = element.findChildByType(GLSLTokenTypes.PREPROCESSOR_STRING);
                if (string == null) {
                    throw new IncorrectOperationException(element + " can't be renamed");
                }

                // Get the current path from the include directive
                String currentPathRaw = string.getText();
                String currentPath = currentPathRaw.substring(1, currentPathRaw.length() - 1); // Remove quotes

                // Find the last separator (/ or \) to isolate the directory part
                int lastSeparator = Math.max(currentPath.lastIndexOf('/'), currentPath.lastIndexOf('\\'));

                String newPath;
                if (lastSeparator >= 0) {
                    // Preserve the directory path, only replace the filename
                    String directoryPath = currentPath.substring(0, lastSeparator + 1);
                    newPath = directoryPath + newElementName.replace("\"", "").replace("\n", "").replace("\r", "");
                } else {
                    // No directory in the original path, just use the new filename
                    newPath = newElementName.replace("\"", "").replace("\n", "").replace("\r", "");
                }

                String newPathWithQuotes = "\"" + newPath + "\"";
                GLSLReferencableDeclaration.replacePreprocessorString(string, newPathWithQuotes);
                setRangeInElement(GLSLReferenceUtil.rangeOfIn(
                    element.findChildByType(GLSLTokenTypes.PREPROCESSOR_STRING),
                    element
                ));
                return element;
            }

            @Override
            public PsiElement bindToElement(@NotNull PsiElement targetElement) throws IncorrectOperationException {
                if (!(targetElement instanceof PsiFile)) {
                    throw new IncorrectOperationException("Can only bind to a file");
                }

                final GLSLPreprocessorInclude element = getElement();
                final PsiElement string = element.findChildByType(GLSLTokenTypes.PREPROCESSOR_STRING);
                if (string == null) {
                    throw new IncorrectOperationException(element + " can't be rebound");
                }

                PsiFile targetFile = (PsiFile) targetElement;
                PsiFile currentFile = element.getContainingFile();

                // Get the original file to handle preview/temporary files
                while (currentFile.getOriginalFile() != currentFile) {
                    currentFile = currentFile.getOriginalFile();
                }

                VirtualFile currentVirtualFile = currentFile.getVirtualFile();
                VirtualFile targetVirtualFile = targetFile.getVirtualFile();

                if (currentVirtualFile == null || targetVirtualFile == null) {
                    throw new IncorrectOperationException("Virtual files not available");
                }

                // Get the current include path to determine if it's absolute or relative
                String currentPathRaw = string.getText();
                String currentPath = currentPathRaw.substring(1, currentPathRaw.length() - 1); // Remove quotes
                boolean isAbsolutePath = currentPath.startsWith("/");

                String newPath;
                if (isAbsolutePath) {
                    // For absolute paths, compute path from source/content root
                    ProjectFileIndex fileIndex = ProjectRootManager.getInstance(element.getProject()).getFileIndex();
                    VirtualFile sourceRoot = fileIndex.getSourceRootForFile(currentVirtualFile);
                    if (sourceRoot == null) {
                        sourceRoot = fileIndex.getContentRootForFile(currentVirtualFile);
                    }

                    if (sourceRoot != null) {
                        String relativePath = com.intellij.openapi.vfs.VfsUtilCore.getRelativePath(targetVirtualFile, sourceRoot, '/');
                        if (relativePath != null) {
                            newPath = "/" + relativePath;
                        } else {
                            throw new IncorrectOperationException("Cannot compute path from source root");
                        }
                    } else {
                        throw new IncorrectOperationException("Cannot find source root");
                    }
                } else {
                    // For relative paths, compute path relative to current file's directory
                    VirtualFile currentDir = currentVirtualFile.getParent();
                    if (currentDir != null) {
                        newPath = computeRelativePath(currentDir, targetVirtualFile);
                    } else {
                        throw new IncorrectOperationException("Cannot find current directory");
                    }
                }

                String newPathWithQuotes = "\"" + newPath + "\"";
                GLSLReferencableDeclaration.replacePreprocessorString(string, newPathWithQuotes);
                setRangeInElement(GLSLReferenceUtil.rangeOfIn(
                    element.findChildByType(GLSLTokenTypes.PREPROCESSOR_STRING),
                    element
                ));
                return element;
            }

            /**
             * Computes a relative path from baseDir to targetFile, supporting parent directory navigation with "..".
             */
            private String computeRelativePath(VirtualFile baseDir, VirtualFile targetFile) {
                // First try the simple case - target is within baseDir
                String simplePath = com.intellij.openapi.vfs.VfsUtilCore.getRelativePath(targetFile, baseDir, '/');
                if (simplePath != null) {
                    return simplePath;
                }

                // Target is not within baseDir, need to use ".." to go up
                // Find common ancestor
                java.util.List<VirtualFile> basePath = new java.util.ArrayList<>();
                VirtualFile current = baseDir;
                while (current != null) {
                    basePath.add(current);
                    current = current.getParent();
                }

                java.util.List<VirtualFile> targetPath = new java.util.ArrayList<>();
                current = targetFile.getParent();
                while (current != null) {
                    targetPath.add(current);
                    current = current.getParent();
                }

                // Find common ancestor
                VirtualFile commonAncestor = null;
                for (VirtualFile base : basePath) {
                    if (targetPath.contains(base)) {
                        commonAncestor = base;
                        break;
                    }
                }

                if (commonAncestor == null) {
                    throw new IncorrectOperationException("Cannot find common ancestor directory");
                }

                // Count how many ".." we need from baseDir to commonAncestor
                StringBuilder result = new StringBuilder();
                current = baseDir;
                while (current != null && !current.equals(commonAncestor)) {
                    if (result.length() > 0) {
                        result.append('/');
                    }
                    result.append("..");
                    current = current.getParent();
                }

                // Add path from commonAncestor to targetFile
                String pathFromAncestor = com.intellij.openapi.vfs.VfsUtilCore.getRelativePath(targetFile, commonAncestor, '/');
                if (pathFromAncestor != null && !pathFromAncestor.isEmpty()) {
                    if (result.length() > 0) {
                        result.append('/');
                    }
                    result.append(pathFromAncestor);
                }

                return result.toString();
            }
        };
    }
}
