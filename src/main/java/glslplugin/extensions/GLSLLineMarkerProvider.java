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

import com.intellij.codeInsight.daemon.DefaultGutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.declarations.GLSLFunctionDeclaration;
import glslplugin.lang.elements.declarations.GLSLFunctionDefinition;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * This annotation will show gutter icons for prototypes that are implemented and implementations of prototypes.
 */
public class GLSLLineMarkerProvider implements LineMarkerProvider {
    private static final Icon implemented = AllIcons.Gutter.ImplementedMethod;
    private static final Icon implementing = AllIcons.Gutter.ImplementingMethod;

    private static Collection<GLSLFunctionDeclaration> findOtherFunctionDeclarations(GLSLFunctionDeclaration basedOn, boolean declarations, boolean definitions) {
        final Collection<GLSLFunctionDeclaration> possibilities = PsiTreeUtil.findChildrenOfType(basedOn.getContainingFile(), GLSLFunctionDeclaration.class);
        final String targetSignature = basedOn.getSignature();
        final ArrayList<GLSLFunctionDeclaration> result = new ArrayList<>(possibilities.size() - 1);
        for (GLSLFunctionDeclaration possibility : possibilities) {
            if (possibility == basedOn) {
                continue;
            }
            final boolean isDefinition = possibility instanceof GLSLFunctionDefinition;
            if (isDefinition && !definitions || !isDefinition && !declarations) {
                continue;
            }
            if (targetSignature.equals(possibility.getSignature())) {
                result.add(possibility);
            }
        }
        return result;
    }

    private static final Supplier<String> implementedAccessibilityName = () -> "implemented";
    private static final Supplier<String> implementingAccessibilityName = () -> "implementing";

    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement expr) {
        if (!(expr instanceof LeafPsiElement leaf) || leaf.getElementType() != GLSLTokenTypes.IDENTIFIER) {
            return null;
        }

        final PsiElement parent = expr.getParent();

        if (!(parent instanceof GLSLFunctionDeclaration functionDeclaration) || !GLSLElement.text(leaf).equals(functionDeclaration.getFunctionName())) {
            return null;
        }

        final boolean isDefinition = functionDeclaration instanceof GLSLFunctionDefinition;
        final Collection<GLSLFunctionDeclaration> navigationTargets = findOtherFunctionDeclarations(functionDeclaration, isDefinition, !isDefinition);
        if (navigationTargets.isEmpty()) {
            return null;
        }

        final String signature = functionDeclaration.getSignature();
        return new LineMarkerInfo<>(expr,
                expr.getTextRange(),
                isDefinition ? implementing : implemented,
                (element) -> signature + (isDefinition ? " has forward declaration" : " has definition"),
                new DefaultGutterIconNavigationHandler<>(navigationTargets, (isDefinition ? "Declarations of " : "Definitions of ") + signature),
                GutterIconRenderer.Alignment.RIGHT,
                isDefinition ? implementingAccessibilityName : implementedAccessibilityName);
    }
}
