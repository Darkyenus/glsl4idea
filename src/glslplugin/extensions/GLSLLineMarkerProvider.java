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

import com.intellij.codeHighlighting.Pass;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.util.IconLoader;
import com.intellij.psi.PsiElement;
import glslplugin.lang.elements.declarations.GLSLFunctionDeclarationImpl;
import glslplugin.lang.elements.declarations.GLSLFunctionDefinitionImpl;

import javax.swing.*;
import java.util.Collection;
import java.util.List;

/**
 * This annotation will show gutter icons for prototypes that are implemented and implementations of prototypes.
 */
public class GLSLLineMarkerProvider implements LineMarkerProvider {
    private static final Icon implemented = IconLoader.getIcon("/icons/implementedMethod.png");
    private static final Icon implementing = IconLoader.getIcon("/icons/implementingMethod.png");

    public GLSLLineMarkerProvider() {

    }

    public LineMarkerInfo getLineMarkerInfo(PsiElement expr) {
        //todo: add navigation support for guttericons and tooltips
        if (expr instanceof GLSLFunctionDefinitionImpl) {
            //todo: check if a prototype exists
            return new LineMarkerInfo<GLSLFunctionDefinitionImpl>((GLSLFunctionDefinitionImpl) expr, expr.getTextOffset(), implementing, Pass.UPDATE_ALL, null, null);
        } else if (expr instanceof GLSLFunctionDeclarationImpl) {
            //todo: check if it is implemented
            return new LineMarkerInfo<GLSLFunctionDeclarationImpl>((GLSLFunctionDeclarationImpl) expr, expr.getTextOffset(), implemented, Pass.UPDATE_ALL, null, null);
        }
        return null;
    }

    public void collectSlowLineMarkers(List<PsiElement> psiElements, Collection<LineMarkerInfo> lineMarkerInfos) {
        //not sure about the difference

    }
}
