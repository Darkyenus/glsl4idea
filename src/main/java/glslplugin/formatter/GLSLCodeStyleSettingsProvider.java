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
		return """
				#version 330 core

				// Some definitions
				#if LIGHT_TYPE == 1
				#define LIGHT_STRUCTNAME SpotLight
				#else
				#define LIGHT_STRUCTNAME PointLight
				#endif

				// Normal variables
				in vec2 fragTexCoords;
				out vec4 fragColor;

				uniform int activeLightCount;

				// Interface block
				in VertexData
				{
				    vec3 color;
				    vec2 texCoord;
				} inData[];

				layout(row_major) uniform MatrixBlock
				{
				    mat4 projection;
				    layout(column_major) mat4 modelview;
				} matrices[3];

				void main() {
				    func();
				}

				vec3 addVectors(vec3 a, vec3 b) {
				    return a + b;
				}

				void miscFunc(int t) {
				    if (x == y) {
				        int accum = 0;
				        for (int i = 0;i < t; i++) {
				            if (i == 2) {
				                continue;
				            }
				            accum += 0;
				        }
				    } else {
				        discard;
				    }
				}""";
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
