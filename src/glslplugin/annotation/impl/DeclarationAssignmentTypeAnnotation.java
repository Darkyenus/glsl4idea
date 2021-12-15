/*
 * Copyright 2010 Jean-Paul Balabanian and Yngve Devik Hammersland
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
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.declarations.GLSLVariableDeclaration;
import glslplugin.lang.elements.expressions.GLSLExpression;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypeCompatibilityLevel;
import org.jetbrains.annotations.NotNull;

/**
 * Detects and annotates when currently declared variable is assigned invalid default value.
 *
 * Example: float foobaz = vec2(4.0);
 *
 * @see BinaryOperatorTypeAnnotation
 *
 * @author Darkyen
 */
public class DeclarationAssignmentTypeAnnotation extends Annotator<GLSLVariableDeclaration> {

    @Override
    public void annotate(GLSLVariableDeclaration expr, AnnotationHolder holder) {
        for(final GLSLDeclarator declarator:expr.getDeclarators()){
            final GLSLType variableType = declarator.getType();
            final GLSLExpression initializer = declarator.getInitializerExpression();

            if(variableType.isValidType() && initializer != null){
                final GLSLType assignedType = initializer.getType();
                if(!assignedType.isValidType())continue;
                if(GLSLTypeCompatibilityLevel.getCompatibilityLevel(assignedType, variableType) == GLSLTypeCompatibilityLevel.INCOMPATIBLE){
                    holder.newAnnotation(HighlightSeverity.ERROR, "Can't assign '"+assignedType.getTypename()+"' to '"+variableType.getTypename()+"'").range(initializer).create();
                }
            }
        }
    }

    @NotNull
    @Override
    public Class<GLSLVariableDeclaration> getElementType() {
        return GLSLVariableDeclaration.class;
    }
}
