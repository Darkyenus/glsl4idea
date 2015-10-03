package glslplugin.lang.elements.reference;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import glslplugin.lang.elements.preprocessor.GLSLDefineDirective;
import glslplugin.lang.elements.preprocessor.GLSLRedefinedToken;

/**
 *
 * Created by abigail on 08/07/15.
 */
public class GLSLMacroReference extends GLSLReferenceBase<GLSLRedefinedToken, GLSLDefineDirective> {
    public GLSLMacroReference(GLSLRedefinedToken source) {
        super(source);
    }

    @Override
    public GLSLDefineDirective resolve() {
        PsiElement current = source.getPrevSibling();
        if (current == null) current = source.getParent();

        while (current != null) {
            final GLSLDefineDirective found = resolveDeeperIn(current);
            if(found != null)return found;

            if (current.getPrevSibling() == null) {
                current = current.getParent();
                if (current instanceof PsiFile) return null;
            } else {
                current = current.getPrevSibling();
            }
        }
        return null;
    }

    private GLSLDefineDirective resolveDeeperIn(PsiElement current){
        while(current != null){
            if (current instanceof GLSLDefineDirective) {
                GLSLDefineDirective directive = (GLSLDefineDirective) current;
                if (source.getName().equals(directive.getName())) return directive;
            }else{
                final GLSLDefineDirective deeper = resolveDeeperIn(current.getLastChild());
                if(deeper != null)return deeper;
            }
            current = current.getPrevSibling();
        }
        return null;
    }
}
