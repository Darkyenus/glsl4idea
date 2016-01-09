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
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import glslplugin.GLSLSupportLoader;
import glslplugin.lang.elements.GLSLIdentifier;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.expressions.GLSLAssignmentExpression;
import glslplugin.lang.elements.expressions.GLSLExpression;
import glslplugin.lang.elements.expressions.GLSLFieldSelectionExpression;
import glslplugin.lang.elements.types.GLSLScalarType;
import glslplugin.lang.elements.types.GLSLStructType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLVectorType;
import glslplugin.util.VectorComponents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
            protected void addCompletions(@NotNull CompletionParameters completionParameters, ProcessingContext processingContext, @NotNull CompletionResultSet completionResultSet) {
                GLSLFieldSelectionExpression fieldSelection = (GLSLFieldSelectionExpression) completionParameters.getPosition().getParent().getParent();

                GLSLExpression leftHandExpression = fieldSelection.getLeftHandExpression();
                if (leftHandExpression == null) return;

                // Struct member completion
                GLSLType type = leftHandExpression.getType();
                if (type instanceof GLSLStructType) {
                    completeStructTypes(((GLSLStructType) type), completionResultSet);
                }

                // Vector completion that infers component count from the context
                if (type instanceof GLSLVectorType) {
                    // if it's a declaration (eg. `vec3 v3 = v4.`) the context type is inferred from declarator type (here vec3)
                    GLSLDeclarator declarator = PsiTreeUtil.getParentOfType(fieldSelection, GLSLDeclarator.class);
                    if (declarator != null) {
                        completeVectorTypes(((GLSLVectorType) type), declarator.getType(), completionResultSet);
                    }

                    // if it's an assignment (eg. `vec3 v3; v3 = v4.`) the context type is inferred from variable type (if known)
                    GLSLAssignmentExpression assignment = PsiTreeUtil.getParentOfType(fieldSelection, GLSLAssignmentExpression.class);
                    if (assignment != null) {
                        completeVectorTypes(((GLSLVectorType) type), assignment.getType(), completionResultSet);
                    }
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

    /**
     * Vector type completion implementation. Uses values from {@link VectorComponents} in order.
     *
     * @param type the vector type we're completing
     * @param contextNumComponents how many components does the context expect
     * @param completionResultSet
     */
    private void completeVectorTypes(GLSLVectorType type, int contextNumComponents, CompletionResultSet completionResultSet) {
        int componentCount = Math.min(contextNumComponents, type.getNumComponents());

        for (VectorComponents components : VectorComponents.values()) {
            completionResultSet.addElement(new GLSLLookupElement(components.getComponentRange(componentCount), type));
        }

        completionResultSet.stopHere();
    }

    private void completeVectorTypes(GLSLVectorType type, @Nullable GLSLType contextType, CompletionResultSet completionResultSet) {
        if (contextType instanceof GLSLScalarType) {
            completeVectorTypes(type, 1, completionResultSet);
        } else if (contextType instanceof GLSLVectorType) {
            completeVectorTypes(type, ((GLSLVectorType) contextType).getNumComponents(), completionResultSet);
        }
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
