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

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import glslplugin.lang.GLSLLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static glslplugin.lang.elements.GLSLTokenTypes.*;

/**
 * @author Wyozi
 */
public class GLSLFormattingModelBuilder implements FormattingModelBuilder {
    @NotNull
    @Override
    public FormattingModel createModel(PsiElement psiElement, CodeStyleSettings codeStyleSettings) {
        return FormattingModelProvider.createFormattingModelForPsiFile(
                psiElement.getContainingFile(),
                new GLSLFormattingBlock(psiElement.getNode(), null, null, createSpacingBuilder(codeStyleSettings)),
                codeStyleSettings);
    }

    private SpacingBuilder createSpacingBuilder(CodeStyleSettings codeStyleSettings) {
        return new SpacingBuilder(codeStyleSettings, GLSLLanguage.GLSL_LANGUAGE)
                .before(COMMA).spaceIf(codeStyleSettings.SPACE_BEFORE_COMMA)
                .after(COMMA).spaceIf(codeStyleSettings.SPACE_AFTER_COMMA)
                .before(SEMICOLON).none()

                .after(LEFT_BRACKET).none()
                .before(RIGHT_BRACKET).none()

                .after(LEFT_BRACE).spaces(1)
                .before(RIGHT_BRACE).spaces(1)

                .withinPair(LEFT_PAREN, RIGHT_PAREN).spaceIf(codeStyleSettings.SPACE_WITHIN_METHOD_CALL_PARENTHESES)

                .around(FLOW_KEYWORDS).spaces(1)
                ;
    }

    @Nullable
    @Override
    public TextRange getRangeAffectingIndent(PsiFile psiFile, int i, ASTNode astNode) {
        return astNode.getTextRange();
    }
}
