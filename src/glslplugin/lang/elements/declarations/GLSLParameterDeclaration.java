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
import org.jetbrains.annotations.NotNull;

/**
 * NewParameterDeclaration is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 2, 2009
 *         Time: 2:04:56 PM
 */
public class GLSLParameterDeclaration extends GLSLSingleDeclarationImpl {
    public static final GLSLParameterDeclaration[] NO_PARAMETER_DECLARATIONS = new GLSLParameterDeclaration[0];

    public GLSLParameterDeclaration(@NotNull ASTNode astNode) {
        super(astNode);
    }

    boolean hasDeclarator() {
        return findChildByClass(GLSLDeclarator.class) != null;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder("Parameter Declaration: ");
        b.append(getTypeSpecifierNodeTypeName());
        if (hasDeclarator()) {
            b.append(getDeclaredName());
        }
        return b.toString();
    }
}
