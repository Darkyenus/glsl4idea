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

package glslplugin.lang.elements.declarations;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.GLSLIdentifier;
import glslplugin.lang.elements.GLSLTypedElement;
import glslplugin.lang.elements.types.GLSLStructType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * GLSLDeclarator is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 27, 2009
 *         Time: 10:31:13 AM
 */
public class GLSLTypeDefinition extends GLSLElementImpl implements GLSLTypedElement {
    // Cache this one to enable equals comparison by ==
    //  this is required to be able to compare types of variables of anonymous types.
    // struct {int x;} x, y; <- how to compare types of x and y?
    private GLSLStructType type;

    public GLSLTypeDefinition(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    private String getTypeNameInternal() {
        final PsiElement[] children = getChildren();
        if (children.length > 1) {
            PsiElement id = children[0];
            if (id instanceof GLSLIdentifier) {
                return ((GLSLIdentifier) id).getIdentifierName();
            }
        }
        return null;
    }

    public boolean isNamed() {
        return getTypeNameInternal() != null;
    }

    @NotNull
    public String getTypeName() {
        String name = getTypeNameInternal();
        if (name != null) {
            return name;
        } else {
            return "(anonymous structure)";
        }
    }

    // TODO: Add getMemberDeclarations, findMember(String), etc...

    @Nullable
    public GLSLDeclarationList getDeclarationList() {
        return findChildByClass(GLSLDeclarationList.class);
    }

    @NotNull
    public GLSLDeclaration[] getDeclarations() {
        GLSLDeclarationList declarationList = getDeclarationList();
        if(declarationList == null){
            return GLSLDeclaration.NO_DECLARATIONS;
        }else{
            return declarationList.getDeclarations();
        }
    }

    @NotNull
    public GLSLDeclarator[] getDeclarators() {
        List<GLSLDeclarator> declarators = new ArrayList<GLSLDeclarator>();
        for (GLSLDeclaration declaration : getDeclarations()) {
            Collections.addAll(declarators, declaration.getDeclarators());
        }
        return declarators.toArray(new GLSLDeclarator[declarators.size()]);
    }

    @Override
    public String toString() {
        return "Struct Type: " + getTypeName();
    }

    @NotNull
    public GLSLStructType getType() {
        if (type == null) {
            type = new GLSLStructType(this);
        }
        return type;
    }

    @Nullable
    public GLSLDeclarator getDeclarator(@NotNull String name) {
        for (GLSLDeclarator declarator : getDeclarators()) {
            if (name.equals(declarator.getIdentifierName())) {
                return declarator;
            }
        }
        return null;
    }
}
