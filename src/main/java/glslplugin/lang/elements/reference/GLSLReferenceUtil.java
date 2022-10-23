package glslplugin.lang.elements.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GLSLReferenceUtil {

    @NotNull
    public static TextRange rangeOfIn(@Nullable PsiElement rangeOfThis, @NotNull PsiElement inThisParentElement) {
        if (rangeOfThis == null) {
            return new TextRange(0, inThisParentElement.getTextLength());
        }

        final int length = rangeOfThis.getTextLength();
        int offset = 0;

        PsiElement element = rangeOfThis;
        while (element != inThisParentElement && element != null) {
            offset += element.getStartOffsetInParent();
            element = element.getParent();
        }

        return new TextRange(offset, offset + length);
    }

    private static void appendElement(StringBuilder sb, PsiElement element, TextRange rangeInElement) {
        if (element == null) {
            sb.append("null");
            return;
        }

        sb.append(element.getClass().getSimpleName()).append("{");
        final String elementText = element.getText();

        PsiElement child = element;
        PsiElement parent = child.getParent();
        String parentText = null;
        TextRange textRangeInParent = null;
        while (parent != null) {
            parentText = parent.getText();
            textRangeInParent = child.getTextRangeInParent();
            if (!parentText.equals(elementText))
                break;
            child = parent;
            parent = child.getParent();
        }

        if (parentText != null) {
            if (rangeInElement != null) {
                textRangeInParent = new TextRange(textRangeInParent.getStartOffset() + rangeInElement.getStartOffset(), textRangeInParent.getStartOffset() + rangeInElement.getEndOffset());
            }
            sb.append(parentText, 0, textRangeInParent.getStartOffset());
            sb.append('|');
            sb.append(parentText, textRangeInParent.getStartOffset(), textRangeInParent.getEndOffset());
            sb.append('|');
            sb.append(parentText, textRangeInParent.getEndOffset(), parentText.length());
        } else {
            //TODO handle rangeInElement (unlikely to be ever needed)
            sb.append(elementText);
        }
        sb.append("}");
    }

    public static String toString(PsiReference reference) {
        final StringBuilder sb = new StringBuilder();
        sb.append(reference.getClass().getSimpleName()).append(" ");
        appendElement(sb, reference.getElement(), reference.getRangeInElement());

        final PsiElement resolve = reference.resolve();
        sb.append(" -> ");
        appendElement(sb, resolve, null);

        if (false) {
            sb.append(" [");
            boolean first = true;
            for (Object variant : reference.getVariants()) {
                if (first) {
                    first = false;
                } else {
                    sb.append(", ");
                }

                if (variant instanceof PsiElement) {
                    appendElement(sb, (PsiElement) variant, null);
                } else {
                    sb.append(variant);
                }
            }
            sb.append("]");
        }
        return sb.toString();
    }

}
