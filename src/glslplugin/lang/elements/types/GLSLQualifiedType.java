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

package glslplugin.lang.elements.types;

import glslplugin.lang.elements.declarations.GLSLQualifier;
import org.jetbrains.annotations.NotNull;

/**
 * GLSLQualifiedType is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 27, 2009
 *         Time: 11:19:10 AM
 */
public class GLSLQualifiedType {
    @NotNull
    private final GLSLType type;
    @NotNull
    private final GLSLTypeQualifier[] qualifiers;


    public GLSLQualifiedType(@NotNull GLSLType type, @NotNull GLSLTypeQualifier[] qualifiers) {
        this.type = type;
        this.qualifiers = qualifiers;
    }

    public GLSLQualifiedType(@NotNull GLSLType type, @NotNull glslplugin.lang.elements.declarations.GLSLQualifier[] qualifiers) {
        this.type = type;
        this.qualifiers = convertQualifiers(qualifiers);
    }

    private static final GLSLTypeQualifier[] NO_QUALIFIERS = new GLSLTypeQualifier[0];

    public GLSLQualifiedType(@NotNull GLSLType type) {
        this.type = type;
        this.qualifiers = NO_QUALIFIERS;
    }

    @NotNull
    private GLSLTypeQualifier[] convertQualifiers(@NotNull GLSLQualifier[] qualifiers) {
        GLSLTypeQualifier[] result = new GLSLTypeQualifier[qualifiers.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = qualifiers[i].getQualifierType();
        }
        return result;
    }

    @NotNull
    public GLSLType getType() {
        return type;
    }

    @NotNull
    public GLSLTypeQualifier[] getQualifiers() {
        return qualifiers;
    }
}
