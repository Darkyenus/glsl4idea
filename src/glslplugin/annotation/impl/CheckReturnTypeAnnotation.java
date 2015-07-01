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
import glslplugin.lang.elements.declarations.GLSLFunctionDefinition;
import glslplugin.lang.elements.statements.GLSLReturnStatement;
import glslplugin.lang.elements.statements.GLSLStatement;
import glslplugin.lang.elements.types.GLSLType;
import org.jetbrains.annotations.NotNull;

// First half of function return type checking.
public class CheckReturnTypeAnnotation extends Annotator<GLSLStatement> {

    public void annotate(GLSLStatement expr, AnnotationHolder holder) {
        if (expr instanceof GLSLReturnStatement) {
            GLSLFunctionDefinition function = expr.findParentByClass(GLSLFunctionDefinition.class);

            if (function != null) {
                GLSLType functionType = function.getType().getReturnType();
                GLSLType returnType = ((GLSLReturnStatement) expr).getReturnType();

                if (returnType.isValidType() && !returnType.isConvertibleTo(functionType)) {
                    holder.createErrorAnnotation(expr, "Incompatible types. Required: " + functionType.getTypename() + ", found: " + returnType.getTypename());
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
