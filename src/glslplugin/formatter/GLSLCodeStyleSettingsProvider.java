package glslplugin.formatter;

import com.intellij.application.options.*;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleConfigurable;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import glslplugin.lang.GLSLLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provider for {@link GLSLCodeStyleSettings} and glue for things around it.
 */
public class GLSLCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {

	@NotNull
	@Override
	public CodeStyleConfigurable createConfigurable(@NotNull CodeStyleSettings settings, @NotNull CodeStyleSettings modelSettings) {
		return new CodeStyleAbstractConfigurable(settings, modelSettings, "GLSL") {
			protected CodeStyleAbstractPanel createPanel(CodeStyleSettings settings) {
				return new GLSLCodeStyleMainPanel(this.getCurrentSettings(), settings);
			}
		};
	}

	@Nullable
	@Override
	public CustomCodeStyleSettings createCustomSettings(CodeStyleSettings settings) {
		return new GLSLCodeStyleSettings(settings);
	}

	@Nullable
	@Override
	public String getCodeSample(@NotNull SettingsType settingsType) {
		return "#version 330 core\n" +
				"\n" +
				"// Some definitions\n" +
				"#if LIGHT_TYPE == 1\n" +
				"#define LIGHT_STRUCTNAME SpotLight\n" +
				"#else\n" +
				"#define LIGHT_STRUCTNAME PointLight\n" +
				"#endif\n" +
				"\n" +
				"// Normal variables\n" +
				"in vec2 fragTexCoords;\n" +
				"out vec4 fragColor;\n" +
				"\n" +
				"uniform int activeLightCount;\n" +
				"\n" +
				"// Interface block\n" +
				"in VertexData\n" +
				"{\n" +
				"    vec3 color;\n" +
				"    vec2 texCoord;\n" +
				"} inData[];\n" +
				"\n" +
				"layout(row_major) uniform MatrixBlock\n" +
				"{\n" +
				"    mat4 projection;\n" +
				"    layout(column_major) mat4 modelview;\n" +
				"} matrices[3];\n" +
				"\n" +
				"void main() {\n" +
				"    func();\n" +
				"}\n" +
				"\n" +
				"vec3 addVectors(vec3 a, vec3 b) {\n" +
				"    return a + b;\n" +
				"}\n" +
				"\n" +
				"void miscFunc(int t) {\n" +
				"    if (x == y) {\n" +
				"        int accum = 0;\n" +
				"        for (int i = 0;i < t; i++) {\n" +
				"            if (i == 2) {\n" +
				"                continue;\n" +
				"            }\n" +
				"            accum += 0;\n" +
				"        }\n" +
				"    } else {\n" +
				"        discard;\n" +
				"    }\n" +
				"}";
	}

	@Nullable
	@Override
	public IndentOptionsEditor getIndentOptionsEditor() {
		return new SmartIndentOptionsEditor(this);
	}

	@NotNull
	@Override
	public Language getLanguage() {
		return GLSLLanguage.GLSL_LANGUAGE;
	}

	private static class GLSLCodeStyleMainPanel extends TabbedLanguageCodeStylePanel {

		protected GLSLCodeStyleMainPanel(CodeStyleSettings currentSettings, CodeStyleSettings settings) {
			super(GLSLLanguage.GLSL_LANGUAGE, currentSettings, settings);
		}

		@Override
		protected void initTabs(CodeStyleSettings settings) {
			addIndentOptionsTab(settings);
			// Other tabs appear to be blank and I don't know how to make them contain all options.
			// Since these options are not implemented anyway, the tabs are not necessary.
		}
	}
}
