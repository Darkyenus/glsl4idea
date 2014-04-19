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
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;

public class GLSLIdentifier extends GLSLElementImpl {

    public GLSLIdentifier(@NotNull ASTNode astNode) {
        super(astNode);
    }

    public String getIdentifierName() {
        return getText();
    }

    public String toString() {
        return "Identifier: '" + getText() + "'";
    }

    private GLSLReferenceElement getRealReference() {
        PsiElement parent = getParent();
        if (parent instanceof GLSLReferenceElement) {
            return (GLSLReferenceElement) parent;
        } else {
            return null;
        }
    }

    @Override
    public PsiReference getReference() {
        GLSLReferenceElement theRealReference = getRealReference();
        if (theRealReference != null) {
            return theRealReference.getReferenceProxy();
        } else {
            return null;
        }
    }
    /*
    @NotNull
    @Override
    public PsiReference[] getReferences() {
        GLSLReferenceElement theRealReference = getRealReference();
        if(theRealReference!=null) {
            return theRealReference.getReferencesProxy();
        } else {
            return PsiReference.EMPTY_ARRAY;
        }
    }
    */
}
