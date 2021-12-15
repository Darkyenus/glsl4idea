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

package glslplugin.lang.elements.declarations;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.GLSLIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base for all elements, which declare something named.
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 2, 2009
 *         Time: 10:33:30 AM
 */
public abstract class GLSLDeclarationImpl extends GLSLElementImpl implements GLSLDeclaration {
    public GLSLDeclarationImpl(@NotNull ASTNode astNode) {
        super(astNode);
    }

    private static final GLSLQualifier[] NO_QUALIFIERS = new GLSLQualifier[0];
    @NotNull
    public GLSLQualifier[] getQualifiers() {
        final GLSLQualifierList qualifierList = getQualifierList();
        if(qualifierList == null){
            return NO_QUALIFIERS;
        }else{
            return qualifierList.getQualifiers();
        }
    }

    /**
     * Returns the type specifier Psi element for this declaration.
     * <p/>
     * <b>WARNING!</b>
     * <p/>
     * This is not a complete type of any variable as a declarator may contain an array specifier.
     * For the complete type use {@link glslplugin.lang.elements.declarations.GLSLDeclarator#getType()} instead.
     *
     * @return the type specifier or null if declaration is malformed
     */
    @Nullable
    public GLSLTypeSpecifier getTypeSpecifierNode() {
        return findChildByClass(GLSLTypeSpecifier.class);
    }

    /**
     * Returns "(unknown)" if getTypeSpecifierNode() returns null
     * @return result of getTypeSpecifierNode().getTypeName()
     */
    @NotNull
    public String getTypeSpecifierNodeTypeName(){
        GLSLTypeSpecifier specifier = getTypeSpecifierNode();
        if(specifier != null){
            return specifier.getTypeName();
        }else {
            return "(unknown)";
        }
    }

    @Nullable
    public GLSLQualifierList getQualifierList() {
        return findChildByClass(GLSLQualifierList.class);
    }

    @NotNull
    public GLSLDeclarator[] getDeclarators() {
        final GLSLDeclaratorList list = findChildByClass(GLSLDeclaratorList.class);
        if(list == null)return GLSLDeclarator.NO_DECLARATORS;
        else return list.getDeclarators();
    }

    @NotNull
    protected String getDeclaratorsString() {
        StringBuilder b = new StringBuilder();
        boolean first = true;
        for (GLSLDeclarator declarator : getDeclarators()) {
            if (!first) {
                b.append(", ");
            }
            GLSLIdentifier identifier = declarator.getNameIdentifier();
            if(identifier == null){
                b.append("(unknown)");
            } else {
                b.append(identifier.getName());
            }
            first = false;
        }
        return b.toString();
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, @Nullable PsiElement lastParent, @NotNull PsiElement place) {
        for (GLSLDeclarator declarator : getDeclarators()) {
            if (declarator == lastParent)
                continue;
            if (!processor.execute(declarator, state))
                return false;
        }

        GLSLTypeSpecifier specifier = getTypeSpecifierNode();
        return specifier == null || lastParent == specifier || specifier.processDeclarations(processor, state, lastParent, place);
    }
}
