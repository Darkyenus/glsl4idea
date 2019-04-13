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

package glslplugin.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.BalloonBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.ui.awt.RelativePoint;
import glslplugin.lang.GLSLLanguage;
import glslplugin.lang.elements.expressions.GLSLExpression;
import glslplugin.lang.elements.types.GLSLType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static glslplugin.Utils.escapeHtml;

/**
 * GLSLDeduceExpressionTypeAction is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 7, 2009
 *         Time: 12:24:02 AM
 */
public class GLSLDeduceExpressionTypeAction extends AnAction {


    public void actionPerformed(@NotNull AnActionEvent e) {
        PsiElement element = getPsiElementEnclosingSelection(e);

        while (element != null && !(element instanceof GLSLExpression)) {
            element = element.getParent();
            if (element instanceof PsiFile) {
                element = null;
            }
        }

        String typename = null;
        String expressionText = null;
        String value = null;

        GLSLExpression expr = (GLSLExpression) element;

        if (expr != null) {
            expressionText = expr.getText();
            GLSLType type = expr.getType();
            typename = type.getTypename();

            if (expr.isConstantValue()) {
                final Object constValue = expr.getConstantValue();
                if (constValue != null) {
                    value = constValue.toString();
                }
            }
        }

        if (expressionText == null && element != null) {
            expressionText = element.getText();
        }

        showBalloon(e, createHtml(expressionText, typename, value));
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        final PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
        if (psiFile != null) {
            e.getPresentation().setEnabledAndVisible(GLSLLanguage.GLSL_LANGUAGE.equals(psiFile.getLanguage()));
        }
    }

    private void showBalloon(AnActionEvent e, String html) {
        final Editor editor = e.getData(CommonDataKeys.EDITOR_EVEN_IF_INACTIVE);
        if (editor == null) {
            return;
        }
        final JBPopupFactory factory = JBPopupFactory.getInstance();
        final BalloonBuilder builder = factory.createBalloonBuilder(new JLabel(html));
        Balloon balloon = builder.createBalloon();
        RelativePoint position = factory.guessBestPopupLocation(editor);
        balloon.show(position, Balloon.Position.below);
    }

    private static String createHtml(String expression, String type, String value) {
        expression = makeExpressionTextHtml(expression, type != null);
        type = makeTypenameHtml(type);
        value = escapeHtml(value);

        StringBuilder b = new StringBuilder();
        b.append("<html><table>");

        b.append("<tr><th align='right'>Expression:</th>");
        b.append("<td>").append(expression).append("</td></tr>");

        b.append("<tr><th align='right'>Type:</th>");
        b.append("<td>").append(type).append("</td></tr>");

        if (value != null) {
            b.append("<tr><th align='right'>Value:</th>");
            b.append("<td>").append(value).append("</td></tr>");
        }

        b.append("</table></html>");

        return b.toString();
    }

    private static String makeTypenameHtml(String type) {
        if (type == null) {
            return  "unable to deduce type";
        }
        else return  "<code>" + escapeHtml(type) + "</code>";
    }

    private static String makeExpressionTextHtml(String expression, boolean valid) {
        if (valid) {
            return "<code>" + escapeHtml(expression) + "</code>";
        } else if (expression != null) {
            return "<code>" + escapeHtml(expression) + "</code> (Not an expression)";
        } else {
            return "selection is not an expression";
        }
    }

    private PsiElement getPsiElementEnclosingSelection(AnActionEvent e) {
        final PsiFile file = e.getData(CommonDataKeys.PSI_FILE);
        final Editor editor = e.getData(CommonDataKeys.EDITOR_EVEN_IF_INACTIVE);

        assert file != null;
        assert editor != null;

        final SelectionModel selectionModel = editor.getSelectionModel();
        final int start = selectionModel.getSelectionStart();
        final int end = selectionModel.getSelectionEnd();

        PsiElement elt = getElementInclusiveRange(file, new TextRange(start, end));
        assert elt != null;
        return elt;
    }

    // From https://github.com/JetBrains/intellij-community/blob/master/java/java-psi-api/src/com/intellij/psi/util/PsiUtil.java#L399
    private static PsiElement getElementInclusiveRange(PsiElement scope, TextRange range) {
        PsiElement psiElement = scope.findElementAt(range.getStartOffset());
        while (psiElement != null && !psiElement.getTextRange().contains(range)) {
            if (psiElement == scope) {
                return null;
            }
            psiElement = psiElement.getParent();
        }
        return psiElement;
    }
}
