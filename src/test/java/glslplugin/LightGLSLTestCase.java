package glslplugin;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.PsiFileFactory;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import glslplugin.lang.GLSLFileType;
import glslplugin.lang.parser.GLSLFile;

import java.io.File;
import java.io.IOException;

/**
 * Base for light test cases
 */
public abstract class LightGLSLTestCase extends LightPlatformCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return new File("testdata").getAbsolutePath();
    }

    private File getTestFile(String fileName){
        return new File(new File(getTestDataPath()), fileName);
    }

    public GLSLFile parseFile(String filePath){
        final String testFileContent;
        try {
            testFileContent = FileUtil.loadFile(getTestFile(filePath), "UTF-8", true);
            assertNotNull(testFileContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return ((GLSLFile) PsiFileFactory.getInstance(getProject()).createFileFromText("dummy.glsl", GLSLFileType.INSTANCE, testFileContent));
    }
}
