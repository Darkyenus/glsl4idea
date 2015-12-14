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

package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.expressions.GLSLExpression;
import glslplugin.lang.elements.expressions.GLSLFunctionCallExpression;
import glslplugin.lang.elements.types.GLSLScalarType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLVectorType;
import org.jetbrains.annotations.NotNull;

/**
 * Creates an error annotation if trying to pass invalid amount of arguments to a vector constructor.
 *
 * @author wyozi
 */
public class VectorConstructorParamCountAnnotation extends Annotator<GLSLFunctionCallExpression> {
    private boolean isVectorConstructor(GLSLFunctionCallExpression funcCall) {
        if (!funcCall.isConstructor())
            return false;

        GLSLType functionType = funcCall.getType();
        return functionType instanceof GLSLVectorType;
    }

    @Override
    public void annotate(GLSLFunctionCallExpression expr, AnnotationHolder holder) {
        if (isVectorConstructor(expr)) {
            final int neededComponents = ((GLSLVectorType) expr.getType()).getNumComponents();

            int numComponents = 0;
            for (GLSLType type : expr.getParameterTypes()) {
                if (type instanceof GLSLScalarType) {
                    numComponents += 1;
                } else if (type instanceof GLSLVectorType) {
                    numComponents += ((GLSLVectorType) type).getNumComponents();
                } else {
                    return; // we can't be sure if other types contribute to component count -> skip check
                }
            }

            // Single value constructors are always valid
            boolean isSingleValueConstructor = numComponents == 1;

            if (!isSingleValueConstructor && numComponents != neededComponents) {
                holder.createErrorAnnotation(expr, "Invalid amount of arguments for a vector constructor. (" + neededComponents + " needed. " + numComponents + " given)");
            }
        }
    }

    @NotNull
    @Override
    public Class<GLSLFunctionCallExpression> getElementType() {
        return GLSLFunctionCallExpression.class;
    }
}
