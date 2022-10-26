package glslplugin.lang.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.DefaultASTFactoryImpl;
import com.intellij.lang.ForeignLeafType;
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

    /**
     * Stand in for preprocessor redefined tokens.
     * Since {@link #getText()} and related text methods must match the file reality, they are not overridden.
     * Use {@link glslplugin.lang.elements.GLSLElement#nodeText(ASTNode)} instead for compatbilitiy.
     * But this subclass at least provides correct {@link IElementType}.
     */
    public static final class LeafPsiCompositeElement extends CompositeElement {

        public final String actualText;

        public LeafPsiCompositeElement(@NotNull ForeignLeafType type) {
            super(type.getDelegate());
            actualText = type.getValue();
        }
    }
}
