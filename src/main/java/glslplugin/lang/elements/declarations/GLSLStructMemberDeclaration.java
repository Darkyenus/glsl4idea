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
 * Element for structure members.
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 2, 2009
 *         Time: 3:52:15 PM
 */
public class GLSLStructMemberDeclaration extends GLSLElementImpl implements GLSLQualifiedDeclaration {
    public GLSLStructMemberDeclaration(@NotNull ASTNode astNode) {
        super(astNode);
    }

    public GLSLDeclarator @NotNull[] getDeclarators() {
        final GLSLDeclaratorList list = findChildByClass(GLSLDeclaratorList.class);
        if (list == null) return GLSLDeclarator.NO_DECLARATORS;
        else return list.getDeclarators();
    }

    @Override
    public String toString() {
        return "Struct Declaration: " + GLSLDeclarator.toString(getDeclarators());
    }

}
