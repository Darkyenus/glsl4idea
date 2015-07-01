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
import glslplugin.GLSLSupportLoader;
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

    /**
     * Adds a template to TEMPLATES. Template is loaded from resources: "templates/default.[extension]".
     * If the file does not exist, default template is assigned.
     * @param extension in lowercase
     */
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
        //All recognized extensions
        addTemplate("glsl");
        addTemplate("frag");
        addTemplate("vert");
        addTemplate("tesc");
        addTemplate("tese");
        addTemplate("geom");
        addTemplate("comp");
    }

    @Override
    public boolean handlesTemplate(FileTemplate template) {
        return template.isTemplateOfType(GLSLSupportLoader.GLSL);
    }

    //Copied and modified from parent class
    @NotNull
    @Override
    public PsiElement createFromTemplate(final Project project, final PsiDirectory directory, String fileName, final FileTemplate template,
                                         String templateText,
                                         @NotNull final Map<String, Object> props) throws IncorrectOperationException {
        //Make sure it has some extension
        int extensionDot = fileName.lastIndexOf('.');
        String extension;
        if(extensionDot == -1){
            extension = template.getExtension();
            fileName = fileName+"."+extension;
        }else{
            extension = fileName.substring(extensionDot+1);
            if(extension.isEmpty()){
                //Filename ends with a dot
                extension = template.getExtension();
                fileName = fileName + extension;
            }else if(template.isDefault()){
                //Do text replacement only if the user has not changed the template
                String alternateTemplateText = TEMPLATES.get(extension.toLowerCase());
                if(alternateTemplateText != null){
                    templateText = alternateTemplateText;
                }
            }
        }
        //Make sure that the extension is valid
        if(!TEMPLATES.containsKey(extension.toLowerCase())){
            //Extension is not recognized, add recognized default one
            fileName = fileName + "." + DEFAULT_EXTENSION;
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
