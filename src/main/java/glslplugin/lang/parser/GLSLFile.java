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

package glslplugin.lang.parser;

import com.intellij.psi.FileViewProvider;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import glslplugin.GLSLSupportLoader;
import glslplugin.lang.elements.preprocessor.GLSLVersionDirective;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GLSLFile extends PsiFileBase {
    /**
     * The assumed GLSL version target if no #version directive exists.
     *
     * @see <a href="https://www.opengl.org/registry/doc/GLSLangSpec.4.40.pdf">https://www.opengl.org/registry/doc/GLSLangSpec.4.40.pdf</a> (page 17)
     */
    public static final int GLSL_DEFAULT_VERSION = 110;

    public GLSLFile(FileViewProvider fileViewProvider) {
        super(fileViewProvider, GLSLSupportLoader.GLSL.getLanguage());
    }

    /**
     * @return the GLSL version specified in #version directive or {@link GLSLFile#GLSL_DEFAULT_VERSION}
     */
    public int getGLSLVersion() {
        GLSLVersionDirective versionDirective = PsiTreeUtil.findChildOfType(this, GLSLVersionDirective.class);
        if (versionDirective == null) return GLSL_DEFAULT_VERSION;

        int versionLiteralNumber = versionDirective.getVersionLiteralNumber();
        if (versionLiteralNumber == -1) return GLSL_DEFAULT_VERSION;

        return versionLiteralNumber;
    }

    @NotNull
    public FileType getFileType() {
        return GLSLSupportLoader.GLSL;
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, @Nullable PsiElement lastParent, @NotNull PsiElement place) {
        if (lastParent == null) {
            return true;
        }
        PsiElement child = lastParent.getPrevSibling();
        while (child != null) {
            if (!child.processDeclarations(processor, state, lastParent, place)) return false;
            child = child.getPrevSibling();
        }
        return true;
    }

    @Override
    public String toString() {
        return getName();
    }
}
