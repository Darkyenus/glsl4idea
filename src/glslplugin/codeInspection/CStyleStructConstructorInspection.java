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

package glslplugin.codeInspection;

import com.intellij.codeInspection.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import glslplugin.lang.elements.GLSLPsiElementFactory;
import glslplugin.lang.elements.declarations.GLSLDeclaration;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.declarations.GLSLInitializerList;
import glslplugin.lang.elements.expressions.GLSLAssignmentExpression;
import glslplugin.lang.elements.expressions.GLSLExpression;
import glslplugin.lang.elements.expressions.GLSLFunctionCallExpression;
import glslplugin.lang.elements.expressions.GLSLParameterList;
import glslplugin.lang.elements.types.GLSLArrayType;
import glslplugin.lang.elements.types.GLSLStructType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.parser.GLSLFile;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Wyozi
 */
public class CStyleStructConstructorInspection extends LocalInspectionTool {
    @Nls
    @NotNull
    @Override
    public String getDisplayName() {
        return "C-Style Constructor";
    }

    @Nullable
    @Override
    public ProblemDescriptor[] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        if (!(file instanceof GLSLFile)) return null;

        List<ProblemDescriptor> descriptorList = new ArrayList<ProblemDescriptor>();

        Collection<GLSLInitializerList> initializerLists = PsiTreeUtil.findChildrenOfAnyType(file, GLSLInitializerList.class);
        for (GLSLInitializerList initializerList : initializerLists) {
            PsiElement parent = initializerList.getParent();

            if (parent instanceof GLSLDeclarator && checkType(((GLSLDeclarator) parent).getQualifiedType().getType())) {
                descriptorList.add(manager.createProblemDescriptor(initializerList, "C-Style Constructor", new ConvertToNormalConstructor(), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, true));
            }
        }

        return descriptorList.toArray(new ProblemDescriptor[descriptorList.size()]);
    }

    /**
     * Checks if this type should be inspected.
     */
    private boolean checkType(GLSLType type) {
        return !(type instanceof GLSLArrayType) && type instanceof GLSLStructType;
    }

    private static class ConvertToNormalConstructor implements LocalQuickFix {
        @Nls
        @NotNull
        @Override
        public String getName() {
            return "Convert To Normal Constructor";
        }

        @Nls
        @NotNull
        @Override
        public String getFamilyName() {
            return getName();
        }

        @Override
        public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor) {
            GLSLInitializerList initializerList = (GLSLInitializerList) problemDescriptor.getPsiElement();
            GLSLDeclarator parent = (GLSLDeclarator) initializerList.getParent();

            GLSLType type = parent.getQualifiedType().getType();
            String ctorName = type.getTypename();

            PsiElement[] children = initializerList.getChildren();
            System.out.println(Arrays.toString(children));

            PsiElement funcCallExpr = GLSLPsiElementFactory.createExpressionFromText(project, ctorName + "();");
            GLSLParameterList paramList = PsiTreeUtil.findChildOfType(funcCallExpr, GLSLParameterList.class);

            // Copy all children except first and last (because they are braces)
            PsiElement startChild = initializerList.getFirstChild().getNextSibling(); // inclusive
            PsiElement lastChild = initializerList.getLastChild();                    // exclusive
            for (PsiElement child = startChild; child != lastChild; child = child.getNextSibling()) {
                paramList.add(child.copy());
            }

            initializerList.replace(funcCallExpr);
        }
    }
}
