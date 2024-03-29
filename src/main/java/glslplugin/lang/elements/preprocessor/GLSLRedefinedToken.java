package glslplugin.lang.elements.preprocessor;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.reference.GLSLAbstractReference;
import glslplugin.lang.elements.reference.GLSLReferencingElement;
import glslplugin.lang.parser.GLSLASTFactory;
import glslplugin.util.TreeIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * A token that has been redefined by preprocessor.
 */
public class GLSLRedefinedToken extends LeafPsiElement implements GLSLReferencingElement {

    public GLSLRedefinedToken(@NotNull CharSequence text) {
        super(GLSLTokenTypes.PREPROCESSOR_REDEFINED, text);
    }

    @Override
    public @Nullable PsiElement getReferencingIdentifierForRenaming() {
        return this;
    }

    @Nullable
    public String getRedefinedTokenName() {
        return GLSLElement.text(getReferencingIdentifierForRenaming());
    }

    @NotNull
    public String redefinedTo() {
        final StringBuilder sb = new StringBuilder();

        ASTNode node;
        {
            PsiElement next = getNextSibling();
            if (next instanceof GLSLPreprocessorFunctionMacroArguments) {
                final PsiElement afterNext = next.getNextSibling();
                node = afterNext == null ? null : TreeIterator.nextLeaf(afterNext.getNode());
            } else {
                node = TreeIterator.nextLeaf(getNode());
            }
        }
        while (node instanceof GLSLASTFactory.LeafPsiCompositeElement leaf) {
            sb.append(leaf.actualText);
            sb.append(' ');
            node = TreeIterator.nextLeaf(node);
        }
        int leadingWhitespace = 0;
        while (leadingWhitespace < sb.length() && Character.isWhitespace(sb.charAt(leadingWhitespace))) {
            leadingWhitespace++;
        }
        sb.replace(0, leadingWhitespace, "");

        int trailingWhitespace = sb.length();
        while (trailingWhitespace > 0 && Character.isWhitespace(sb.charAt(trailingWhitespace - 1))) {
            trailingWhitespace--;
        }
        sb.setLength(trailingWhitespace);

        return sb.toString();
    }

    public static final class RedefinedTokenReference extends GLSLAbstractReference<GLSLRedefinedToken> {

        public RedefinedTokenReference(@NotNull GLSLRedefinedToken element) {
            super(element);
        }

        @Override
        public @Nullable GLSLDefineDirective resolve() {
            final String tokenName = element.getRedefinedTokenName();
            if (tokenName == null) return null;

            GLSLDefineDirective prev = TreeIterator.previous(element, GLSLDefineDirective.class);
            while (prev != null) {
                if (tokenName.equals(prev.getName())) {
                    return prev;
                }
                prev = TreeIterator.previous(prev, GLSLDefineDirective.class);
            }

            return null;
        }
    }

    private final RedefinedTokenReference reference = new RedefinedTokenReference(this);

    /**
     * @return reference to the #define which caused the redefinition of this token
     */
    @Override
    public RedefinedTokenReference getReference() {
        return reference;
    }
}
