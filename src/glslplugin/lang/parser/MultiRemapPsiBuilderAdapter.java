package glslplugin.lang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.TokenWrapper;
import com.intellij.lang.impl.PsiBuilderAdapter;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * A PsiBuilderAdapter in which each token can be remapped to an arbitrary number of tokens.
 *
 * In order to preserve the functionality of getTokenText(), it is necessary to remap tokens
 * to an instance of {#link com.intellij.lang.TokenWrapper}.
 * Created by abigail on 30/06/15.
 */
public class MultiRemapPsiBuilderAdapter extends PsiBuilderAdapter {
    protected ArrayList<IElementType> waitingTokens = new ArrayList<>();

    public MultiRemapPsiBuilderAdapter(PsiBuilder delegate) {
        super(delegate);
    }

    @Nullable
    @Override
    public IElementType getTokenType() {
        if (waitingTokens.isEmpty()) {
            return super.getTokenType();
        } else if (waitingTokens.get(0) instanceof TokenWrapper) {
            return ((TokenWrapper) waitingTokens.get(0)).getDelegate();
        }
        return waitingTokens.get(0);
    }

    @Nullable
    @Override
    public String getTokenText() {
        if (waitingTokens.isEmpty()) {
            return super.getTokenText();
        } else if (waitingTokens.get(0) instanceof TokenWrapper) {
            return ((TokenWrapper) waitingTokens.get(0)).getValue();
        }
        return "";
    }

    @Override
    public void advanceLexer() {
        if (waitingTokens.isEmpty()) {
            super.advanceLexer();
        } else {
            // The underlying PsiBuilder doesn't know about the replaced elements and so doesn't know to create
            // LeafPsiElement instances for all the tokens.
            // TODO this should use {#link com.intellij.lang.ForeignLeafType}, which is what the waiting token
            // will probably be, in order to create leaves with valid text.
            Marker foreignLeaf = super.mark();
            foreignLeaf.done(getTokenType());
            waitingTokens.remove(0);
        }
    }

    @Override
    public void remapCurrentToken(IElementType type) {
        remapCurrentToken(Collections.singletonList(type));
    }

    public void remapCurrentToken(Collection<IElementType> types) {
        advanceLexer();
        waitingTokens.addAll(0, types);
    }

    @Override
    public Marker mark() {
        return new DelegateMarker(super.mark());
    }

    protected class DelegateMarker extends com.intellij.lang.impl.DelegateMarker {
        protected ArrayList<IElementType> rollbackWaitingTokens = new ArrayList<>();

        public DelegateMarker(Marker delegate) {
            super(delegate);
            rollbackWaitingTokens = (ArrayList<IElementType>) waitingTokens.clone();
        }

        @Override
        public Marker precede() {
            Marker precedent = super.precede();
            if (precedent instanceof DelegateMarker) {
                ((DelegateMarker) precedent).rollbackWaitingTokens = rollbackWaitingTokens;
            }
            return precedent;
        }

        @Override
        public void rollbackTo() {
            super.rollbackTo();
            waitingTokens = rollbackWaitingTokens;
        }
    }
}
