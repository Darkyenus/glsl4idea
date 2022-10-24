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

package glslplugin.intentions;

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.ui.components.JBList;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.ThrowableRunnable;
import glslplugin.lang.elements.expressions.GLSLExpression;
import glslplugin.lang.elements.expressions.GLSLFieldSelectionExpression;
import glslplugin.lang.elements.types.GLSLScalarType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLVectorType;
import glslplugin.util.VectorComponents;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class VectorComponentsIntention extends PsiElementBaseIntentionAction {

    private static final Logger LOG = Logger.getInstance(VectorComponentsIntention.class);

    @NotNull
    @Override
    public String getText() {
        return "Convert vector components";
    }

    @NotNull
    public String getFamilyName() {
        return "GLSL vector components";
    }


    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
        if (!(element instanceof GLSLFieldSelectionExpression fse)) {
            return false;
        }
        final String name = fse.getFieldName();
        if (name == null || name.isEmpty()) return false;

        final GLSLExpression leftHand = fse.getLeftHandExpression();
        if (leftHand == null) return false;
        final GLSLType objectType = leftHand.getType();
        if (!(objectType instanceof GLSLScalarType) && !(objectType instanceof GLSLVectorType)) {
            return false;
        }

        // Check that all component names are valid, ignoring type width, amount or mixing
        for (int i = 0; i < name.length(); i++) {
            if (VectorComponents.ALL_COMPONENTS.indexOf(name.charAt(i)) == -1) {
                return false;// The character itself must be mappable
            }
        }
        return true;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
        if (!(element instanceof GLSLFieldSelectionExpression fse)) {
            throw new IncorrectOperationException("Can't apply to non-field selection");
        }

        final String name = fse.getFieldName();
        if (name == null || name.isEmpty()) throw new IncorrectOperationException("Field selection has empty field name");

        // Don't check left hand type, already checked above
        int[] components = new int[name.length()];
        outer: for (int i = 0; i < name.length(); i++) {
            final char c = name.charAt(i);
            for (String swizzleSet : VectorComponents.SETS) {
                final int component = swizzleSet.indexOf(c);
                if (component >= 0) {
                    components[i] = component;
                    continue outer;
                }
            }
            throw new IncorrectOperationException("Field selection has invalid component '"+c+"'");
        }

        final ArrayList<String> variants = new ArrayList<>();
        final StringBuilder variantBuilder = new StringBuilder();
        for (String swizzleSet : VectorComponents.SETS) {
            for (int component : components) {
                variantBuilder.append(swizzleSet.charAt(component));
            }

            final String variant = variantBuilder.toString();
            if (!variant.equals(name)) {
                variants.add(variant);
            }
            variantBuilder.setLength(0);
        }

        //http://www.jetbrains.net/devnet/message/5208622#5208622
        final JBList<String> list = new JBList<>(variants);

        PopupChooserBuilder<String> builder = new PopupChooserBuilder<>(list);
        builder.setTitle("Select Variant");
        builder.setItemChoosenCallback(() -> {
            try {
                WriteCommandAction.writeCommandAction(element.getProject(), element.getContainingFile())
                        .run((ThrowableRunnable<Throwable>) () ->
                                fse.setFieldName(variants.get(list.getSelectedIndex()))
                        );
            } catch (Throwable t) {
                LOG.error("replaceIdentifierElement failed", t);
            }
        });
        builder.createPopup().showInBestPositionFor(editor);
    }
}

