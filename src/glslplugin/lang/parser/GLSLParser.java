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

package glslplugin.lang.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class GLSLParser implements PsiParser, LightPsiParser {

    @Override
    @NotNull
    public ASTNode parse(IElementType root, PsiBuilder builder) {
        //builder.setDebugMode(true);
        parseLight(root, builder);
        return builder.getTreeBuilt();
    }

    @Override
    public void parseLight(IElementType root, PsiBuilder builder) {
        // builder.setDebugMode(true);
        final PsiBuilder.Marker rootMarker = builder.mark();
        if (!builder.eof()) { //Empty file is not an error
            final GLSLParsing theRealParser = new GLSLParsing(builder);

            theRealParser.parseTranslationUnit();
            while (!builder.eof()) // exhaust the file if unable to parse everything
                builder.advanceLexer();
        }

        rootMarker.done(root);
    }
}
