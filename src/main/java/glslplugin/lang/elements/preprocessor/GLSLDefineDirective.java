package glslplugin.lang.elements.preprocessor;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.reference.GLSLReferencableDeclaration;
import glslplugin.lang.parser.GLSLFile;
import glslplugin.util.TreeIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.Objects;

/**
 *
 * Created by abigail on 08/07/15.
 */
public class GLSLDefineDirective extends GLSLPreprocessorDirective implements GLSLReferencableDeclaration {
    public GLSLDefineDirective(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        return findChildByType(GLSLTokenTypes.IDENTIFIER);
    }

    @NotNull
    @Override
    public String getName() {
        return Objects.requireNonNullElse(GLSLElement.text(getNameIdentifier()), "");
    }

    @Override
    public int getTextOffset() {
        final PsiElement identifier = getNameIdentifier();
        return identifier != null ? identifier.getTextOffset() : super.getTextOffset();
    }

    public static @Nullable GLSLDefineDirective findActiveDefinitionBefore(@NotNull PsiElement origin, @NotNull String tokenName) {
        return findActiveDefinitionBefore(origin, tokenName, Collections.newSetFromMap(new IdentityHashMap<>())).definition;
    }

    private static @NotNull MacroLookupResult findActiveDefinitionBefore(
        @NotNull PsiElement origin,
        @NotNull String tokenName,
        @NotNull Set<GLSLFile> visitedIncludedFiles
    ) {
        GLSLPreprocessorDirective prev = TreeIterator.previous(origin, GLSLPreprocessorDirective.class);
        while (prev != null) {
            if (prev instanceof GLSLDefineDirective defineDirective && tokenName.equals(defineDirective.getName())) {
                return MacroLookupResult.defined(defineDirective);
            }
            if (prev.isUndefDirectiveFor(tokenName)) {
                return MacroLookupResult.undefined();
            }
            if (prev instanceof GLSLPreprocessorInclude include) {
                MacroLookupResult includedResult = findActiveDefinitionAtEnd(
                    include.includedFile(),
                    tokenName,
                    visitedIncludedFiles
                );
                if (includedResult.isTerminal()) {
                    return includedResult;
                }
            }
            prev = TreeIterator.previous(prev, GLSLPreprocessorDirective.class);
        }

        return MacroLookupResult.notFound();
    }

    private static @NotNull MacroLookupResult findActiveDefinitionAtEnd(
        @Nullable GLSLFile file,
        @NotNull String tokenName,
        @NotNull Set<GLSLFile> visitedIncludedFiles
    ) {
        if (file == null || !visitedIncludedFiles.add(file)) {
            return MacroLookupResult.notFound();
        }

        return findActiveDefinitionBefore(file, tokenName, visitedIncludedFiles);
    }

    private static final class MacroLookupResult {
        private static final MacroLookupResult NOT_FOUND = new MacroLookupResult(null, false);
        private static final MacroLookupResult UNDEFINED = new MacroLookupResult(null, true);

        private final @Nullable GLSLDefineDirective definition;
        private final boolean undefined;

        private MacroLookupResult(@Nullable GLSLDefineDirective definition, boolean undefined) {
            this.definition = definition;
            this.undefined = undefined;
        }

        private static @NotNull MacroLookupResult defined(@NotNull GLSLDefineDirective definition) {
            return new MacroLookupResult(definition, false);
        }

        private static @NotNull MacroLookupResult undefined() {
            return UNDEFINED;
        }

        private static @NotNull MacroLookupResult notFound() {
            return NOT_FOUND;
        }

        private boolean isTerminal() {
            return definition != null || undefined;
        }
    }

    @Override
    public boolean processDeclarations(
        @NotNull PsiScopeProcessor processor,
        @NotNull ResolveState state,
        PsiElement lastParent,
        @NotNull PsiElement place
    ) {
        return processor.execute(this, state);
    }

    /**
     * @return text after the name
     */
    @NotNull
    public String getBoundText(){
        final PsiElement name = getNameIdentifier();
        if (name == null) return "";
        final ASTNode first = name.getNode().getTreeNext();
        if (first == null || first.getElementType() == GLSLTokenTypes.PREPROCESSOR_END) return "";
        ASTNode last = first;
        while (true) {
            final ASTNode next = last.getTreeNext();
            if (next == null || next.getElementType() == GLSLTokenTypes.PREPROCESSOR_END) {
                break;
            }
            last = next;
        }

        int startOffset = first.getStartOffset();
        int endOffset = last.getStartOffset() + last.getTextLength();
        final String text = getContainingFile().getText();
        if (startOffset < 0) startOffset = 0;
        if (endOffset > text.length()) endOffset = text.length();
        if (startOffset >= endOffset) return "";

        return text.substring(startOffset, endOffset).trim();
    }

    @Override
    public @NotNull String declaredNoun() {
        return "redefined token";
    }

    @Override
    public String toString() {
        return "#Define Directive "+getName()+" => "+getBoundText();
    }
}
