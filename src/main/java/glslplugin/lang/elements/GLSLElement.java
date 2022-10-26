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

package glslplugin.lang.elements;

import com.intellij.lang.ASTNode;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import glslplugin.lang.parser.GLSLASTFactory;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * GLSLElement is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 6, 2009
 *         Time: 1:33:24 PM
 */
public interface GLSLElement extends NavigatablePsiElement {

    /**
     * @return the parent if the parent is of the given class or null otherwise
     */
    @Nullable
    <T extends GLSLElement> T findParentByClass(Class<T> clazz);

    /**
     * @return the parent if the parent is one of the given classes or null otherwise
     */
    @SuppressWarnings("unchecked")
    @Nullable
    PsiElement findParentByClasses(Class<? extends PsiElement>... clazzes);
    @Nullable
    PsiElement findParentByClasses(Collection<Class<? extends PsiElement>> clazzes);


    /** Just exposes the same method from {@link com.intellij.psi.impl.PsiElementBase}
     * so that this whole interface can be implemented in the default methods. */
    @Nullable
    <T> T findChildByClass(Class<T> aClass);

    /** Just exposes the same method from {@link com.intellij.psi.impl.PsiElementBase}
     * so that this whole interface can be implemented in the default methods. */
    <T extends PsiElement> @Nullable T findChildByType(IElementType type);

    /** Just exposes the same method from {@link com.intellij.psi.impl.PsiElementBase}
     * so that this whole interface can be implemented in the default methods. */
    <T> T @NotNull [] findChildrenByClass(Class<T> aClass);

    /**
     * Retrieve text of the leaf element. Supports redefined tokens.
     */
    @Contract("null->null;!null->!null")
    static @Nullable String text(@Nullable PsiElement element) {
        if (element == null) return null;
        return nodeText(element.getNode());
    }

    /**
     * Retrieve text of the leaf element. Supports redefined tokens.
     */
    @Contract("null->null;!null->!null")
    static @Nullable String nodeText(@Nullable ASTNode node) {
        if (node == null) return null;
        if (node instanceof GLSLASTFactory.LeafPsiCompositeElement leaf) return leaf.actualText;
        return node.getText();
    }
}
