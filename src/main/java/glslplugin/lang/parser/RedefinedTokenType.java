package glslplugin.lang.parser;

import com.intellij.lang.ForeignLeafType;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 *
 */
public class RedefinedTokenType extends ForeignLeafType {

    /** Names of #defines through which this token was created.
     * Used to break recursive definitions, eg. #define FOO FOO. */
    public final String[] redefinedThrough;

    public RedefinedTokenType(@NotNull ForeignLeafType delegate, String redefinedThrough) {
        super(delegate.getDelegate(), delegate.getValue());
        this.redefinedThrough = new String[]{redefinedThrough};
    }

    public RedefinedTokenType(@NotNull IElementType delegate, @NotNull CharSequence value, String[] redefinedThrough) {
        super(delegate, value);
        this.redefinedThrough = redefinedThrough;
    }

    public RedefinedTokenType redefineAlsoThrough(String name) {
        final String[] through = new String[redefinedThrough.length + 1];
        System.arraycopy(this.redefinedThrough, 0, through, 0, this.redefinedThrough.length);
        through[this.redefinedThrough.length] = name;
        return new RedefinedTokenType(getDelegate(), getValue(), through);
    }

    @Override
    public String toString() {
        return "RedefinedTokenType("+getDelegate()+", '"+getValue()+"', "+ Arrays.toString(redefinedThrough)+")";
    }
}
