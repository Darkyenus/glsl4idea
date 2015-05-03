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
import com.intellij.psi.PsiElement;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.declarations.GLSLFunctionDefinition;
import glslplugin.lang.elements.statements.GLSLCompoundStatement;
import glslplugin.lang.elements.statements.GLSLStatement;
import glslplugin.lang.elements.types.GLSLPrimitiveType;
import org.jetbrains.annotations.NotNull;

/**
 * Detects and annotates when non-void function lacks a return statement.
 *
 * @author Darkyen
 */
public class MissingReturnAnnotation extends Annotator<GLSLFunctionDefinition> {
    @Override
    public void annotate(GLSLFunctionDefinition expr, AnnotationHolder holder) {
        if (expr.getType().getBaseType() == GLSLPrimitiveType.VOID) return;

        final GLSLCompoundStatement body = expr.getBody();

        if (body.getTerminatorScope() == GLSLStatement.TerminatorScope.NONE) {
            PsiElement annotationPlace = body;

            /*
            Can't be sure that last child is },
            because parser might have bundled some comments or preprocessor statements into GLSLCompoundStatement.

            Also don't use getChildren() because it doesn't return all children for some reason and is slower anyway.
             */
            PsiElement e = body.getLastChild();
            while(e != null){
                if(e.getNode().getElementType() == GLSLTokenTypes.RIGHT_BRACE){
                    annotationPlace = e;
                    break;
                }else{
                    e = e.getPrevSibling();
                }
            }
            holder.createErrorAnnotation(annotationPlace, "Missing return statement");
        }
    }

    @NotNull
    @Override
    public Class<GLSLFunctionDefinition> getElementType() {
        return GLSLFunctionDefinition.class;
    }
}
