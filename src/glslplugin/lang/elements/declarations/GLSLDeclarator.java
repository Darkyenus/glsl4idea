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
import glslplugin.lang.elements.GLSLIdentifier;
import glslplugin.lang.elements.expressions.GLSLExpression;
import org.jetbrains.annotations.NotNull;

/**
 * GLSLDeclarator represents a local or global variable declaration.
 * It may contain one or more declarators.
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 29, 2009
 *         Time: 7:29:46 PM
 */
public class GLSLDeclarator extends GLSLDeclaratorBase {
    public static final GLSLDeclarator[] NO_DECLARATORS = new GLSLDeclarator[0];

    public GLSLDeclarator(@NotNull ASTNode astNode) {
        super(astNode);
    }

    public boolean hasInitializer() {
        return findChildByClass(GLSLInitializer.class) != null;
    }

    public boolean hasIdentifier() {
        return findChildByClass(GLSLIdentifier.class) != null;
    }

    public GLSLExpression getInitializerExpression() {
        final GLSLInitializer init = findChildByClass(GLSLInitializer.class);
        if (init != null) {
            return init.getInitializerExpression();
        } else {
            throw new RuntimeException("Check for initializer before asking for it!");
        }
    }

    public GLSLArraySpecifier getArraySpecifier() {
        return findChildByClass(GLSLArraySpecifier.class);
    }
}
