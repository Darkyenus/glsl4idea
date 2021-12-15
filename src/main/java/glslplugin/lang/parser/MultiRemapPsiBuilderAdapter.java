package glslplugin.lang.parser;

import com.intellij.lang.ForeignLeafType;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.TokenWrapper;
import com.intellij.lang.impl.PsiBuilderAdapter;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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

    private static final String[] NO_NAMES = {};

    @NotNull
    public String[] getNamesThroughWhichThisTokenWasRedefined() {
        if (waitingTokens.isEmpty()) return NO_NAMES;
        final IElementType type = waitingTokens.get(0);
        if (type instanceof RedefinedTokenType) {
            return ((RedefinedTokenType) type).redefinedThrough;
        } else {
            return NO_NAMES;
        }
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
            final IElementType tokenType = getTokenType();
            if (tokenType != null) {
                foreignLeaf.done(tokenType);
            } else {
                foreignLeaf.drop();
            }
            waitingTokens.remove(0);
        }
    }

    /** @see #remapCurrentToken(List, String), does that but without redefining */
    @Override
    public void remapCurrentToken(IElementType type) {
        remapCurrentTokenAdvanceLexer();
        waitingTokens.add(0, type);
    }

    protected void remapCurrentTokenAdvanceLexer(){
        advanceLexer();
    }

    public void remapCurrentToken(List<ForeignLeafType> remapToTypes, String remappedThrough) {
        remapCurrentTokenAdvanceLexer();
        final ArrayList<IElementType> tagged = new ArrayList<>(remapToTypes.size());
        for (final ForeignLeafType type : remapToTypes) {
            final IElementType taggedType;
            if (type instanceof RedefinedTokenType) {
                taggedType = ((RedefinedTokenType) type).redefineAlsoThrough(remappedThrough);
            } else {
                taggedType = new RedefinedTokenType(type, remappedThrough);
            }
            tagged.add(taggedType);
        }
        waitingTokens.addAll(0, tagged);
    }

    @NotNull
    @Override
    public Marker mark() {
        return new DelegateMarker(super.mark());
    }

    @Override
    public IElementType lookAhead(int steps) {
        final Marker lookaheadMark = mark();
        try{
            for (int i = 0; i < steps; i++) {
                advanceLexer();
            }
            return getTokenType();
        }finally {
            lookaheadMark.rollbackTo();
        }
    }

    protected class DelegateMarker extends com.intellij.lang.impl.DelegateMarker {
        protected ArrayList<IElementType> rollbackWaitingTokens;

        public DelegateMarker(Marker delegate) {
            super(delegate);
            rollbackWaitingTokens = new ArrayList<>(waitingTokens);
        }

        @NotNull
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
