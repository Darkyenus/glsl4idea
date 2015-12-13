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

package glslplugin.lang;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import glslplugin.lang.elements.*;
import glslplugin.lang.parser.GLSLFile;
import glslplugin.lang.parser.GLSLParser;
import glslplugin.lang.scanner.GLSLFlexAdapter;
import org.jetbrains.annotations.NotNull;

public class GLSLParserDefinition implements ParserDefinition {
    private static final GLSLPsiElementFactory psiFactory = new GLSLPsiElementFactory();

    @NotNull
    public Lexer createLexer(Project project) {
        return new GLSLFlexAdapter();
    }

    public PsiParser createParser(Project project) {
        return new GLSLParser();
    }

    public IFileElementType getFileNodeType() {
        return GLSLElementTypes.FILE;
    }

    @NotNull
    public TokenSet getWhitespaceTokens() {
        return TokenSet.create(GLSLTokenTypes.WHITE_SPACE);
    }

    @NotNull
    public TokenSet getCommentTokens() {
        return GLSLTokenTypes.COMMENTS;
    }

    @NotNull
    public TokenSet getStringLiteralElements() {
        return TokenSet.EMPTY;
    }

    @NotNull
    public PsiElement createElement(ASTNode node) {
        GLSLElement elt =  psiFactory.create(node);
        if (elt != null) {
            return elt;
        } else {
            //Logger.getInstance(GLSLParserDefinition.class).warn("Creating default GLSLElementImpl for "+node);
            return new GLSLElementImpl(node);
        }
    }

    public PsiFile createFile(FileViewProvider fileViewProvider) {
        return new GLSLFile(fileViewProvider);
    }

    public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode astNode, ASTNode astNode1) {
        return SpaceRequirements.MAY;
    }
}
