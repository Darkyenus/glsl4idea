package glslplugin.components;

import com.intellij.ide.fileTemplates.DefaultCreateFromTemplateHandler;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.ResourceUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles creating new shader file templates.
 *
 * It can change the template text based on the given extension.
 *
 * @author Jan Polák
 */
public class GLSLCreateFromTemplateHandler extends DefaultCreateFromTemplateHandler {

    public static final String DEFAULT_EXTENSION = "glsl";
    private static final String DEFAULT_TEMPLATE = "#version 120\n\nvoid main() {\n\n}";
    public static final Map<String, String> TEMPLATES = new HashMap<String, String>();

    private static void addTemplate(String extension){
        URL templateURL = ResourceUtil.getResource(GLSLCreateFromTemplateHandler.class, "templates", "default." + extension);
        if(templateURL == null)TEMPLATES.put(extension, DEFAULT_TEMPLATE);
        else {
            try {
                TEMPLATES.put(extension, ResourceUtil.loadText(templateURL));
            } catch (IOException e) {
                Logger.getLogger("GLSLCreateFromTemplateHandler").log(Level.WARNING, "Failed to load template.", e);
                TEMPLATES.put(extension, DEFAULT_TEMPLATE);
            }
        }
    }

    static {
        addTemplate("glsl");
        addTemplate("frag");
        addTemplate("vert");
    }

    @Override
    public boolean handlesTemplate(FileTemplate template) {
        return GLSLTemplatesLoader.addedTemplates.contains(template) || GLSLTemplatesLoader.TEMPLATE_NAME.equals(template.getName());
    }

    //Copied and modified from parent class
    @NotNull
    @Override
    public PsiElement createFromTemplate(final Project project, final PsiDirectory directory, String fileName, final FileTemplate template,
                                         String templateText,
                                         @NotNull final Map<String, Object> props) throws IncorrectOperationException {
        int extensionDot = fileName.lastIndexOf('.');
        if(extensionDot == -1){
            fileName = fileName+"."+DEFAULT_EXTENSION;
        }else{
            String extension = fileName.substring(extensionDot+1).toLowerCase();
            if(extension.isEmpty()){
                fileName = fileName + DEFAULT_EXTENSION;
            }else if(template.isDefault()){
                //Do text replacement only if the user has not changed the template
                String alternateTemplateText = TEMPLATES.get(extension);
                if(alternateTemplateText != null){
                    templateText = alternateTemplateText;
                }
            }
        }

        if (FileTypeManager.getInstance().isFileIgnored(fileName)) {
            throw new IncorrectOperationException("This filename is ignored (Settings | File Types | Ignore files and folders)");
        }

        directory.checkCreateFile(fileName);
        FileType type = FileTypeRegistry.getInstance().getFileTypeByFileName(fileName);
        PsiFile file = PsiFileFactory.getInstance(project).createFileFromText(fileName, type, templateText);

        if (template.isReformatCode()) {
            CodeStyleManager.getInstance(project).reformat(file);
        }

        file = (PsiFile)directory.add(file);
        return file;
    }
}
