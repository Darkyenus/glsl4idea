package glslplugin.lang.elements.preprocessor;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
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
        PsiElement versionLiteral = getVersionLiteral();
        if (versionLiteral == null) return -1;

        return Integer.parseInt(versionLiteral.getText());
    }

    @Override
    public String toString() {
        final int versionLiteralNumber = getVersionLiteralNumber();
        return "#Version Directive " + (versionLiteralNumber == -1 ? "?" : versionLiteralNumber);
    }
}
