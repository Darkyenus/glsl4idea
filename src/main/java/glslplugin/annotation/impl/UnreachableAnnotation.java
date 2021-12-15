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
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLElementTypes;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.declarations.GLSLFunctionDefinition;
import glslplugin.lang.elements.statements.GLSLLabelStatement;
import glslplugin.lang.elements.statements.GLSLStatement;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class UnreachableAnnotation extends Annotator<GLSLFunctionDefinition> {
    private final TextAttributesKey unreachableAttributes;

    public UnreachableAnnotation() {
        unreachableAttributes = TextAttributesKey.createTextAttributesKey("GLSL.UNREACHABLE", CodeInsightColors.NOT_USED_ELEMENT_ATTRIBUTES);
    }

    public void annotate(GLSLFunctionDefinition function, AnnotationHolder holder) {
        final Collection<GLSLStatement> statements = PsiTreeUtil.findChildrenOfType(function, GLSLStatement.class);
        for (GLSLStatement statement : statements) {
            GLSLStatement.TerminatorScope scope = statement.getTerminatorScope();
            if (scope == GLSLStatement.TerminatorScope.NONE) continue;

            final PsiElement parent = statement.getParent();
            if (parent == null || parent.getNode().getElementType() != GLSLElementTypes.COMPOUND_STATEMENT) {
                continue;
            }

            PsiElement element = statement.getNextSibling();
            while (element != null) {
                if (element instanceof GLSLElement && !GLSLTokenTypes.PREPROCESSOR_DIRECTIVES.contains(element.getNode().getElementType())) {
                    if (element instanceof GLSLLabelStatement) break;
                    PsiElement child = element.getFirstChild();

                    if (child == null) {
                        holder.newAnnotation(HighlightSeverity.WARNING, "Unreachable expression").range(element).textAttributes(unreachableAttributes).create();
                    } else {
                        do {
                            IElementType type = child.getNode().getElementType();
                            if(!GLSLTokenTypes.PREPROCESSOR_DIRECTIVES.contains(type) && type != TokenType.WHITE_SPACE){
                                if (child instanceof GLSLLabelStatement) break;
                                holder.newAnnotation(HighlightSeverity.WARNING, "Unreachable expression").range(child).textAttributes(unreachableAttributes).create();
                            }
                            child = child.getNextSibling();
                        } while(child != null);
                    }

                }
                element = element.getNextSibling();
            }
        }
    }

    @NotNull
    @Override
    public Class<GLSLFunctionDefinition> getElementType() {
        return GLSLFunctionDefinition.class;
    }
}
