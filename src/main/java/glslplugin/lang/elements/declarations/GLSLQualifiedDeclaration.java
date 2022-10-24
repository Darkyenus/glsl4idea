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

import glslplugin.lang.elements.GLSLElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * GLSLQualifiedDeclaration is a common interface for all declarations that can have qualifiers;
 * variable declarations, function declarations/definitions, and struct member declarations.
 */
public interface GLSLQualifiedDeclaration extends GLSLElement {
    GLSLQualifiedDeclaration[] NO_DECLARATIONS = new GLSLQualifiedDeclaration[0];

    /**
     * Returns the type specifier Psi element for this declaration.
     * <p/>
     * <b>WARNING!</b>
     * <p/>
     * This is not a complete type of any variable as a declarator may contain an array specifier.
     * For the complete type use {@link glslplugin.lang.elements.declarations.GLSLDeclarator#getType()} instead.
     *
     * @return the type specifier or null if declaration is malformed
     */
    @Nullable
    default GLSLTypeSpecifier getTypeSpecifierNode() {
        return findChildByClass(GLSLTypeSpecifier.class);
    }

    /**
     * Returns "(unknown)" if getTypeSpecifierNode() returns null
     * @return result of getTypeSpecifierNode().getTypeName()
     */
    @NotNull
    default String getTypeSpecifierNodeTypeName() {
        GLSLTypeSpecifier specifier = getTypeSpecifierNode();
        if(specifier != null){
            return specifier.getTypeName();
        } else {
            return "(unknown)";
        }
    }

    @Nullable
    default GLSLQualifierList getQualifierList() {
        return findChildByClass(GLSLQualifierList.class);
    }

    @NotNull
    default GLSLQualifier[] getQualifiers() {
        final GLSLQualifierList qualifierList = getQualifierList();
        if(qualifierList == null){
            return GLSLQualifier.NO_QUALIFIERS;
        }else{
            return qualifierList.getQualifiers();
        }
    }

}
