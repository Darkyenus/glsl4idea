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
    //This is optimal for small amount of annotators, if the list grows, change to map.
    //These two instances form something like map. Add only through add(Annotator) method.
    private final static List<Class<? extends PsiElement>> annotationTypes = new ArrayList<Class<? extends PsiElement>>();
    private final static List<Annotator<? extends PsiElement>> annotators = new ArrayList<Annotator<? extends PsiElement>>();

    private static void add(Annotator<?> annotator) {
        annotationTypes.add(annotator.getElementType());
        annotators.add(annotator);
    }

    static {
        //Register all annotators
        add(new LValueAnnotator());
        add(new UnaryOperatorTypeAnnotation());
        add(new BinaryOperatorTypeAnnotation());
        add(new VectorComponentsAnnotation());
        add(new MemberCheckAnnotation());
        add(new UnreachableAnnotation());
        add(new CheckReturnTypeAnnotation());
        add(new ConditionCheckAnnotation());
        add(new MissingReturnAnnotation());
        add(new DeclarationAssignmentTypeAnnotation());
        add(new ReservedKeywordAnnotation());
        add(new ReservedIdentifierAnnotation());
        add(new InitializerListEmptyAnnotation());
        add(new RedefinedTokenAnnotation());
        add(new StatementParentAnnotation());
        add(new SubscriptBoundAnnotation());
        add(new ConstModificationAnnotation());
        add(new ConstInitializationAnnotation());
        add(new ParameterQualifierAnnotation());
        add(new SwitchAnnotation());
    }

    public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder holder) {
        for (int i = 0; i < annotationTypes.size(); i++) {
            Class<? extends PsiElement> type = annotationTypes.get(i);
            if (type.isInstance(psiElement)) {
                annotators.get(i).annotateGeneric(type.cast(psiElement), holder);
            }
        }
    }
}
