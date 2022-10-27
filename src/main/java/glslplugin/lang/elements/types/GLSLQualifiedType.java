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
    private final GLSLQualifier[] qualifiers;


    public GLSLQualifiedType(@NotNull GLSLType type, @NotNull GLSLQualifier[] qualifiers) {
        this.type = type;
        this.qualifiers = qualifiers;
    }

    public GLSLQualifiedType(@NotNull GLSLType type) {
        this.type = type;
        this.qualifiers = GLSLQualifier.NO_QUALIFIERS;
    }

    @NotNull
    public GLSLType getType() {
        return type;
    }

    @NotNull
    public GLSLQualifier[] getQualifiers() {
        return qualifiers;
    }

    public boolean hasQualifier(GLSLQualifier.Qualifier qualifier) {
        for (GLSLQualifier candidate : getQualifiers()) {
            if (candidate.getQualifier() == qualifier) return true;
        }
        return false;
    }

    /** Return true, if this type could represent an L-value expression. */
    public boolean isLValue() {
        if (type.getBaseType() instanceof GLSLOpaqueType) {
            return false;
        }

        return !hasQualifier(GLSLQualifier.Qualifier.CONST)
                && !hasQualifier(GLSLQualifier.Qualifier.READONLY);
    }
}
