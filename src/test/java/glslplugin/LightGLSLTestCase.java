package glslplugin;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.psi.PsiFileFactory;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import glslplugin.lang.GLSLFileType;
import glslplugin.lang.parser.GLSLFile;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

/**
 * Base for light test cases
 */
public abstract class LightGLSLTestCase extends BasePlatformTestCase {

    @Override
    protected String getTestDataPath() {
        return new File("testdata").getAbsolutePath();
    }

    private static void allowBuiltinResourceRootAccess() {
        final URL builtinUrl = LightGLSLTestCase.class.getClassLoader().getResource("glsl/builtin.glsl");
        if (builtinUrl == null) {
            return;
        }

        try {
            if ("jar".equals(builtinUrl.getProtocol())) {
                final String urlText = builtinUrl.toString();
                final int separator = urlText.indexOf("!/");
                if (separator < 0) {
                    return;
                }
                final URL jarUrl = new URL(urlText.substring("jar:".length(), separator));
                allowVfsRootAccess(Path.of(jarUrl.toURI()).toString());
            } else if ("file".equals(builtinUrl.getProtocol())) {
                allowVfsRootAccess(Path.of(builtinUrl.toURI()).toString());
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Failed to allow GLSL builtin resource root", e);
        }
    }

    private static void allowVfsRootAccess(String root) {
        String roots = System.getProperty("vfs.additional-allowed-roots");
        if (roots == null || roots.isEmpty()) {
            roots = root;
        } else {
            roots = roots + File.pathSeparator + root;
        }
        System.setProperty("vfs.additional-allowed-roots", roots);
    }

    static {
        allowBuiltinResourceRootAccess();
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
