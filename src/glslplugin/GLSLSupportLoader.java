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

package glslplugin;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import com.intellij.openapi.fileTypes.LanguageFileType;
import glslplugin.lang.GLSLFileType;
import org.jetbrains.annotations.NotNull;

/**
 * Initiates GLSL support
 */
public class GLSLSupportLoader extends FileTypeFactory {
    public static final LanguageFileType GLSL = new GLSLFileType();

    private static final String FILETYPES = "glsl" + FileTypeConsumer.EXTENSION_DELIMITER
                                          + "frag" + FileTypeConsumer.EXTENSION_DELIMITER
                                          + "fsh"  + FileTypeConsumer.EXTENSION_DELIMITER
                                          + "vert" + FileTypeConsumer.EXTENSION_DELIMITER
                                          + "vsh"  + FileTypeConsumer.EXTENSION_DELIMITER
                                          + "tesc" + FileTypeConsumer.EXTENSION_DELIMITER
                                          + "tese" + FileTypeConsumer.EXTENSION_DELIMITER
                                          + "geom" + FileTypeConsumer.EXTENSION_DELIMITER
                                          + "comp" + FileTypeConsumer.EXTENSION_DELIMITER
                                          + "shader";

    public void createFileTypes(@NotNull FileTypeConsumer consumer) {
        consumer.consume(GLSL, FILETYPES);
    }
}
