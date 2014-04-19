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

/**
 * The qualifier types as specified in the spec.
 */
public enum GLSLTypeQualifier {
    INVARIANT_QUALIFIER(1, "invariant qualifier"),
    STORAGE_QUALIFIER(2, "storage qualifier"),
    PARAMETER_QUALIFIER(3, "parameter qualifier"),
    //reserved for futuer specs: PRECISION_QUALIFIER
    ;
    public final int order;
    private final String textRepresentation;

    GLSLTypeQualifier(int order, String textRepresentation) {
        this.order = order;
        this.textRepresentation = textRepresentation;
    }

    @Override
    public String toString() {
        return textRepresentation;
    }
}
