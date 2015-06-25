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
import glslplugin.lang.elements.expressions.GLSLCondition;
import glslplugin.lang.elements.statements.ConditionStatement;
import glslplugin.lang.elements.statements.GLSLStatement;
import glslplugin.lang.elements.statements.GLSLWhileStatement;
import glslplugin.lang.elements.types.GLSLPrimitiveType;
import glslplugin.lang.elements.types.GLSLType;
import org.jetbrains.annotations.NotNull;

public class ConditionCheckAnnotation extends Annotator<GLSLStatement> {
    public void annotate(GLSLStatement expr, AnnotationHolder holder) {
        if (expr instanceof ConditionStatement) {
            ConditionStatement conditionStatement = (ConditionStatement) expr;
            GLSLCondition condition = conditionStatement.getCondition();

            if (condition != null) {
                GLSLType conditionType = condition.getType();
                if (conditionType instanceof GLSLPrimitiveType) {
                    GLSLPrimitiveType pType = (GLSLPrimitiveType) conditionType;
                    if (!pType.equals(GLSLPrimitiveType.BOOL)) {
                        holder.createErrorAnnotation(condition, "Condition must be a boolean expression.");
                    }
                }
            }
        }
    }

    @NotNull
    @Override
    public Class<GLSLStatement> getElementType() {
        return GLSLStatement.class;
    }
}
