package glslplugin.formatter;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.codeStyle.CodeStyleManager;
import glslplugin.LightGLSLTestCase;
import glslplugin.lang.GLSLFileType;

public class GLSLFormattingTest extends LightGLSLTestCase {

    private void assertFormatted(String before, String after) {
        myFixture.configureByText(GLSLFileType.INSTANCE, before);
        WriteCommandAction.writeCommandAction(getProject()).run(() ->
                CodeStyleManager.getInstance(getProject()).reformat(myFixture.getFile()));
        assertEquals(after, myFixture.getFile().getText());
    }

    public void testMultilineConditionUsesContinuationIndent() {
        assertFormatted("""
                if (a
                && b
                && c
                ) {
                }
                """, """
                if (a
                    && b
                    && c
                ) {
                }
                """);
    }

    public void testConditionStartingOnNextLineUsesContinuationIndent() {
        assertFormatted("""
                if (
                a
                && b
                ) {
                }
                """, """
                if (
                    a
                    && b
                ) {
                }
                """);
    }

    public void testMultilineConditionWithLogicalOrChainKeepsUniformIndent() {
        assertFormatted("""
                if (
                    !radianceValid
                    || newTargetWeight <= 0.0
                    || any(isnan(radiance))
                    || isnan(newTargetWeight)
                ) {
                    return false;
                }
                """, """
                if (
                    !radianceValid
                    || newTargetWeight <= 0.0
                    || any(isnan(radiance))
                    || isnan(newTargetWeight)
                ) {
                    return false;
                }
                """);
    }

    public void testMultilineConditionStartingOnSameLineKeepsUniformIndent() {
        assertFormatted("""
                if (!radianceValid
                    || newTargetWeight <= 0.0
                    || any(isnan(radiance))
                    || isnan(newTargetWeight)
                ) {
                    return false;
                }
                """, """
                if (!radianceValid
                    || newTargetWeight <= 0.0
                    || any(isnan(radiance))
                    || isnan(newTargetWeight)
                ) {
                    return false;
                }
                """);
    }
}
