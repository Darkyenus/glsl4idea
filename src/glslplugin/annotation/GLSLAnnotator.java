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

package glslplugin.annotation;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiElement;
import glslplugin.annotation.impl.*;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.expressions.GLSLAssignmentExpression;
import glslplugin.lang.elements.expressions.GLSLBinaryOperatorExpression;
import glslplugin.lang.elements.expressions.GLSLFieldSelectionExpression;
import glslplugin.lang.elements.statements.GLSLStatement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * GLSLAnnotator is the annotator implementation of GLSL.
 * It delegates the actual work to the {@link Annotator} implementators.
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 15, 2009
 *         Time: 8:47:00 PM
 * @see glslplugin.annotation.impl
 */
public class GLSLAnnotator implements com.intellij.lang.annotation.Annotator {
    private final static List<Annotator<GLSLAssignmentExpression>> assignmentExpressionAnnotators = new ArrayList<>();
    private final static List<Annotator<GLSLBinaryOperatorExpression>> binaryOperatorExpressionAnnotators = new ArrayList<>();
    private final static List<Annotator<GLSLFieldSelectionExpression>> fieldSelectionExpressionAnnotators = new ArrayList<>();
    private final static List<Annotator<GLSLStatement>> statementAnnotators = new ArrayList<>();

    static {
        assignmentExpressionAnnotators.add(new LValueAnnotator());
        binaryOperatorExpressionAnnotators.add(new BinaryOperatorTypeAnnotation());
        fieldSelectionExpressionAnnotators.add(new VectorComponentsAnnotation());
        fieldSelectionExpressionAnnotators.add(new MemberCheckAnnotation());
        statementAnnotators.add(new UnreachableAnnotation());
        statementAnnotators.add(new CheckReturnTypeAnnotation());
        statementAnnotators.add(new ConditionCheckAnnotation());
    }

    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder holder) {
        if (psiElement instanceof GLSLAssignmentExpression) {
            annotate((GLSLAssignmentExpression) psiElement, holder, assignmentExpressionAnnotators);
        }

        if (psiElement instanceof GLSLBinaryOperatorExpression) {
            annotate((GLSLBinaryOperatorExpression) psiElement, holder, binaryOperatorExpressionAnnotators);
        }

        if (psiElement instanceof GLSLFieldSelectionExpression) {
            annotate((GLSLFieldSelectionExpression) psiElement, holder, fieldSelectionExpressionAnnotators);
        }

        if (psiElement instanceof GLSLStatement) {
            annotate((GLSLStatement) psiElement, holder, statementAnnotators);
        }
    }

    /**
     * Calls all the given annotators with the supplied element and annotation holder.
     *
     * @param element    the element to annotate
     * @param holder     the annotator holder
     * @param annotators the annotators to call
     * @param <T>        the GLSLElement subclass for the particular type.
     */
    private <T extends GLSLElementImpl> void annotate(T element, AnnotationHolder holder, Iterable<Annotator<T>> annotators) {
        for (Annotator<T> annotation : annotators) {
            annotation.annotate(element, holder);
        }
    }
}
