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
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.psi.text.BlockSupport;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GLSLParser implements PsiParser, LightPsiParser {

    private boolean crashing = false;

    @Override
    @NotNull
    public ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder builder) {
        // Intentionally not using parser.mark(), because it would prevent memory saving optimizations in parser
        final PsiBuilder.Marker rootMarker = builder.mark();
        if (builder.eof()) {
            rootMarker.done(root);
            return builder.getTreeBuilt();
        }

        try {


            final GLSLParsing parser = new GLSLParsing(builder);
            if (crashing) {
                parser.b.setDebugMode(true);
            }
            parser.parseTranslationUnit();

            rootMarker.done(root);
            return parser.b.getTreeBuilt();
        } catch (ProcessCanceledException | BlockSupport.ReparsedSuccessfullyException expected) {
            // Not a problem
            throw expected;
        } catch (Exception ex) {
            crashing = true;
            Logger.getLogger("GLSLParser").log(Level.WARNING, "Crashed while trying to parse "+root, ex);
            throw ex;
        }
    }

    @Override
    public void parseLight(IElementType root, PsiBuilder builder) {
        final PsiBuilder.Marker rootMarker = builder.mark();
        if (builder.eof()) {
            rootMarker.done(root);
            return;
        }

        final GLSLParsing parser = new GLSLParsing(builder);
        if (crashing) {
            parser.b.setDebugMode(true);
        }
        parser.parseTranslationUnit();

        rootMarker.done(root);
    }
}
