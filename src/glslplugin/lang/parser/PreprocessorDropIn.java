/*
 * Copyright 2010 Jean-Paul Balabanian and Yngve Devik Hammersland
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

package glslplugin.lang.parser;

import java.util.Collections;
import java.util.List;

/**
 * @author Darkyen
 */
final class PreprocessorDropIn {
    public final PreprocessorDropInType type;
    public final List<PreprocessorToken> tokens;
    public final String text;

    private PreprocessorDropIn(PreprocessorDropInType type, List<PreprocessorToken> tokens) {
        this.type = type;
        this.tokens = tokens;
        this.text = "";
    }

    PreprocessorDropIn(PreprocessorDropInType type, List<PreprocessorToken> tokens, String text) {
        this.type = type;
        this.tokens = tokens;
        this.text = text;
    }

    public static final PreprocessorDropIn EMPTY = new PreprocessorDropIn(PreprocessorDropInType.EMPTY, Collections.<PreprocessorToken>emptyList());
}
