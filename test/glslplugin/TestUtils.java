package glslplugin;

import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.LocalTimeCounter;
import glslplugin.lang.parser.GLSLFile;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;

/**
 * Credits to lua-for-idea project.
 */
public class TestUtils {
    public static GLSLFile createPseudoPhysicalFile(final Project project,
                                                    final String fileName,
                                                    final String text) throws IncorrectOperationException {
        return ((GLSLFile) PsiFileFactory.getInstance(project).createFileFromText(fileName, GLSLSupportLoader.GLSL, text));
    }

    public static String getTestDataPath() {
        return FileUtil.toSystemIndependentName("testdata/");
    }

    public static String readFileAsString(String filePath) {
        String content;
        try {
            content = new String(FileUtil.loadFileText(new File(filePath)));
            content = StringUtil.replace(content, "\r", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assert.assertNotNull(content);
        return content;
    }

    public static GLSLFile parseFile(Project project, String filePath) {
        return createPseudoPhysicalFile(project, "dummy.glsl", readFileAsString(filePath));
    }
}
