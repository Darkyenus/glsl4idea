package glslplugin.extensions;

import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.VirtualFile;
import glslplugin.lang.GLSLFileType;

/**
 * Created by abigail on 29/04/15.
 */
public class GLSLProblemFileHighlightFilter implements Condition<VirtualFile> {
    @Override
    public boolean value(VirtualFile virtualFile) {
        return virtualFile.getFileType() instanceof GLSLFileType;
    }
}
