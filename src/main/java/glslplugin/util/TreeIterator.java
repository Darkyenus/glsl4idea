package glslplugin.util;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Iterates through the tree from some start point until the end of the file */
public class TreeIterator {

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

    public static @Nullable PsiElement next(@NotNull PsiElement origin) {
        // Try visiting children
        final PsiElement child = origin.getFirstChild();
        if (child != null) {
            return child;
        }

        while (true) {
            // No more children, are there any siblings?
            final PsiElement nextSibling = origin.getNextSibling();
            if (nextSibling != null) {
                return nextSibling;
            }

            // Then we have to go up and to the right
            origin = origin.getParent();
            if (origin == null) {
                // Can't go on
                return null;
            }

            // We have already visited origin at some point, so we need to go further right
        }
    }

    public static @Nullable PsiElement previous(@NotNull PsiElement origin) {
        // Try visiting children
        final PsiElement child = origin.getLastChild();
        if (child != null) {
            return child;
        }

        while (true) {
            // No more children, are there any siblings?
            final PsiElement nextSibling = origin.getPrevSibling();
            if (nextSibling != null) {
                return nextSibling;
            }

            // Then we have to go up and to the right
            origin = origin.getParent();
            if (origin == null) {
                // Can't go on
                return null;
            }

            // We have already visited origin at some point, so we need to go further right
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> @Nullable T previous(@NotNull PsiElement origin, @NotNull Class<T> type) {
        PsiElement p = previous(origin);
        while (p != null && !type.isInstance(p)) {
            p = previous(p);
        }
        return (T) p;
    }
}
