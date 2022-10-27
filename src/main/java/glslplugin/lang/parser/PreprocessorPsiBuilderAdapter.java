package glslplugin.lang.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ForeignLeafType;
import com.intellij.lang.PsiBuilder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.tree.IElementType;
import glslplugin.lang.elements.GLSLElementTypes;
import glslplugin.lang.elements.GLSLTokenTypes;
import kotlin.collections.ArrayDeque;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Predicate;

/**
 * Does not actually implement {@link PsiBuilder}, because some methods are not implemented and this guarantees that they are not used by accident.
 */
public class PreprocessorPsiBuilderAdapter {

    private static final Logger LOG = Logger.getInstance(PreprocessorPsiBuilderAdapter.class);

    final @NotNull PsiBuilder parent;

    private static long position(int parent, int replacement) {
        return (((long)parent) << 32) | (((long) replacement) & 0xFFFF_FFFFL);
    }
    private static int parentPosition(long position) {
        return (int) (position >>> 32);
    }
    private static int replacementPosition(long position) {
        return (int) position;
    }

    /** To help with memory consumption, all tokens that will definitely not be needed are dropped from tokens.
     * The amount of dropped tokens is recorded here to aid with proper indexing, because all position numbers are indexed from the start. */
    private int droppedTokens = 0;
    /** The current token, in absolute position, is recorded here.
     * {@link #advanceLexer()} moves it forward, marker rollbacks move it backward.
     * The position is combination of position in parent and position in token replacements. */
    private long currentPosition = position(0, 0);

    /** At parent position holds the replacement tokens. */
    private final ArrayDeque<@Nullable Replacements> tokenReplacements = new ArrayDeque<>();
    private final TreeSet<PMark> pendingMarkers = new TreeSet<>();

    private final HashMap<String, Redefinition> redefinitions = new HashMap<>();

    private boolean debugMode = false;

    private boolean allowRedefinitions = true;

    private static final class Replacements extends ArrayList<@NotNull ForeignLeafType> {
        public final PsiBuilder.Marker marker;

        private Replacements(PsiBuilder.Marker marker) {
            this.marker = marker;
        }
    }

    public PreprocessorPsiBuilderAdapter(@NotNull PsiBuilder parent) {
        this.parent = parent;
    }

    public void setAllowRedefinitions(boolean allowRedefinitions) {
        if (this.allowRedefinitions == allowRedefinitions) return;
        this.allowRedefinitions = allowRedefinitions;

        int currentParent = parentPosition(currentPosition);
        assert replacementPosition(currentPosition) == 0 : "Can't change allowRedefinitions, already in redefinition";

        final int tokenIndex = currentParent - droppedTokens;
        if (tokenIndex >= tokenReplacements.getSize()) return; // EOF
        assert tokenIndex + 1 == tokenReplacements.getSize();

        final Replacements replacement = tokenReplacements.removeLast();
        if (replacement != null) {
            replacement.marker.rollbackTo();
        }
    }

    public @NotNull ASTNode getTreeBuilt() {
        if (debugMode) {
            for (PMark marker : pendingMarkers) {
                LOG.error("Incomplete marker: "+marker.alive);
            }
        }
        for (Replacements tokenReplacement : tokenReplacements) {
            if (tokenReplacement != null) {
                tokenReplacement.marker.drop();
            }
        }
        tokenReplacements.clear();

        return parent.getTreeBuilt();
    }

    private static final ForeignLeafType[] EMPTY_FOREIGN_LEAF_TYPE_ARRAY = new ForeignLeafType[0];

    private void fillBufferIfEmpty() {
        final int currentParent = parentPosition(currentPosition);
        final int currentReplaced = replacementPosition(currentPosition);

        final int tokenIndex = currentParent - droppedTokens;
        if (tokenIndex < tokenReplacements.size()) {
            var replacement = tokenReplacements.get(tokenIndex);
            if (replacement == null || currentReplaced < replacement.size()) {
                // Not empty
                return;
            }
        }
        if (parent.eof()) {
            // Nothing else to do
            return;
        }
        assert tokenIndex == tokenReplacements.size();

        // We must put something in
        final String text = parent.getTokenText();
        final Redefinition redefinition = allowRedefinitions ? redefinitions.get(text) : null;
        final @Nullable Replacements result;
        if (redefinition != null && redefinition.redefinedTo != null) {
            // The next token is redefined, skip it (mark as redefined) and insert tokens for redefinitions
            result = new Replacements(parent.mark());
            parent.remapCurrentToken(GLSLTokenTypes.PREPROCESSOR_REDEFINED);
            parent.advanceLexer();

            final List<@NotNull String> arguments = redefinition.arguments;
            if (arguments == null) {
                result.addAll(redefinition.redefinedTo);
            } else {
                // This is a function macro
                if (parent.getTokenType() != GLSLTokenTypes.LEFT_PAREN) {
                    parent.error("Expected macro parameters");
                } else {
                    final PsiBuilder.Marker mark = parent.mark();
                    parent.remapCurrentToken(GLSLTokenTypes.PREPROCESSOR_MACRO_ARGUMENT);
                    parent.advanceLexer();

                    final var actualArguments = new ArrayList<ForeignLeafType[]>();
                    final var actualArgument = new ArrayList<ForeignLeafType>();
                    int parenNesting = 0;
                    while (true) {
                        final IElementType type = parent.getTokenType();
                        if (type == null) {
                            parent.error("Unexpected end of the file, expected function macro arguments");
                            return;
                        }

                        parent.remapCurrentToken(GLSLTokenTypes.PREPROCESSOR_MACRO_ARGUMENT);
                        if (parenNesting == 0 && (type == GLSLTokenTypes.COMMA || type == GLSLTokenTypes.RIGHT_PAREN)) {
                            actualArguments.add(actualArgument.toArray(EMPTY_FOREIGN_LEAF_TYPE_ARRAY));
                            actualArgument.clear();
                            parent.advanceLexer();
                            if (type == GLSLTokenTypes.RIGHT_PAREN) {
                                break;
                            }
                        } else {
                            final String tokenText = parent.getTokenText();
                            parent.advanceLexer();

                            actualArgument.add(new ForeignLeafType(type, tokenText == null ? "" : tokenText));
                            if (type == GLSLTokenTypes.LEFT_PAREN) {
                                parenNesting++;
                            } else if (type == GLSLTokenTypes.RIGHT_PAREN) {
                                parenNesting--;
                            }
                        }
                    }

                    mark.done(GLSLElementTypes.PREPROCESSOR_FUNCTION_MACRO_ARGUMENTS);

                    if (actualArguments.size() != arguments.size()) {
                        mark.precede().error("Expected "+arguments.size()+" arguments, but found "+actualArguments.size()+".");
                    }

                    for (ForeignLeafType type : redefinition.redefinedTo) {
                        final int argumentIndex = arguments.indexOf(type.getValue());
                        if (argumentIndex >= 0) {
                            // Insert argument
                            Collections.addAll(result, actualArguments.get(argumentIndex));
                        } else {
                            // Insert the token literally
                            result.add(type);
                        }
                    }
                }
            }
        } else {
            result = null;// = check parent
        }
        tokenReplacements.add(result);
    }

    public void advanceLexer() {
        fillBufferIfEmpty();
        int currentParent = parentPosition(currentPosition);
        int currentReplaced = replacementPosition(currentPosition);

        final int tokenIndex = currentParent - droppedTokens;
        if (tokenIndex >= tokenReplacements.getSize()) return; // EOF
        final List<@NotNull ForeignLeafType> replacement = tokenReplacements.get(tokenIndex);
        if (replacement == null) {
            parent.advanceLexer();
            currentParent++;
            currentReplaced = 0;
            //TODO Verify that the next token is not empty
        } else if (currentReplaced >= replacement.size()) {
            return; //EOF
        } else {
            parent.mark().done(replacement.get(currentReplaced));
            currentReplaced++;
            if (currentReplaced < replacement.size()) {
                // Done, ok
                currentPosition = position(currentParent, currentReplaced);
                return;
            } else {
                currentParent++;
                currentReplaced = 0;
            }
        }

        // Verify that the position we have landed on is valid
        // Note: It may appear that this setting would be needed for the very first token as well, but
        // since it is impossible for any definitions to exist at that point, this is moot.
        while (true) {
            currentPosition = position(currentParent, currentReplaced);
            fillBufferIfEmpty();
            final int newTokenIndex = currentParent - droppedTokens;
            if (newTokenIndex >= tokenReplacements.getSize()) {
                return; // EOF
            }
            final List<@NotNull ForeignLeafType> newReplacement = tokenReplacements.get(newTokenIndex);
            if (newReplacement == null || newReplacement.size() > 0) {
                // Valid, ok
                return;
            }
            // Replaced into nothing, keep looking
            currentParent++;
        }
    }

    public @Nullable IElementType getTokenType() {
        fillBufferIfEmpty();
        int currentParent = parentPosition(currentPosition);
        final int tokenIndex = currentParent - droppedTokens;
        // advanceLexer makes sure that we are never in an empty replacement or at the end of it
        if (tokenIndex >= tokenReplacements.size()) {
            return null;
        }
        final List<@NotNull ForeignLeafType> replacements = tokenReplacements.get(tokenIndex);
        if (replacements == null) {
            final IElementType tokenType = parent.getTokenType();
            assert tokenType != GLSLTokenTypes.PREPROCESSOR_REDEFINED;
            return tokenType;
        }
        return replacements.get(replacementPosition(currentPosition)).getDelegate();
    }

    public @NonNls @Nullable String getTokenText() {
        fillBufferIfEmpty();
        int currentParent = parentPosition(currentPosition);
        final int tokenIndex = currentParent - droppedTokens;
        // advanceLexer makes sure that we are never in an empty replacement or at the end of it
        if (tokenIndex >= tokenReplacements.size()) {
            return null;
        }
        final List<@NotNull ForeignLeafType> replacements = tokenReplacements.get(tokenIndex);
        if (replacements == null) {
            return parent.getTokenText();
        }
        return replacements.get(replacementPosition(currentPosition)).getValue();
    }

    public @NotNull PreprocessorPsiBuilderAdapter.PMark mark() {
        return addMarker(parent.mark(), currentPosition, null);
    }

    public void error(@NotNull String messageText) {
        //TODO Is this ok?
        parent.error(messageText);
    }

    public boolean eof() {
        fillBufferIfEmpty();

        int currentParent = parentPosition(currentPosition);
        final int tokenIndex = currentParent - droppedTokens;
        // advanceLexer makes sure that we are never in an empty replacement or at the end of it
        final boolean eof = tokenIndex >= tokenReplacements.size();
        if (eof) {
            return true;
        }
        return false;
    }

    public void define(@NotNull String definitionName, @Nullable List<@NotNull String> arguments, @Nullable List<@NotNull ForeignLeafType> redefinedTo) {
        final Redefinition redefinition = new Redefinition(definitionName, arguments, redefinedTo, currentPosition);
        Redefinition chain = this.redefinitions.get(definitionName);
        if (chain == null || chain.fromPosition < currentPosition) {
            redefinition.previous = chain;
            redefinitions.put(definitionName, redefinition);
        } else if (chain.fromPosition == currentPosition) {
            // Redefine
            LOG.warn("Redefining " + chain + " -> " + redefinition);
            redefinition.previous = chain.previous;
            this.redefinitions.put(definitionName, redefinition);
        } else while (true) {
            if (chain.previous == null || chain.previous.fromPosition < currentPosition) {
                redefinition.previous = chain.previous;
                chain.previous = redefinition;
                break;
            } else if (chain.previous.fromPosition == currentPosition) {
                LOG.warn("Redefining " + chain.previous + " -> " + redefinition);
                redefinition.previous = chain.previous.previous;
                chain.previous = redefinition;
                break;
            } else {
                chain = chain.previous;
            }
        }
    }

    private static final class Redefinition {
        public final @NotNull String name;
        private final @Nullable List<@NotNull String> arguments;
        public final @Nullable List<@NotNull ForeignLeafType> redefinedTo;
        public final long fromPosition;
        public @Nullable Redefinition previous;

        private Redefinition(@NotNull String name, @Nullable List<@NotNull String> arguments, @Nullable List<@NotNull ForeignLeafType> redefinedTo, long fromPosition) {
            this.name = name;
            this.arguments = arguments;
            this.redefinedTo = redefinedTo;
            this.fromPosition = fromPosition;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Redefinition that = (Redefinition) o;

            if (fromPosition != that.fromPosition) return false;
            if (!name.equals(that.name)) return false;
            if (!Objects.equals(arguments, that.arguments)) return false;
            return Objects.equals(redefinedTo, that.redefinedTo);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + (arguments != null ? arguments.hashCode() : 0);
            result = 31 * result + (redefinedTo != null ? redefinedTo.hashCode() : 0);
            result = 31 * result + Long.hashCode(fromPosition);
            return result;
        }

        @Override
        public String toString() {
            return "Redefinition{" +
                    "name='" + name + '\'' +
                    ", arguments=" + arguments +
                    ", redefinedTo=" + redefinedTo +
                    '}';
        }
    }

    public void setDebugMode(boolean dbgMode) {
        parent.setDebugMode(dbgMode);
        debugMode = dbgMode;
    }

    private PMark addMarker(PsiBuilder.Marker parent, long tokenPosition, final PMark precede) {
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

            currentPosition = marker.tokenPosition;
            pendingMarkers.removeIf(removeFutureMarkers);

            // Drop extra tokens so that they may get re-processed later,
            // trying to reuse them is tricky. But don't drop already reprocessed.
            final int targetTokenCount = parentPosition(currentPosition) - droppedTokens + 1;
            while (tokenReplacements.size() > targetTokenCount) {
                // Don't drop them, already dropped by rollback
                tokenReplacements.removeLast();
            }
        }

        // Maybe we can trim the token buffer now?
        if (!pendingMarkers.isEmpty()) {
            long firstRequiredPosition = pendingMarkers.first().tokenPosition;
            assert firstRequiredPosition <= currentPosition;
            int firstRequiredParentToken = parentPosition(firstRequiredPosition);

            while (!tokenReplacements.isEmpty() && droppedTokens < firstRequiredParentToken) {
                final Replacements removed = tokenReplacements.removeFirst();
                if (removed != null) {
                    removed.marker.drop();
                }
                droppedTokens++;
            }
        }
    }

    private final Predicate<PMark> removeFutureMarkers = m -> {
        if (m.tokenPosition > currentPosition) {
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
        private final long tokenPosition;

        /** All tokens on the same token position form a linked list. */
        private PMark previous = null, next = null;

        private PMark(PsiBuilder.Marker parent, long tokenPosition) {
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
            final int compare = Long.compareUnsigned(this.tokenPosition, o.tokenPosition);
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
