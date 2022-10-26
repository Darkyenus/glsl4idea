package glslplugin.lang.parser;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Exists just for shorter type names.
 */
interface Marker {

    Marker precede();

    void drop();

    void rollbackTo();

    void done(@NotNull IElementType type);

    void error(@NotNull String message);
}
