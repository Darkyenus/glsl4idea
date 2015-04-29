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
import glslplugin.lang.elements.expressions.GLSLAssignmentExpression;
import glslplugin.lang.elements.expressions.GLSLExpression;

/**
 * LValueAnnotation checks for l-values at the left hand side of assignment expressions.
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 30, 2009
 *         Time: 10:46:40 AM
 */
public class LValueAnnotator extends Annotator<GLSLAssignmentExpression> {

    public void annotate(GLSLAssignmentExpression expr, AnnotationHolder holder) {
        GLSLExpression left = expr.getLeftOperand();
        if (!left.isLValue()) {
            holder.createErrorAnnotation(left, "Left operand of assignment expression is not L-Value.");
        }
    }

    @Override
    public Class<GLSLAssignmentExpression> getElementType() {
        return GLSLAssignmentExpression.class;
    }
}
