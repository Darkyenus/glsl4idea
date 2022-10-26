package glslplugin.lang.parser;

import com.intellij.lang.DefaultASTFactoryImpl;
import com.intellij.lang.ForeignLeafType;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.tree.IElementType;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.preprocessor.GLSLRedefinedToken;
import org.jetbrains.annotations.NotNull;

public class GLSLASTFactory extends DefaultASTFactoryImpl {

    @Override
    public @NotNull LeafElement createLeaf(@NotNull IElementType type, @NotNull CharSequence text) {
        if (type == GLSLTokenTypes.PREPROCESSOR_REDEFINED) {
            return new GLSLRedefinedToken(text);
        }
        return super.createLeaf(type, text);
    }

    @Override
    public @NotNull CompositeElement createComposite(@NotNull IElementType type) {
        CompositeElement result;
        if (type instanceof ForeignLeafType leaf) {
            // Generate AST for redefined tokens that is not easily distinguishable from normal tokens
            result = new LeafPsiCompositeElement(leaf);
        } else {
            result = super.createComposite(type);
        }
        return result;
    }

    public static final class LeafPsiCompositeElement extends CompositeElement {

        private final String text;

        public LeafPsiCompositeElement(@NotNull ForeignLeafType type) {
            super(type.getDelegate());
            text = type.getValue();
        }

        @Override
        public @NotNull String getText() {
            return text;
        }

        @Override
        public @NotNull CharSequence getChars() {
            return text;
        }

        @Override
        public char @NotNull [] textToCharArray() {
            return text.toCharArray();
        }

        @Override
        public boolean textContains(char c) {
            return text.indexOf(c) != -1;
        }

        @Override
        protected int textMatches(@NotNull CharSequence buffer, int start) {
            assert start >= 0 : start;
            final int length = text.length();
            if(buffer.length() - start < length) {
                return start == 0 ? Integer.MIN_VALUE : -start;
            }
            for(int i = 0; i < length; i++){
                int k = i + start;
                if(text.charAt(i) != buffer.charAt(k)) {
                    return k == 0 ? Integer.MIN_VALUE : -k;
                }
            }
            return start + length;
        }

        @Override
        public int getTextLength() {
            return 0;// Otherwise it breaks some assumptions.
        }

        @Override
        public TextRange getTextRange() {
            final int startOffset = getStartOffset();
            return new TextRange(startOffset, startOffset);
        }
    }
}
