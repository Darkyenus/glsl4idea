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

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiElement;
import glslplugin.GLSLHighlighter;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.preprocessor.GLSLElementDropIn;
import glslplugin.lang.parser.GLSLRedefinedTokenType;
import org.jetbrains.annotations.NotNull;

/**
 * Highlights tokens which will be replaced at compile time.
 * Also shows text they will be replaced as.
 *
 * @author Darkyen
 */
public class RedefinedTokenAnnotation extends Annotator<GLSLElementDropIn> {

    @Override
    public void annotate(GLSLElementDropIn expr, AnnotationHolder holder) {
        PsiElement redefinedToken = expr.getNextSibling();
        if(redefinedToken == null || !(redefinedToken.getNode().getElementType() instanceof GLSLRedefinedTokenType)) {
            return; //Something is broken here
        }

        String message = expr.getOriginalText();

        final Annotation annotation = holder.createInfoAnnotation(redefinedToken, message);
        annotation.setTextAttributes(GLSLHighlighter.GLSL_REDEFINED_TOKEN[0]);
    }

    @NotNull
    @Override
    public Class<GLSLElementDropIn> getElementType() {
        return GLSLElementDropIn.class;
    }
}
