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

package glslplugin.lang.elements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiCheckedRenameElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GLSLIdentifier extends GLSLElementImpl implements PsiCheckedRenameElement, PsiNameIdentifierOwner {

    public GLSLIdentifier(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        return findChildByType(GLSLTokenTypes.IDENTIFIER);
    }

    @NotNull
    public String getName() {
        return getText();
    }

    @Override
    public void checkSetName(String name) throws IncorrectOperationException {
        final PsiElement oldName = getNameIdentifier();
        if (oldName == null) throw new IncorrectOperationException("Unnamed identifier!");// This probably shouldn't happen.
        PsiElement newName = GLSLPsiElementFactory.createLeafElement(getProject(), name);
        if (newName.getNode().getElementType() != GLSLTokenTypes.IDENTIFIER)
            throw new IncorrectOperationException("Invalid identifier name!");
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        checkSetName(name);
        final PsiElement oldName = getNameIdentifier();
        assert oldName != null; // we've already checked this isn't null (and thrown if it is) in checkSetName
        PsiElement newName = GLSLPsiElementFactory.createLeafElement(getProject(), name);
        getNode().replaceChild(oldName.getNode(), newName.getNode());
        return newName;
    }

    @NotNull
    @Override
    public String toString() {
        return "Identifier: '" + getText() + "'";
    }
}
