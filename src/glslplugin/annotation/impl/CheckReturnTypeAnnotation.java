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
import com.intellij.lang.annotation.Annotation;
import com.intellij.psi.PsiElement;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.statements.*;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.declarations.GLSLFunctionDefinitionImpl;
import glslplugin.lang.elements.declarations.GLSLFunctionDefinition;

import java.awt.*;

// First half of function return type checking.
// todo: Must have a separate annotation to check if a function with a return type has a return statement.
public class CheckReturnTypeAnnotation implements Annotator<GLSLStatement> {

    public void annotate(GLSLStatement expr, AnnotationHolder holder) {
        if (expr instanceof GLSLReturnStatement) {
            GLSLFunctionDefinition function = expr.findParentByClass(GLSLFunctionDefinition.class);

            if(function != null) {
                GLSLType functionType = function.getTypeSpecifierNode().getType();
                GLSLType returnType = ((GLSLReturnStatement) expr).getReturnType();

                if(!returnType.isValidType() || !functionType.isConvertibleTo(returnType)) {
                    holder.createErrorAnnotation(expr, "Incompatible types. Required: " + functionType.getTypename() + ", found: " + returnType.getTypename());
                }
            }
        }
    }
}