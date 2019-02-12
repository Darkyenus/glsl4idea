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

package glslplugin.intentions.vectorcomponents;

import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.PopupChooserBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.ui.components.JBList;
import com.intellij.util.ThrowableRunnable;
import glslplugin.intentions.Intentions;
import glslplugin.lang.elements.GLSLIdentifier;
import org.jetbrains.annotations.NotNull;

import static glslplugin.intentions.vectorcomponents.VectorComponentsPredicate.*;

public class VectorComponentsIntention extends Intentions {

    private static final Logger LOG = Logger.getInstance(VectorComponentsIntention.class);

    private enum Components {
        RGBA("rgba"),
        XYZW("xyzw"),
        STPQ("stpq");

        private String components;

        Components(String components) {
            this.components = components;
        }

        public String getComponent(int i) {
            return String.valueOf(components.charAt(i));
        }
    }

    private String[] results = new String[2];

    public VectorComponentsIntention() {
        super(new VectorComponentsPredicate());
    }

    @NotNull
    @Override
    public String getText() {
        return "Convert vector components";
    }

    @NotNull
    public String getFamilyName() {
        return "GLSL vector components";
    }

    protected void processIntention(PsiElement psiElement) {
        GLSLIdentifier elementTemp = null;
        if(psiElement instanceof GLSLIdentifier){
            elementTemp = (GLSLIdentifier) psiElement;
        }else{
            PsiElement parent = psiElement.getParent();
            if(parent instanceof GLSLIdentifier){
                elementTemp = (GLSLIdentifier) parent;
            }
        }
        if(elementTemp == null){
            Logger.getInstance(VectorComponentsIntention.class).warn("Could not find GLSLIdentifier for psiElement: "+psiElement);
            return;
        }
        final GLSLIdentifier element = elementTemp;

        String components = element.getText();

        createComponentVariants(components);

        String[] variants = new String[]{components + " -> " + results[0], components + " -> " + results[1]};
        //http://www.jetbrains.net/devnet/message/5208622#5208622
        final JBList<String> list = new JBList<>(variants);
        PopupChooserBuilder builder = new PopupChooserBuilder(list);
        builder.setTitle("Select Variant");
        builder.setItemChoosenCallback(new Runnable() {
            public void run() {
                try {
                    WriteCommandAction.writeCommandAction(element.getProject(), element.getContainingFile()).run(new ThrowableRunnable<Throwable>() {
                        @Override
                        public void run() {
                            replaceIdentifierElement(element, results[list.getSelectedIndex()]);
                        }
                    });
                } catch (Throwable t) {
                    LOG.error("replaceIdentifierElement failed", t);
                }
            }
        });
        JBPopup popup = builder.createPopup();
        popup.showInBestPositionFor(getEditor());
    }

    private void createComponentVariants(String components) {
        int i = 0;
        if (!rgba.matcher(components).matches()) {
            results[i++] = convertComponents(components, Components.RGBA);
        }

        if (!xyzw.matcher(components).matches()) {
            results[i++] = convertComponents(components, Components.XYZW);
        }

        if (!stpq.matcher(components).matches()) {
            results[i++] = convertComponents(components, Components.STPQ);
        }

        if (i != 2) {
            throw new RuntimeException("Unknown components!");
        }
    }

    private String convertComponents(String vectorComponents, Components components) {
        return toComponents(toNumbers(vectorComponents), components);
    }

    private String toNumbers(String components) {
        String results = components.replaceAll("[rsx]", "1");
        results = results.replaceAll("[gty]", "2");
        results = results.replaceAll("[bpz]", "3");
        results = results.replaceAll("[aqw]", "4");
        return results;
    }

    private String toComponents(String componentsAsNumbers, Components components) {
        String results = componentsAsNumbers.replaceAll("1", components.getComponent(0));
        results = results.replaceAll("2", components.getComponent(1));
        results = results.replaceAll("3", components.getComponent(2));
        results = results.replaceAll("4", components.getComponent(3));
        return results;
    }
}

