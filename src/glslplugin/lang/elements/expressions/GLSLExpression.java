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

package glslplugin.lang.elements.expressions;

import com.intellij.lang.ASTNode;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.GLSLTypedElement;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * GLSLExpression is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 28, 2009
 *         Time: 2:30:00 PM
 */
public abstract class GLSLExpression extends GLSLElementImpl implements GLSLTypedElement {
    public GLSLExpression(@NotNull ASTNode astNode) {
        super(astNode);
    }

    public boolean isLValue() {
        return false;
    }

    /**
     * Checks whether the value of this expression is a known compile-time constant
     */
    public boolean isConstantValue(){
        return false;
    }

    /**
     * Queries for the constant value.
     * Call is valid only if {@link GLSLExpression#isConstantValue()} returns true.
     * Otherwise the result is undefined.
     *
     * The actual returned type depends on the type of the expression,
     * and should map as closely as possible to the most similar Java type (where it makes sense).
     *
     * For example, numeric values default to their Java primitive counterparts (boxed).
     */
    @Nullable
    public Object getConstantValue(){
        return null;
    }

    @NotNull
    public GLSLType getType() {
        return GLSLTypes.UNKNOWN_TYPE;
    }
}
