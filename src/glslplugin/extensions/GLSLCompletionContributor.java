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

package glslplugin.extensions;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import glslplugin.GLSLSupportLoader;
import glslplugin.lang.elements.GLSLIdentifier;
import glslplugin.lang.elements.expressions.GLSLExpression;
import glslplugin.lang.elements.expressions.GLSLFieldSelectionExpression;
import glslplugin.lang.elements.types.GLSLStructType;
import glslplugin.lang.elements.types.GLSLType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * Provides more advanced contextual autocompletion
 *
 * @author Wyozi
 */
public class GLSLCompletionContributor extends DefaultCompletionContributor {
    private static final ElementPattern<PsiElement> FIELD_SELECTION =
            psiElement().withParent(psiElement(GLSLIdentifier.class).withParent(GLSLFieldSelectionExpression.class));

    public GLSLCompletionContributor() {
        // Add field selection completion
        extend(CompletionType.BASIC, FIELD_SELECTION, new CompletionProvider<CompletionParameters>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters completionParameters, @NotNull ProcessingContext processingContext, @NotNull CompletionResultSet completionResultSet) {
                GLSLFieldSelectionExpression fieldSelection = (GLSLFieldSelectionExpression) completionParameters.getPosition().getParent().getParent();

                GLSLExpression leftHandExpression = fieldSelection.getLeftHandExpression();
                if (leftHandExpression == null) return;

                // Only complete struct types for now
                GLSLType type = leftHandExpression.getType();
                if (type instanceof GLSLStructType) {
                    completeStructTypes(((GLSLStructType) type), completionResultSet);
                }
            }
        });
    }

    private void completeStructTypes(GLSLStructType type, CompletionResultSet completionResultSet) {
        for (Map.Entry<String, GLSLType> entry : type.getMembers().entrySet()) {
            completionResultSet.addElement(new GLSLLookupElement(entry.getKey(), entry.getValue()));
        }
        completionResultSet.stopHere();
    }

    public static class GLSLLookupElement extends LookupElement {
        private final String str;
        private GLSLType type;

        public GLSLLookupElement(String str, GLSLType type) {
            this.str = str;
            this.type = type;
        }

        @Override
        public void renderElement(LookupElementPresentation presentation) {
            super.renderElement(presentation);
            presentation.setIcon(GLSLSupportLoader.GLSL.getIcon());

            if (type != null) {
                presentation.setTypeText(this.type.getTypename());
            }
        }

        @NotNull
        @Override
        public String getLookupString() {
            return this.str;
        }
    }
}
