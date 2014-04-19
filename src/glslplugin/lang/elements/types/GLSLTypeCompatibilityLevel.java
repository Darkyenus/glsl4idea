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

import org.jetbrains.annotations.NotNull;

/**
 * TypeCompatibilityLevel is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Mar 4, 2009
 *         Time: 12:48:15 PM
 */
public enum GLSLTypeCompatibilityLevel {
    INCOMPATIBLE,
    COMPATIBLE_WITH_IMPLICIT_CONVERSION,
    DIRECTLY_COMPATIBLE;

    @NotNull
    public static GLSLTypeCompatibilityLevel getCompatibilityLevel(@NotNull GLSLType source, @NotNull GLSLType target) {
        if (source.typeEquals(target)) {
            return DIRECTLY_COMPATIBLE;
        } else if (source.isConvertibleTo(target)) {
            return COMPATIBLE_WITH_IMPLICIT_CONVERSION;
        } else {
            return INCOMPATIBLE;
        }
    }

    @NotNull
    public static GLSLTypeCompatibilityLevel getCompatibilityLevel(@NotNull GLSLType[] source, @NotNull GLSLType[] target) {
        if (source.length != target.length) {
            return INCOMPATIBLE;
        }

        GLSLTypeCompatibilityLevel result = DIRECTLY_COMPATIBLE;

        for (int i = 0; i < source.length && result != INCOMPATIBLE; i++) {
            GLSLTypeCompatibilityLevel compatibility = getCompatibilityLevel(source[i], target[i]);
            switch (compatibility) {
                case COMPATIBLE_WITH_IMPLICIT_CONVERSION:
                    result = COMPATIBLE_WITH_IMPLICIT_CONVERSION;
                    break;

                case INCOMPATIBLE:
                    result = INCOMPATIBLE;
                    break;
            }
        }
        return result;
    }
}
