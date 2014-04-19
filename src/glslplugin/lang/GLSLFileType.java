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

package glslplugin.lang;

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import glslplugin.lang.GLSLLanguage;

/**
 * Language file type
 */
public class GLSLFileType extends LanguageFileType {

    public GLSLFileType() {
        super(new GLSLLanguage());
    }

    @NotNull
    @NonNls
    public String getName() {
        return "GLSL shader file";
    }

    @NotNull
    public String getDescription() {
        return "OpenGL Shading Language file";
    }

    @NotNull
    @NonNls
    public String getDefaultExtension() {
        return "glsl";
    }

    @Nullable
    public Icon getIcon() {
        return IconLoader.getIcon("/icons/GLSLmini.png");
    }
}
