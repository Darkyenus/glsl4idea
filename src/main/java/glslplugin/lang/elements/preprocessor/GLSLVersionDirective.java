package glslplugin.lang.elements.preprocessor;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLTokenTypes;
import org.jetbrains.annotations.NotNull;

/**
 * @author Wyozi
 */
public class GLSLVersionDirective extends GLSLPreprocessorDirective {
    public GLSLVersionDirective(@NotNull ASTNode astNode) {
        super(astNode);
    }

    public PsiElement getVersionLiteral() {
        PsiElement child = getFirstChild();
        while (child != null) { // we can't iterate over getChildren(), as that ignores leaf elements
            if (child.getNode().getElementType() == GLSLTokenTypes.INTEGER_CONSTANT) return child;
            child = child.getNextSibling();
        }
        return null;
    }

    /**
     * @return the version literal as number or -1 if not found
     */
    public int getVersionLiteralNumber() {
        String versionLiteral = GLSLElement.text(getVersionLiteral());
        if (versionLiteral == null) {
            PsiElement child = getFirstChild();
            while (child != null) {
                if (child.getNode().getElementType() == GLSLTokenTypes.PREPROCESSOR_RAW) {
                    versionLiteral = firstIntegerIn(child.getText());
                    break;
                }
                child = child.getNextSibling();
            }
        }

        if (versionLiteral == null) return -1;

        return Integer.parseInt(versionLiteral);
    }

    private static String firstIntegerIn(String text) {
        final int length = text.length();
        int start = -1;
        for (int i = 0; i < length; i++) {
            if (Character.isDigit(text.charAt(i))) {
                start = i;
                break;
            }
        }
        if (start == -1) return null;

        int end = start + 1;
        while (end < length && Character.isDigit(text.charAt(end))) {
            end++;
        }
        return text.substring(start, end);
    }

    @Override
    public String toString() {
        final int versionLiteralNumber = getVersionLiteralNumber();
        return "#Version Directive " + (versionLiteralNumber == -1 ? "?" : versionLiteralNumber);
    }
}
