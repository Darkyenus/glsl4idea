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

package glslplugin.lang.elements.declarations;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * NewSingleDeclarationImpl is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 2, 2009
 *         Time: 12:46:15 PM
 */
public class GLSLSingleDeclarationImpl extends GLSLDeclarationImpl implements GLSLSingleDeclaration {
    public GLSLSingleDeclarationImpl(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @NotNull
    public String getDeclaredName() {
        final GLSLDeclarator declarator = getDeclarator();
        if(declarator == null){
            return "(unknown)";
        }else{
            return declarator.getIdentifierName();
        }
    }

    /**
     * Overridden to provide the single GLSLIdentifier.
     * It is not packaged in DECLARATOR_LIST like the other declarations.
     *
     * @return the declarator list.
     */
    @Override
    @NotNull
    public GLSLDeclarator[] getDeclarators() {
        return findChildrenByClass(GLSLDeclarator.class);
    }

    @Nullable
    public GLSLDeclarator getDeclarator() {
        GLSLDeclarator[] declarators = getDeclarators();
        if(declarators.length == 0){
            return null;
        }else{
            return declarators[0];
        }
    }
}
