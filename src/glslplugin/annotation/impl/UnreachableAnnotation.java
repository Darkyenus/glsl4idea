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

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLElementTypes;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.statements.*;
import org.jetbrains.annotations.NotNull;

public class UnreachableAnnotation extends Annotator<GLSLStatement> {
    private TextAttributesKey unreachableAttributes;

    public UnreachableAnnotation() {
        unreachableAttributes = TextAttributesKey.createTextAttributesKey("GLSL.UNREACHABLE", CodeInsightColors.NOT_USED_ELEMENT_ATTRIBUTES);
    }

    public void annotate(GLSLStatement expr, AnnotationHolder holder) {
        GLSLStatement.TerminatorScope scope = expr.getTerminatorScope();
        if (scope == GLSLStatement.TerminatorScope.NONE) return;

        if (expr.getParent() == null
                || expr.getParent().getNode().getElementType() != GLSLElementTypes.COMPOUND_STATEMENT) {
            return;
        }

        PsiElement element = expr.getNextSibling();
        while (element != null) {
            if (element instanceof GLSLElement && !GLSLTokenTypes.PREPROCESSOR_DIRECTIVES.contains(element.getNode().getElementType())) {
                if (element instanceof GLSLLabelStatement) return;
                PsiElement child = element.getFirstChild();

                if(child == null){
                    Annotation annotation = holder.createWarningAnnotation(element, "Unreachable expression");
                    annotation.setTextAttributes(unreachableAttributes);
                }else{
                    do {
                        IElementType type = child.getNode().getElementType();
                        if(!GLSLTokenTypes.PREPROCESSOR_DIRECTIVES.contains(type) && type != TokenType.WHITE_SPACE){
                            if (child instanceof GLSLLabelStatement) return;
                            Annotation annotation = holder.createWarningAnnotation(child, "Unreachable expression");
                            annotation.setTextAttributes(unreachableAttributes);
                        }
                        child = child.getNextSibling();
                    }while(child != null);
                }

            }
            element = element.getNextSibling();
        }
    }

    @NotNull
    @Override
    public Class<GLSLStatement> getElementType() {
        return GLSLStatement.class;
    }
}
