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
import glslplugin.lang.elements.expressions.GLSLExpression;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

/**
 * GLSLArrayDeclarator is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 29, 2009
 *         Time: 2:11:52 PM
 */
public class GLSLArraySpecifier extends GLSLElementImpl {
    public GLSLArraySpecifier(ASTNode node) {
        super(node);
    }

    public boolean hasSizeExpression() {
        return findChildByClass(GLSLExpression.class) != null;
    }

    @Nullable
    public GLSLExpression getSizeExpression() {
        GLSLExpression expr = findChildByClass(GLSLExpression.class);
        if (expr != null) {
            return expr;
        } else {
            Logger.getLogger("GLSLArraySpecifier").warning("Check for array size expression before asking for it!");
            return null;
        }
    }

    @Override
    public String toString() {
        return "Array Declarator";
    }
}
