package glslplugin.actions;

import com.intellij.ide.actions.CreateFileFromTemplateAction;
import com.intellij.ide.actions.CreateFileFromTemplateDialog;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import glslplugin.GLSLSupportLoader;
import org.jetbrains.annotations.NotNull;

/**
 * @author Jan Polák
 */
public class GLSLCreateNewShaderFile extends CreateFileFromTemplateAction {

    public GLSLCreateNewShaderFile() {
        super("GLSL Shader", "Create new GLSL shader file", GLSLSupportLoader.GLSL.getIcon());
    }

    @Override
    protected void buildDialog(@NotNull Project project, @NotNull PsiDirectory directory, CreateFileFromTemplateDialog.Builder builder) {
        builder.setTitle("Create new GLSL shader file");
        builder.addKind("GLSL Shader", null, "GLSL Shader");
    }

    @Override
    protected String getActionName(PsiDirectory directory, @NotNull String newName, String templateName) {
        return "GLSL Shader";
    }
}
