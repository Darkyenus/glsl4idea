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
import com.intellij.lang.annotation.HighlightSeverity;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.expressions.GLSLFunctionCallExpression;
import glslplugin.lang.elements.types.*;
import org.jetbrains.annotations.NotNull;

/**
 * Creates an error annotation if trying to pass invalid amount of arguments to a constructor.
 */
public class ConstructorParamCountAnnotation extends Annotator<GLSLFunctionCallExpression> {

    @Override
    public void annotate(GLSLFunctionCallExpression expr, AnnotationHolder holder) {
        if (!expr.isConstructor()) return;
        final GLSLType constructorType = expr.getType();
        final GLSLType[] parameters = expr.getParameterTypes();

        if (!(constructorType instanceof GLSLArrayType)) {//We do not handle array constructors
            //Now it can be only scalar, vector, matrix or a struct

            final GLSLFunctionType[] constructors = constructorType.getConstructors();
            if(constructors.length == 0) return; //Huh, this one does not have any constructors, don't report anything then
            boolean found = false;
            for (GLSLFunctionType constructor : constructors) {
                if(constructor.getParameterCompatibilityLevel(parameters) != GLSLTypeCompatibilityLevel.INCOMPATIBLE){
                    found = true;
                    break;
                }
            }
            if(!found){
                StringBuilder sb = new StringBuilder();
                sb.append("Cannot resolve constructor '").append(constructorType.getTypename()).append('(');
                if(parameters.length != 0){
                    final String PARAMETER_SEPARATOR = ", ";
                    for (GLSLType parameter : parameters) {
                        sb.append(parameter.getTypename()).append(PARAMETER_SEPARATOR);
                    }
                    sb.setLength(sb.length() - PARAMETER_SEPARATOR.length());
                }
                sb.append(")'");

                final String baseMessage = sb.toString();

                sb.setLength(0);
                sb.append("<html><body>");
                sb.append(baseMessage);
                sb.append("<br/>");

                if(constructorType instanceof GLSLVectorType || constructorType instanceof GLSLMatrixType){
                    sb.append(GLSLVectorType.countVectorOrMatrixConstructorElements(parameters));
                    sb.append(" elements found<br/>");
                }

                sb.append("Try:<hr/><code>");

                for (GLSLFunctionType constructor : constructors) {
                    sb.append(constructor.getTypename()).append("<br/>");
                }

                sb.append("</code></body></html>");


                holder.createAnnotation(HighlightSeverity.ERROR, expr.getTextRange(), baseMessage, sb.toString());
            }
        }
    }

    @NotNull
    @Override
    public Class<GLSLFunctionCallExpression> getElementType() {
        return GLSLFunctionCallExpression.class;
    }
}
