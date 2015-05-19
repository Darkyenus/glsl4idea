/*
 * Copyright 2010 Jean-Paul Balabanian and Yngve Devik Hammersland
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

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import glslplugin.lang.GLSLLanguage;
import glslplugin.lang.elements.GLSLElement;

import java.util.List;

/**
 * @author Darkyen
 */
public final class GLSLRedefinedTokenType extends IElementType {

    private final List<PreprocessorToken> redefinedTo;

    protected GLSLRedefinedTokenType(List<PreprocessorToken> redefinedTo) {
        super("GLSLRedefinedToken", GLSLLanguage.GLSL_LANGUAGE, false);
        this.redefinedTo = redefinedTo;
    }

    public static final class GLSLRedefinedPsiElement extends ASTWrapperPsiElement implements GLSLElement {

        private final GLSLRedefinedTokenType type;

        public GLSLRedefinedPsiElement(ASTNode node, GLSLRedefinedTokenType type) {
            super(node);
            this.type = type;
        }

        @Override
        public <T extends GLSLElement> T findParentByClass(Class<T> clazz) {
            return null;//TODO Impl
        }

        @Override
        public GLSLElement findParentByClasses(Class<? extends GLSLElement>... clazzes) {
            return null;//TODO Impl
        }

        @Override
        public <T extends GLSLElement> T findPrevSiblingByClass(Class<T> clazz) {
            return null;//TODO Impl
        }

        @Override
        public GLSLElement findPrevSiblingByClasses(Class<? extends GLSLElement>... clazzes) {
            return null;//TODO Impl
        }

        @Override
        public boolean isDescendantOf(PsiElement ancestor) {
            return false;//TODO Impl
        }
    }
}
