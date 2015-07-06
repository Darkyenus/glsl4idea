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
import glslplugin.GLSLSupportLoader;
import org.jetbrains.annotations.NotNull;

public class GLSLFile extends PsiFileBase {
    public GLSLFile(FileViewProvider fileViewProvider) {
        super(fileViewProvider, GLSLSupportLoader.GLSL.getLanguage());
    }

    @NotNull
    public FileType getFileType() {
        return GLSLSupportLoader.GLSL;
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
        PsiElement child = lastParent.getPrevSibling();
        while (child != null) {
            if (!child.processDeclarations(processor, state, lastParent, place)) return false;
            child = child.getPrevSibling();
        }
        return true;
    }
}
