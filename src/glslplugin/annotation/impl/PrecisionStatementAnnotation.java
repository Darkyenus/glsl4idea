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

import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.tree.TokenSet;
import glslplugin.GLSLHighlighter;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.GLSLElementTypes;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.statements.GLSLPrecisionStatement;
import org.jetbrains.annotations.NotNull;

/**
 * Highlights whole precision statements (eg. precision mediump float;) as such.
 * "precision" keyword is highlighted at token level.
 *
 * @author Darkyen
 */
public class PrecisionStatementAnnotation extends Annotator<GLSLPrecisionStatement> {

    //PRECISION_KEYWORD is highlighted by default, so no need to do it again
    private static TokenSet HIGHLIGHTED_TOKENS = TokenSet.create(GLSLTokenTypes.PRECISION_QUALIFIER, GLSLTokenTypes.SEMICOLON, GLSLElementTypes.TYPE_SPECIFIER);

    @Override
    public void annotate(GLSLPrecisionStatement expr, AnnotationHolder holder) {
        //PSI does not for some reason hold all relevant Elements
        for(ASTNode child:expr.getNode().getChildren(HIGHLIGHTED_TOKENS)){
            //This circus is necessary, because node may contain things like comments and preprocessor directives.
            holder.createInfoAnnotation(child,null).setTextAttributes(GLSLHighlighter.GLSL_PRECISION_STATEMENT[0]);
        }
    }

    @NotNull
    @Override
    public Class<GLSLPrecisionStatement> getElementType() {
        return GLSLPrecisionStatement.class;
    }
}
