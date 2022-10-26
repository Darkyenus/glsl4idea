package glslplugin.lang.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ForeignLeafType;
import com.intellij.lang.PsiBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.tree.IElementType;
import glslplugin.lang.elements.GLSLTokenTypes;
import kotlin.collections.ArrayDeque;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Predicate;

/**
 * Does not actually implement {@link PsiBuilder}, because some methods are not implemented and this guarantees that they are not used by accident.
 */
public class PreprocessorPsiBuilderAdapter {

    private static final Logger LOG = Logger.getInstance(PreprocessorPsiBuilderAdapter.class);

    private final @NotNull PsiBuilder parent;

    /** To help with memory consumption, all tokens that will definitely not be needed are dropped from tokens.
     * The amount of dropped tokens is recorded here to aid with proper indexing, because all position numbers are indexed from the start. */
    private int droppedTokens = 0;
    /** The current token, in absolute position, is recorded here.
     * {@link #advanceLexer()} moves it forward, marker rollbacks move it backward. */
    private int currentToken = 0;
    /** Holds peeked tokens, redefined tokens and markers */
    private final ArrayDeque<@Nullable ForeignLeafType> tokens = new ArrayDeque<>();
    private final TreeSet<PMark> pendingMarkers = new TreeSet<>();

    private final HashMap<String, Redefinition> redefinitions = new HashMap<>();

    private boolean debugMode = false;

    public boolean allowRedefinitions = true;

    public PreprocessorPsiBuilderAdapter(@NotNull PsiBuilder parent) {
        this.parent = parent;
    }

    public Project getProject() {
        return parent.getProject();
    }

    public @NotNull ASTNode getTreeBuilt() {
        if (debugMode) {
            for (PMark marker : pendingMarkers) {
                LOG.error("Incomplete marker: "+marker.alive);
            }
        }
        return parent.getTreeBuilt();
    }

    public @NotNull CharSequence getOriginalText() {
        return parent.getOriginalText();
    }

    private void fillBufferIfEmpty() {
        final int tokenIndex = currentToken - droppedTokens;
        if (tokenIndex < tokens.size() || parent.eof()) {
            // Not empty or eof
            return;
        }
        assert tokenIndex == tokens.size();

        while (true) {
            // We must put something in
            final String text = parent.getTokenText();
            final Redefinition redefinition = allowRedefinitions ? redefinitions.get(text) : null;
            if (redefinition != null && redefinition.redefinedTo != null) {
                // The next token is redefined, skip it (mark as redefined) and insert tokens for redefinitions
                parent.remapCurrentToken(GLSLTokenTypes.PREPROCESSOR_REDEFINED);
                parent.advanceLexer();
                //TODO Add this to tokens?

                if (!redefinition.redefinedTo.isEmpty()) {
                    tokens.addAll(redefinition.redefinedTo);
                    break;
                }
                // Continue, check next token
            } else {
                tokens.add(null);// = check parent
                break;
            }
        }
    }

    public void advanceLexer() {
        fillBufferIfEmpty();
        if (eof()) {
            return;
        }
        final int tokenIndex = currentToken - droppedTokens;
        final ForeignLeafType type = tokens.get(tokenIndex);
        if (type == null) {
            parent.advanceLexer();
        } else {
            parent.mark().done(type);
        }
        currentToken++;

    }

    public @Nullable IElementType getTokenType() {
        fillBufferIfEmpty();
        if (eof()) {
            return null;
        }
        final int tokenIndex = currentToken - droppedTokens;
        final ForeignLeafType type = tokens.get(tokenIndex);
        if (type == null) {
            return parent.getTokenType();
        } else {
            return type.getDelegate();
        }
    }

    public @NonNls @Nullable String getTokenText() {
        fillBufferIfEmpty();
        if (eof()) {
            return null;
        }
        final int tokenIndex = currentToken - droppedTokens;
        final ForeignLeafType type = tokens.get(tokenIndex);
        if (type == null) {
            return parent.getTokenText();
        } else {
            return type.getValue();
        }
    }

    //TODO Delete if unused
    public int getCurrentOffset() {
        return parent.getCurrentOffset();
    }

    public @NotNull PreprocessorPsiBuilderAdapter.PMark mark() {
        return addMarker(parent.mark(), currentToken, null);
    }

    public void error(@NotNull String messageText) {
        //TODO Is this ok?
        parent.error(messageText);
    }

    public boolean eof() {
        fillBufferIfEmpty();
        final int tokenIndex = currentToken - droppedTokens;
        return tokenIndex >= tokens.size() && parent.eof();
    }

    public void define(@NotNull String definitionName, @Nullable List<@NotNull String> arguments, @Nullable List<@NotNull ForeignLeafType> redefinedTo) {
        final Redefinition redefinition = new Redefinition(definitionName, arguments, redefinedTo, currentToken);
        Redefinition chain = this.redefinitions.get(definitionName);
        if (chain == null || chain.fromTokenPosition < currentToken) {
            redefinition.previous = chain;
            redefinitions.put(definitionName, redefinition);
            return;
        } else if (chain.fromTokenPosition > currentToken) {
            while (true) {
                if (chain.previous == null || chain.previous.fromTokenPosition < currentToken) {
                    redefinition.previous = chain.previous;
                    chain.previous = redefinition;
                    return;
                }
                chain = chain.previous;
            }
        }

        LOG.error("Can't create two redefinitions at the same token position");
        assert false;
    }

    private static final class Redefinition {
        public final @NotNull String name;
        private final @Nullable List<@NotNull String> arguments;
        public final @Nullable List<@NotNull ForeignLeafType> redefinedTo;
        public final int fromTokenPosition;
        public @Nullable Redefinition previous;

        private Redefinition(@NotNull String name, @Nullable List<@NotNull String> arguments, @Nullable List<@NotNull ForeignLeafType> redefinedTo, int fromTokenPosition) {
            this.name = name;
            this.arguments = arguments;
            this.redefinedTo = redefinedTo;
            this.fromTokenPosition = fromTokenPosition;
        }
    }

    public void setDebugMode(boolean dbgMode) {
        parent.setDebugMode(dbgMode);
        debugMode = dbgMode;
    }

    private PMark addMarker(PsiBuilder.Marker parent, int tokenPosition, final PMark precede) {
        final PMark marker = new PMark(parent, tokenPosition);
        if (precede != null) {
            final PMark previous = precede.previous;
            if (previous != null) {
                previous.next = marker;
            }
            marker.previous = previous;
            marker.next = precede;
            precede.previous = marker;
        } else {
            final PMark before = pendingMarkers.floor(marker);
            if (before != null && before.tokenPosition == tokenPosition) {
                // This marker is really after it!
                assert before.next == null;
                before.next = marker;
                marker.previous = before;
            }
        }
        marker.alive = debugMode ? new Exception() : marker;
        pendingMarkers.add(marker);
        return marker;
    }

    private void removeMarker(PMark marker, boolean rollback) {
        pendingMarkers.remove(marker);
        final PMark previous = marker.previous;
        PMark next = marker.next;
        // The previous/next must always be correct before interacting with the tree
        if (previous != null) {
            previous.next = next;
        }
        if (next != null) {
            next.previous = previous;
        }
        if (rollback) {
            while (next != null) {
                pendingMarkers.remove(next);
                next = next.next;
                if (previous != null) {
                    previous.next = next;
                }
                if (next != null) {
                    next.previous = previous;
                } else {
                    break;
                }
            }

            currentToken = marker.tokenPosition;
            pendingMarkers.removeIf(removeFutureMarkers);
        }

        // Maybe we can trim the token buffer now?
        if (!pendingMarkers.isEmpty()) {
            int firstRequiredTokenPosition = pendingMarkers.first().tokenPosition;
            assert firstRequiredTokenPosition <= currentToken;

            while (!tokens.isEmpty() && droppedTokens < firstRequiredTokenPosition) {
                tokens.removeFirst();
                droppedTokens++;
            }
        }
    }

    private final Predicate<PMark> removeFutureMarkers = m -> {
        if (m.tokenPosition > currentToken) {
            m.alive = false;
            final PMark previous = m.previous;
            PMark next = m.next;
            // The previous/next must always be correct when interacting with the tree
            if (previous != null) {
                previous.next = next;
            }
            if (next != null) {
                next.previous = previous;
            }
            m.previous = null;
            m.next = null;
            return true;
        } else return false;
    };

    public final class PMark implements Marker, Comparable<PMark> {

        /** Contains this if alive, null if not alive, Throwable with stack trace if alive and created when debug mode was active. */
        private Object alive = null;
        private final PsiBuilder.Marker parent;
        private final int tokenPosition;

        /** All tokens on the same token position form a linked list. */
        private PMark previous = null, next = null;

        private PMark(PsiBuilder.Marker parent, int tokenPosition) {
            this.parent = parent;
            this.tokenPosition = tokenPosition;
        }

        private void ensureAlive() {
            if (alive == null) throw new IllegalStateException("PMark is used up");
        }

        public PMark precede() {
            ensureAlive();
            return addMarker(parent.precede(), tokenPosition, this);
        }

        public void drop() {
            ensureAlive();
            parent.drop();
            removeMarker(this, false);
        }

        public void rollbackTo() {
            ensureAlive();
            parent.rollbackTo();
            removeMarker(this, true);
        }

        public void done(@NotNull IElementType type) {
            ensureAlive();
            parent.done(type);
            removeMarker(this, false);
        }

        public void error(@NotNull String message) {
            ensureAlive();
            parent.error(message);
            removeMarker(this, false);
        }

        @Override
        public int compareTo(@NotNull PreprocessorPsiBuilderAdapter.PMark o) {
            final int compare = Integer.compare(this.tokenPosition, o.tokenPosition);
            if (compare != 0) return compare;
            if (this == o) return 0;
            // These have the same position in token stream, we must distinguish them by the linked list

            // -1 is this object less than = is this object somewhere in o[.previous]+?
            PMark previous = o.previous;
            while (previous != null) {
                if (previous == this) return -1;
                previous = previous.previous;
            }

            // +1 is this object greater than = is this object somewhere in o[.next]+?
            PMark next = o.next;
            while (next != null) {
                if (next == this) return 1;
                next = next.next;
            }

            // One of the objects is not inserted yet! The not-inserted one is greater.
            if (this.alive == null) {
                return 1;
            }
            if (o.alive == null) {
                return -1;
            }

            // This is not possible
            assert false : "Markers are not comparable: "+this+" "+o;
            return 0;
        }
    }


}
