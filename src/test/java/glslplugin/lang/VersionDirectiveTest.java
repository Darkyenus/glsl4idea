package glslplugin.lang;

import glslplugin.LightGLSLTestCase;
import glslplugin.lang.parser.GLSLFile;

/**
 * Test if the versions are inferred correctly
 *
 * Created by wyozi on 19.12.2015.
 */
public class VersionDirectiveTest extends LightGLSLTestCase {

    @Override
    protected String getTestDataPath() {
        return super.getTestDataPath() + "/lang/version";
    }

    public void testDefaultVersion() {
        GLSLFile file = parseFile("defaultVersion.glsl");
        assertEquals(GLSLFile.GLSL_DEFAULT_VERSION, file.getGLSLVersion());
    }

    public void testSpecifiedVersion() {
        GLSLFile file = parseFile("specifiedVersion.glsl");
        assertEquals(330, file.getGLSLVersion());
    }
}
