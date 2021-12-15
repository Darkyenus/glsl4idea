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

package glslplugin;

import com.intellij.lang.PairedBraceMatcher;
import com.intellij.lang.BracePair;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static glslplugin.lang.elements.GLSLTokenTypes.*;

public class GLSLPairedBraceMatcher implements PairedBraceMatcher {
    private static final BracePair[] bracePairs = new BracePair[] {
            new BracePair(LEFT_BRACE, RIGHT_BRACE, true),
            new BracePair(LEFT_PAREN, RIGHT_PAREN, false),
            new BracePair(LEFT_BRACKET, RIGHT_BRACKET, false),
    };

    @NotNull
    public BracePair @NotNull [] getPairs() {
        return bracePairs;
    }

    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType iElementType, @Nullable IElementType iElementType1) {
        return true;
    }

    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
        // TODO: IMPLEMENT
        return openingBraceOffset;
    }
}
