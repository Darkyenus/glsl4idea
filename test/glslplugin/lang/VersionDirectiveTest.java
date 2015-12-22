package glslplugin.lang;

import glslplugin.LightGLSLTestCase;
import glslplugin.TestUtils;
import glslplugin.lang.parser.GLSLFile;

/**
 * Created by wyozi on 19.12.2015.
 */
public class VersionDirectiveTest extends LightGLSLTestCase {
    @Override
    protected String getTestDataPath() {
        return super.getTestDataPath() + "/lang/version";
    }

    public void testDefaultVersion() {
        GLSLFile file = TestUtils.parseFile(getProject(), getTestDataPath() + "/defaultVersion.glsl");
        assertEquals(GLSLFile.GLSL_DEFAULT_VERSION, file.getGLSLVersion());
    }

    public void testSpecifiedVersion() {
        GLSLFile file = TestUtils.parseFile(getProject(), getTestDataPath() + "/specifiedVersion.glsl");
        assertEquals(330, file.getGLSLVersion());
    }
}
