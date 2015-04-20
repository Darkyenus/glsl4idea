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
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLElementTypes;
import glslplugin.lang.elements.statements.*;

public class UnreachableAnnotation implements Annotator<GLSLStatement> {
    private TextAttributesKey strikeThrough;

    public UnreachableAnnotation() {
        strikeThrough = TextAttributesKey.createTextAttributesKey("GLSL.UNREACHABLE", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    }

    public void annotate(GLSLStatement expr, AnnotationHolder holder) {
        GLSLStatement.TerminatorScope scope = expr.getTerminatorScope();
        if (scope == GLSLStatement.TerminatorScope.NONE) return;

        if (scope == GLSLStatement.TerminatorScope.LOOP) {
            //noinspection unchecked
            GLSLElement parent = expr.findParentByClasses(GLSLDoStatement.class, GLSLForStatement.class, GLSLWhileStatement.class);
            if(parent == null) {
                holder.createErrorAnnotation(expr, "Must be in a loop!");
                return;
            }
        }

        if (expr.getParent() == null
                || expr.getParent().getNode().getElementType() != GLSLElementTypes.COMPOUND_STATEMENT) {
            return;
        }

        PsiElement element = expr.getNextSibling();
        while (element != null) {
            if (element instanceof GLSLElement) {
                Annotation annotation = holder.createWarningAnnotation(element, "Unreachable expression");
                annotation.setTextAttributes(strikeThrough);
            }
            element = element.getNextSibling();
        }
    }
}
