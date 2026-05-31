package glslplugin.lang.elements.preprocessor;

import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.IncorrectOperationException;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.reference.GLSLReferencableDeclaration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class GLSLPreprocessorString extends LeafPsiElement {
    public GLSLPreprocessorString(@NotNull CharSequence text) {
        super(GLSLTokenTypes.PREPROCESSOR_STRING, text);
    }

    @Override
    public @Nullable PsiReference getReference() {
        if (getParent() instanceof GLSLPreprocessorInclude include) {
            return new IncludeReference(this, include);
        }
        return null;
    }

    private static final class IncludeReference extends PsiReferenceBase<GLSLPreprocessorString> {
        private final @NotNull GLSLPreprocessorInclude include;

        private IncludeReference(@NotNull GLSLPreprocessorString element, @NotNull GLSLPreprocessorInclude include) {
            super(element, referenceRange(element), true);
            this.include = include;
        }

        @Override
        public @Nullable PsiElement resolve() {
            return include.includedFile();
        }

        @Override
        public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
            String currentPath = unquotePath(getElement());
            int lastSeparator = Math.max(currentPath.lastIndexOf('/'), currentPath.lastIndexOf('\\'));
            String sanitizedName = sanitizePathSegment(newElementName);
            String newPath = lastSeparator >= 0
                ? currentPath.substring(0, lastSeparator + 1) + sanitizedName
                : sanitizedName;
            return replacePath(newPath);
        }

        @Override
        public PsiElement bindToElement(@NotNull PsiElement targetElement) throws IncorrectOperationException {
            if (!(targetElement instanceof PsiFile targetFile)) {
                throw new IncorrectOperationException("Can only bind to a file");
            }

            PsiFile currentFile = include.getContainingFile();
            while (currentFile.getOriginalFile() != currentFile) {
                currentFile = currentFile.getOriginalFile();
            }

            VirtualFile currentVirtualFile = currentFile.getVirtualFile();
            VirtualFile targetVirtualFile = targetFile.getVirtualFile();
            if (currentVirtualFile == null || targetVirtualFile == null) {
                throw new IncorrectOperationException("Virtual files not available");
            }

            String currentPath = unquotePath(getElement());
            String newPath;
            if (currentPath.startsWith("/")) {
                ProjectFileIndex fileIndex = ProjectRootManager.getInstance(include.getProject()).getFileIndex();
                VirtualFile sourceRoot = fileIndex.getSourceRootForFile(currentVirtualFile);
                if (sourceRoot == null) {
                    sourceRoot = fileIndex.getContentRootForFile(currentVirtualFile);
                }
                if (sourceRoot == null) {
                    throw new IncorrectOperationException("Cannot find source root");
                }

                String relativePath = VfsUtilCore.getRelativePath(targetVirtualFile, sourceRoot, '/');
                if (relativePath == null) {
                    throw new IncorrectOperationException("Cannot compute path from source root");
                }
                newPath = "/" + relativePath;
            } else {
                VirtualFile currentDir = currentVirtualFile.getParent();
                if (currentDir == null) {
                    throw new IncorrectOperationException("Cannot find current directory");
                }
                newPath = computeRelativePath(currentDir, targetVirtualFile);
            }

            return replacePath(newPath);
        }

        private @NotNull PsiElement replacePath(@NotNull String newPath) {
            GLSLReferencableDeclaration.replacePreprocessorString(getElement(), quotePath(newPath));
            PsiElement updatedString = include.findChildByType(GLSLTokenTypes.PREPROCESSOR_STRING);
            return updatedString != null ? updatedString : include;
        }

        private static @NotNull TextRange referenceRange(@NotNull GLSLPreprocessorString element) {
            String text = element.getText();
            if (text.length() >= 2 && text.startsWith("\"") && text.endsWith("\"")) {
                return TextRange.create(1, text.length() - 1);
            }
            return TextRange.create(0, text.length());
        }

        private static @NotNull String unquotePath(@NotNull PsiElement stringElement) throws IncorrectOperationException {
            String text = stringElement.getText();
            if (text.length() < 2 || !text.startsWith("\"") || !text.endsWith("\"")) {
                throw new IncorrectOperationException(stringElement + " can't be rebound");
            }
            return text.substring(1, text.length() - 1);
        }

        private static @NotNull String quotePath(@NotNull String path) {
            return '"' + path + '"';
        }

        private static @NotNull String sanitizePathSegment(@NotNull String elementName) {
            return elementName.replace("\"", "").replace("\n", "").replace("\r", "");
        }

        private static @NotNull String computeRelativePath(@NotNull VirtualFile baseDir, @NotNull VirtualFile targetFile) {
            String simplePath = VfsUtilCore.getRelativePath(targetFile, baseDir, '/');
            if (simplePath != null) {
                return simplePath;
            }

            List<VirtualFile> basePath = new ArrayList<>();
            VirtualFile current = baseDir;
            while (current != null) {
                basePath.add(current);
                current = current.getParent();
            }

            List<VirtualFile> targetPath = new ArrayList<>();
            current = targetFile.getParent();
            while (current != null) {
                targetPath.add(current);
                current = current.getParent();
            }

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

            StringBuilder result = new StringBuilder();
            current = baseDir;
            while (current != null && !current.equals(commonAncestor)) {
                if (result.length() > 0) {
                    result.append('/');
                }
                result.append("..");
                current = current.getParent();
            }

            String pathFromAncestor = VfsUtilCore.getRelativePath(targetFile, commonAncestor, '/');
            if (pathFromAncestor != null && !pathFromAncestor.isEmpty()) {
                if (result.length() > 0) {
                    result.append('/');
                }
                result.append(pathFromAncestor);
            }

            return result.toString();
        }
    }
}

