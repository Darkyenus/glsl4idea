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

package glslplugin.formatter;

import com.intellij.formatting.FormattingContext;
import com.intellij.formatting.FormattingModel;
import com.intellij.formatting.FormattingModelBuilder;
import com.intellij.formatting.FormattingModelProvider;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import glslplugin.lang.GLSLLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static glslplugin.lang.elements.GLSLTokenTypes.COMMA;
import static glslplugin.lang.elements.GLSLTokenTypes.COMMENT_LINE;
import static glslplugin.lang.elements.GLSLTokenTypes.FLOW_KEYWORDS;
import static glslplugin.lang.elements.GLSLTokenTypes.LEFT_BRACE;
import static glslplugin.lang.elements.GLSLTokenTypes.LEFT_BRACKET;
import static glslplugin.lang.elements.GLSLTokenTypes.LEFT_PAREN;
import static glslplugin.lang.elements.GLSLTokenTypes.RIGHT_BRACE;
import static glslplugin.lang.elements.GLSLTokenTypes.RIGHT_BRACKET;
import static glslplugin.lang.elements.GLSLTokenTypes.RIGHT_PAREN;
import static glslplugin.lang.elements.GLSLTokenTypes.SEMICOLON;

/**
 * @author Wyozi
 */
public class GLSLFormattingModelBuilder implements FormattingModelBuilder {

    @Override
    public @NotNull FormattingModel createModel(@NotNull FormattingContext formattingContext) {
        final CodeStyleSettings codeStyleSettings = formattingContext.getCodeStyleSettings();
        return FormattingModelProvider.createFormattingModelForPsiFile(
                formattingContext.getContainingFile(),
                new GLSLFormattingBlock(formattingContext.getNode(), null, null, createSpacingBuilder(codeStyleSettings)),
                codeStyleSettings);
    }

    private SpacingBuilder createSpacingBuilder(CodeStyleSettings codeStyleSettings) {
        final CommonCodeStyleSettings commonSettings = codeStyleSettings.getCommonSettings(GLSLLanguage.GLSL_LANGUAGE);

        return new SpacingBuilder(codeStyleSettings, GLSLLanguage.GLSL_LANGUAGE)
                .before(COMMA).spaceIf(commonSettings.SPACE_BEFORE_COMMA)
                .after(COMMA).spaceIf(commonSettings.SPACE_AFTER_COMMA)
                .before(SEMICOLON).none()

                .after(LEFT_BRACKET).none()
                .before(RIGHT_BRACKET).none()

                .after(LEFT_BRACE).spaces(1)
                .before(RIGHT_BRACE).spaces(1)

                .withinPair(LEFT_PAREN, RIGHT_PAREN).spaceIf(commonSettings.SPACE_WITHIN_METHOD_CALL_PARENTHESES)

                .around(FLOW_KEYWORDS).spaces(1)

                .before(COMMENT_LINE).spaceIf(commonSettings.LINE_COMMENT_ADD_SPACE)
                ;
    }

    @Nullable
    @Override
    public TextRange getRangeAffectingIndent(PsiFile psiFile, int i, ASTNode astNode) {
        return astNode.getTextRange();
    }
}
