package glslplugin.extensions;

import com.intellij.codeInsight.editorActions.enter.EnterBetweenBracesDelegate;

/**
 * @author Wyozi
 */
public class GLSLEnterHandlerDelegate extends EnterBetweenBracesDelegate {

    @Override
    protected boolean isBracePair(char c1, char c2) {
        return c1 == '{' && c2 == '}';
    }

}
