package glslplugin.util;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Iterates through the tree from some start point until the end of the file */
public class ASTTreeIterator {

    public static @Nullable ASTNode next(@NotNull ASTNode origin) {
        // Try visiting children
        final ASTNode child = origin.getFirstChildNode();
        if (child != null) {
            return child;
        }

        while (true) {
            // No more children, are there any siblings?
            final ASTNode nextSibling = origin.getTreeNext();
            if (nextSibling != null) {
                return nextSibling;
            }

            // Then we have to go up and to the right
            origin = origin.getTreeParent();
            if (origin == null) {
                // Can't go on
                return null;
            }

            // We have already visited origin at some point, so we need to go further right
        }
    }

    public static @Nullable ASTNode nextLeaf(@NotNull ASTNode origin) {
        ASTNode next = next(origin);
        while (next != null && next.getFirstChildNode() != null) {
            next = next(next);
        }
        return next;
    }
}
