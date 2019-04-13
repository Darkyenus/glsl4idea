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
import glslplugin.lang.elements.types.GLSLArrayType;
import org.jetbrains.annotations.Nullable;

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

    @Nullable
    private GLSLExpression getSizeExpression() {
        return findChildByClass(GLSLExpression.class);
    }

    /**
     * Size of dimension which this specifier denotes.
     * For dimension of yet unknown length, {@link glslplugin.lang.elements.types.GLSLArrayType#UNDEFINED_SIZE_DIMENSION} is returned.
     * For dimension of length known only at runtime, {@link glslplugin.lang.elements.types.GLSLArrayType#DYNAMIC_SIZE_DIMENSION} is returned.
     */
    public int getDimensionSize() {
        GLSLExpression sizeExpression = getSizeExpression();
        if (sizeExpression != null) {
            //Check if it is a constant value
            if (sizeExpression.isConstantValue()) {
                Object constantValue = sizeExpression.getConstantValue();
                if (constantValue instanceof Long) {
                    return ((Long) constantValue).intValue();
                }
            }
            //It is not a constant value, assume it is dynamic
            return GLSLArrayType.DYNAMIC_SIZE_DIMENSION;
        } else {
            return GLSLArrayType.UNDEFINED_SIZE_DIMENSION;
        }
    }

    @Override
    public String toString() {
        int dimensionSize = getDimensionSize();
        if (dimensionSize == GLSLArrayType.DYNAMIC_SIZE_DIMENSION) {
            return "Array Declarator [?]";
        } else if (dimensionSize == GLSLArrayType.UNDEFINED_SIZE_DIMENSION) {
            return "Array Declarator []";
        } else {
            return "Array Declarator [" + dimensionSize + "]";
        }
    }
}
