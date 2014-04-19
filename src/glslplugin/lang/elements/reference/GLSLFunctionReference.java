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

package glslplugin.lang.elements.reference;

import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.ResolveResult;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLIdentifier;
import org.jetbrains.annotations.NotNull;

/**
 * GLSLFunctionReference is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Mar 1, 2009
 *         Time: 10:04:11 PM
 */
public class GLSLFunctionReference extends GLSLReferenceBase<GLSLIdentifier, GLSLElement>
        implements PsiPolyVariantReference {

    GLSLElement[] targets;

    public GLSLFunctionReference(GLSLIdentifier source, GLSLElement[] targets) {
        super(source, targets[0]);
        this.targets = targets;
    }

    @NotNull
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        ResolveResult[] result = new ResolveResult[targets.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new PsiElementResolveResult(targets[i]);
        }
        return result;
    }

    @Override
    public GLSLElement resolve() {
        if (targets.length == 1) {
            return target;
        } else {
            return null;
        }
    }
}
