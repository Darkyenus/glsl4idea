package glslplugin.lang.elements.preprocessor;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.reference.GLSLAbstractReference;
import glslplugin.lang.elements.reference.GLSLReferencingElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.function.Predicate;

/**
 *
 * Created by abigail on 08/07/15.
 */
public class GLSLRedefinedToken extends GLSLElementImpl implements GLSLReferencingElement {

    public GLSLRedefinedToken(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Override
    public @Nullable PsiElement getReferencingIdentifierForRenaming() {
        return findChildByType(GLSLTokenTypes.IDENTIFIER);
    }

    @Nullable
    public String getRedefinedTokenName() {
        final PsiElement identifier = getReferencingIdentifierForRenaming();
        return identifier == null ? null : identifier.getText();
    }

    public static final class RedefinedTokenReference extends GLSLAbstractReference<GLSLRedefinedToken> {

        public RedefinedTokenReference(@NotNull GLSLRedefinedToken element) {
            super(element);
        }

        @Override
        public @Nullable GLSLDefineDirective resolve() {
            final String tokenName = element.getRedefinedTokenName();
            if (tokenName == null) return null;

            GLSLDefineDirective[] result = new GLSLDefineDirective[1];
            walkWholeTreeUp(element, d -> {
                if (tokenName.equals(d.getName())) {
                    result[0] = d;
                    return false;
                }
                return true;
            });
            return result[0];
        }

        @Override
        public Object @NotNull [] getVariants() {
            final ArrayList<GLSLDefineDirective> results = new ArrayList<>();
            walkWholeTreeUp(element, d -> {
                results.add(d);
                return true;
            });
            return results.toArray();
        }

        private static void walkWholeTreeUp(PsiElement from, Predicate<GLSLDefineDirective> visitor) {
            PsiElement current = from.getPrevSibling();
            if (current == null) current = from.getParent();

            while (current != null) {
                if (!visitDeeperIn(current, visitor)) return;

                if (current.getPrevSibling() == null) {
                    current = current.getParent();
                    if (current instanceof PsiFile) return;
                } else {
                    current = current.getPrevSibling();
                }
            }
        }

        private static boolean visitDeeperIn(PsiElement current, Predicate<GLSLDefineDirective> visitor){
            while (current != null) {
                if (current instanceof GLSLDefineDirective directive) {
                    if (!visitor.test(directive)) return false;
                }else{
                    if (!visitDeeperIn(current.getLastChild(), visitor)) return false;
                }
                current = current.getPrevSibling();
            }
            return true;
        }
    }

    /**
     * @return reference to the #define which caused the redefinition of this token
     */
    @Override
    public RedefinedTokenReference getReference() {
        return new RedefinedTokenReference(this);
    }
}
