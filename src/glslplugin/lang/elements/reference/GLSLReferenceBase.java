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

package glslplugin.lang.elements.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import glslplugin.lang.elements.GLSLElement;
import org.jetbrains.annotations.NotNull;

/**
 * GLSLVariableReference is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 4, 2009
 *         Time: 1:29:50 AM
 */
public abstract class GLSLReferenceBase<SOURCE_TYPE extends GLSLElement, TARGET_TYPE extends GLSLElement> implements PsiReference {
    protected SOURCE_TYPE source;
    protected TARGET_TYPE target;

    public GLSLReferenceBase(SOURCE_TYPE source, TARGET_TYPE target) {
        assert source != null;
        assert target != null;
        this.source = source;
        this.target = target;
    }

    public SOURCE_TYPE getElement() {
        return source;
    }

    public TextRange getRangeInElement() {
        return new TextRange(0, source.getTextLength());
    }

    public TARGET_TYPE resolve() {
        return target;
    }

    @NotNull
    public String getCanonicalText() {
        return target.getText();
    }

    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        throw new IncorrectOperationException("Not supported!");
    }

    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        throw new IncorrectOperationException("Not supported!");
    }

    public boolean isReferenceTo(PsiElement element) {
        return false;
    }

    @NotNull
    public Object[] getVariants() {
        return new Object[]{target};
    }

    public boolean isSoft() {
        // TODO: Not quite sure about this one. Investigate!
        return false;
    }
}