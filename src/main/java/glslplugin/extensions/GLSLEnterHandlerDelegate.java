package glslplugin.extensions;

import com.intellij.codeInsight.editorActions.enter.EnterBetweenBracesDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @author Wyozi
 */
public class GLSLEnterHandlerDelegate extends EnterBetweenBracesDelegate {

    @Override
    public boolean isInComment(@NotNull PsiFile file, @NotNull Editor editor, int offset) {
        //TODO Check if this is correct for GLSL
        return PsiTreeUtil.getParentOfType(file.findElementAt(offset), PsiComment.class) != null;
    }

    @Override
    protected boolean isBracePair(char c1, char c2) {
        return c1 == '{' && c2 == '}';
    }

}
