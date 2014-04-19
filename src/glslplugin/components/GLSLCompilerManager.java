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

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.compiler.Compiler;
import com.intellij.openapi.compiler.CompilerManager;
import org.jetbrains.annotations.NotNull;
import glslplugin.GLSLSupportLoader;

public class GLSLCompilerManager implements ProjectComponent {
    private final Compiler compiler;
    private Project project;

    public GLSLCompilerManager(Project project) {

        this.project = project;
        compiler = new GLSLCompiler();
    }

    public void initComponent() {
        // TODO: insert component initialization logic here
    }

    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @NotNull
    public String getComponentName() {
        return "GLSLCompilerManager";
    }

    public void projectOpened() {
        CompilerManager.getInstance(project).addCompiler(compiler);
        CompilerManager.getInstance(project).addCompilableFileType(GLSLSupportLoader.GLSL);
    }

    public void projectClosed() {
        CompilerManager.getInstance(project).removeCompiler(compiler);
        CompilerManager.getInstance(project).removeCompilableFileType(GLSLSupportLoader.GLSL);
    }
}
