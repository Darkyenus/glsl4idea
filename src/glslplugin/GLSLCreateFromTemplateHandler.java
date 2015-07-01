package glslplugin;

import com.intellij.ide.fileTemplates.DefaultCreateFromTemplateHandler;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
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
import glslplugin.lang.GLSLFileType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Handles creating new shader file templates.
 *
 * It can change the template text based on the given extension.
 *
 * @author Jan Polák
 */
public class GLSLCreateFromTemplateHandler extends DefaultCreateFromTemplateHandler {

    protected static FileTemplateManager getDefaultFileTemplateManager() {
        try {
            return FileTemplateManager.getDefaultInstance();
        } catch (NoSuchMethodError err) {
            // This is for compatibility with IDEA 13, which does not have getDefaultInstance().
            // While getInstance() still exists in IDEA 14, it will be probably removed soon,
            // so the reflection dance would be needed sooner or later anyway
            try {
                Method getInstanceMethod = FileTemplateManager.class.getDeclaredMethod("getInstance");
                Object result = getInstanceMethod.invoke(null);
                if (result instanceof FileTemplateManager) {
                    Logger.getLogger("GLSLCreateFromTemplateHandler").info("Successfully used reflection to obtain FileTemplateManager");
                    return (FileTemplateManager) result;
                }
            } catch (NoSuchMethodException ignored) {
            } catch (InvocationTargetException ignored) {
            } catch (IllegalAccessException ignored) {
            }
            Logger.getLogger("GLSLCreateFromTemplateHandler").warning("Failed to obtain FileTemplateManager");
            return null;
        }
    }

    public static final String DEFAULT_EXTENSION = "glsl";
    public static final Map<String, FileTemplate> TEMPLATES = new HashMap<String, FileTemplate>();

    private static void loadTemplates(FileTemplate[] templates){
        for(FileTemplate template:templates){
            if(template.isTemplateOfType(GLSLSupportLoader.GLSL) && GLSLFileType.EXTENSIONS.contains(template.getExtension())){
                System.out.println("Adding ."+template.getExtension()+":\n"+template.getText()+"\n\n");
                TEMPLATES.put(template.getExtension(), template);
            }
        }
    }

    static {
        FileTemplateManager manager = getDefaultFileTemplateManager();
        if(manager != null){
            loadTemplates(manager.getAllTemplates());
            loadTemplates(manager.getInternalTemplates());
        }
    }

    @Override
    public boolean handlesTemplate(FileTemplate template) {
        return template.isTemplateOfType(GLSLSupportLoader.GLSL);
    }

    //Copied and modified from parent class
    @NotNull
    @Override
    public PsiElement createFromTemplate(final Project project, final PsiDirectory directory, String fileName, FileTemplate template,
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
            } else {
                //Do template replacement
                FileTemplate alternateTemplate = TEMPLATES.get(extension.toLowerCase());
                if(alternateTemplate != null){
                    //There is a template defined for this
                    try {
                        templateText = alternateTemplate.getText(props);
                    } catch (IOException e) {
                        throw new IncorrectOperationException("Failed to load template file.", (Throwable)e);
                    }
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
