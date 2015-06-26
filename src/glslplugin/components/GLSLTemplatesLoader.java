/*
 *     Copyright 2010 Jean-Paul Balabanian and Yngve Devik Hammersland
 *
 *     This file is part of glsl4idea.
 *
 *     Glsl4idea is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as
 *     published by the Free Software Foundation, either version 3 of
 *     the License, or (at your option) any later version.
 *
 *     Glsl4idea is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with glsl4idea.  If not, see <http://www.gnu.org/licenses/>.
 */

package glslplugin.components;

import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class GLSLTemplatesLoader implements ApplicationComponent {
    private final static String TEMPLATE_TEXT = "#version 120\n\nvoid main() {\n\n}";

    public GLSLTemplatesLoader() {
    }

    @NotNull
    public String getComponentName() {
        return "GLSL Template Loader";
    }

    private FileTemplateManager getDefaultFileTemplateManager() {
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
                    Logger.getLogger("GLSLTemplatesLoader").info("Successfully used reflection to obtain FileTemplateManager");
                    return (FileTemplateManager) result;
                }
            } catch (NoSuchMethodException ignored) {
            } catch (InvocationTargetException ignored) {
            } catch (IllegalAccessException ignored) {
            }
            Logger.getLogger("GLSLTemplatesLoader").warning("Failed to obtain FileTemplateManager");
            return null;
        }
    }

    public void initComponent() {
        FileTemplateManager fileTemplateManager = getDefaultFileTemplateManager();
        if(fileTemplateManager == null)return;

        if (fileTemplateManager.getTemplate("GLSL Shader") == null) {
            final FileTemplate template = fileTemplateManager.addTemplate("GLSL Shader", "glsl");

            template.setText(TEMPLATE_TEXT);
        }
    }

    public void disposeComponent() {
        FileTemplateManager fileTemplateManager = getDefaultFileTemplateManager();
        if(fileTemplateManager == null)return;

        FileTemplate template = fileTemplateManager.getTemplate("GLSL Shader");
        if (template != null) {
            fileTemplateManager.removeTemplate(template);
        }
    }
}
