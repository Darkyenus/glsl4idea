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
import glslplugin.lang.elements.GLSLElementImpl;
import org.jetbrains.annotations.NotNull;

/**
 * NewDeclarationBase is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 2, 2009
 *         Time: 10:33:30 AM
 */
public class GLSLDeclarationImpl extends GLSLElementImpl implements GLSLDeclaration {
    public GLSLDeclarationImpl(@NotNull ASTNode astNode) {
        super(astNode);
    }

    public GLSLQualifier[] getQualifiers() {
        return getQualifierList().getQualifiers();
    }

    /**
     * Same as {@link GLSLDeclarationImpl#getTypeSpecifierNode()} but may return null.
     * (getTypeSpecifierNode will crash instead)
     * @return type specifier or null
     */
    public GLSLTypeSpecifier findTypeSpecifierNode(){
        return findChildByClass(GLSLTypeSpecifier.class);
    }

    /**
     * Returns the type specifier Psi element for this declaration.
     * <p/>
     * <b>WARNING!</b>
     * <p/>
     * This is not a complete type of any variable as a declarator may contain an array specifier.
     * For the complete type use {@link glslplugin.lang.elements.declarations.GLSLDeclarator#getType()} instead.
     *
     * @return the type specifier.
     */
    public GLSLTypeSpecifier getTypeSpecifierNode() {
        final GLSLTypeSpecifier typeSpecifier = findTypeSpecifierNode();
        assert typeSpecifier != null;//TODO This assertion failed
        return typeSpecifier;
    }

    @NotNull
    public GLSLQualifierList getQualifierList() {
        GLSLQualifierList list = findChildByClass(GLSLQualifierList.class);
        assert list != null;
        return list;
    }

    public GLSLDeclarator[] getDeclarators() {
        GLSLDeclaratorList list = findChildByClass(GLSLDeclaratorList.class);
        assert list != null;
        return list.getDeclarators();
    }

    protected String getDeclaratorsString() {
        StringBuilder b = new StringBuilder();
        boolean first = true;
        for (GLSLDeclarator declarator : getDeclarators()) {
            if (!first) {
                b.append(", ");
            }
            b.append(declarator.getIdentifier().getIdentifierName());
            first = false;
        }
        return b.toString();
    }
}
