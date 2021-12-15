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

package glslplugin.lang.elements.expressions;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import glslplugin.lang.elements.GLSLIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * GLSLMethodCall is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 3, 2009
 *         Time: 12:41:53 PM
 */
public class GLSLMethodCallExpression extends GLSLSelectionExpressionBase {
    public GLSLMethodCallExpression(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    public GLSLIdentifier getMethodIdentifier() {
        GLSLIdentifier id = findChildByClass(GLSLIdentifier.class);
        if (id != null) {
            return id;
        } else {
            // Logger.getLogger("GLSLMethodCallExpression").warning("Method call expression with no method identifier.");
            return null;
        }
    }

    @NotNull
    public String getMethodName() {
        GLSLIdentifier methodIdentifier = getMethodIdentifier();
        if(methodIdentifier != null){
            return methodIdentifier.getName();
        }
        return "(unknown)";
    }

    @Nullable
    public GLSLParameterList getParameterList() {
        final PsiElement last = getLastChild();
        if(last instanceof GLSLParameterList){
            return (GLSLParameterList) last;
        }else{
            return null;
        }
    }

    @Override
    public String toString() {
        return "Method Call: " + getMethodName();
    }
}
