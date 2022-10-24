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
import com.intellij.psi.util.PsiTreeUtil;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.reference.GLSLReferencableDeclaration;
import glslplugin.lang.elements.types.GLSLBasicFunctionType;
import glslplugin.lang.elements.types.GLSLFunctionType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * GLSLFunctionDeclarationImpl is the psi implementation of a function declaration.
 */
public class GLSLFunctionDeclarationImpl extends GLSLElementImpl implements GLSLQualifiedDeclaration, GLSLFunctionDeclaration, GLSLReferencableDeclaration {
    private GLSLFunctionType typeCache;
    private boolean typeCacheDirty = false;

    public GLSLFunctionDeclarationImpl(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
        if (lastParent != null || !PsiTreeUtil.isAncestor(this, place, false)) {
            // Can't see the function from inside
            return true;
        }

        return processor.execute(this, state);
    }

    @Override
    public @NotNull String declaredNoun() {
        return "function";
    }


    /** @return the element that holds the function name */
    @Nullable
    private PsiElement getFunctionNameIdentifier() {
        return findChildByType(GLSLTokenTypes.IDENTIFIER);
    }

    public @Nullable String getFunctionName() {
        final PsiElement identifier = getFunctionNameIdentifier();
        return identifier == null ? null : identifier.getText();
    }

    @NotNull
    public GLSLParameterDeclaration[] getParameters() {
        return findChildrenByClass(GLSLParameterDeclaration.class);
    }

    @NotNull
    @Override
    public GLSLType getReturnType() {
        GLSLTypeSpecifier typeSpecifier = findChildByClass(GLSLTypeSpecifier.class);
        if(typeSpecifier == null){
            return GLSLTypes.UNKNOWN_TYPE;
        }else{
            return typeSpecifier.getType();
        }
    }

    @NotNull
    public String getSignature() {
        StringBuilder b = new StringBuilder();
        b.append(getName()).append("(");
        boolean first = true;
        for (GLSLParameterDeclaration declarator : getParameters()) {
            if (!first) {
                b.append(",");
            }
            first = false;
            b.append(declarator.getTypeSpecifierNodeTypeName());
        }
        b.append(") : ");
        b.append(getTypeSpecifierNodeTypeName());
        return b.toString();
    }

    @Override
    public String toString() {
        return "Function Declaration: " + getSignature();
    }

    @NotNull
    public GLSLFunctionType getType() {
        if (typeCache == null || typeCacheDirty) {
            typeCache = createType();
            typeCacheDirty = false;
        }
        return typeCache;
    }

    private GLSLFunctionType createType() {
        final String functionName = getFunctionName();
        final GLSLParameterDeclaration[] parameterDeclarations = getParameters();
        final GLSLType[] parameterTypes = new GLSLType[parameterDeclarations.length];
        for (int i = 0; i < parameterDeclarations.length; i++) {
            GLSLDeclarator declarator = parameterDeclarations[i].getDeclarator();
            if(declarator == null){
                parameterTypes[i] = GLSLTypes.UNKNOWN_TYPE;
            }else{
                parameterTypes[i] = declarator.getType();
            }
        }
        return new GLSLBasicFunctionType(this, functionName == null ? "<no name>" : functionName, getReturnType(), parameterTypes);
    }

    @NotNull
    @Override
    public String getDeclarationDescription() {
        return "function";
    }

    @Nullable
    @Override
    public String getName() {
        return getFunctionName();
    }

    @Override
    public int getTextOffset() {
        final PsiElement identifier = getFunctionNameIdentifier();
        return identifier != null ? identifier.getTextOffset() : super.getTextOffset();
    }

    @Override
    public @Nullable PsiElement getNameIdentifier() {
        return getFunctionNameIdentifier();
    }

    @Override
    public void subtreeChanged() {
        super.subtreeChanged();
        typeCacheDirty = true;
    }


    @Override
    public <T> @Nullable T findChildByClass(Class<T> aClass) {
        return super.findChildByClass(aClass);
    }
}
