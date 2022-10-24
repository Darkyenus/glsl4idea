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
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.expressions.GLSLExpression;
import glslplugin.lang.elements.reference.GLSLReferencableDeclaration;
import glslplugin.lang.elements.statements.GLSLCompoundStatement;
import glslplugin.lang.elements.types.GLSLArrayType;
import glslplugin.lang.elements.types.GLSLQualifiedType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * GLSLDeclarator represents a local or global variable declaration.
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 29, 2009
 *         Time: 7:29:46 PM
 */
public class GLSLDeclarator extends GLSLElementImpl implements GLSLReferencableDeclaration {
    public static final GLSLDeclarator[] NO_DECLARATORS = new GLSLDeclarator[0];

    public GLSLDeclarator(@NotNull ASTNode astNode) {
        super(astNode);
    }


    /** @return the element that holds the function name */
    @Nullable
    private PsiElement getVariableNameIdentifier() {
        return findChildByType(GLSLTokenTypes.IDENTIFIER);
    }

    public @Nullable String getVariableName() {
        final PsiElement identifier = getVariableNameIdentifier();
        return identifier == null ? null : identifier.getText();
    }

    @Nullable
    @Override
    public String getName() {
        return getVariableName();
    }

    @Override
    public @Nullable PsiElement getNameIdentifier() {
        return getVariableNameIdentifier();
    }

    @Override
    public int getTextOffset() {
        final PsiElement identifier = getVariableNameIdentifier();
        return identifier != null ? identifier.getTextOffset() : super.getTextOffset();
    }

    @Nullable
    public GLSLExpression getInitializerExpression() {
        final GLSLInitializerExpression init = findChildByClass(GLSLInitializerExpression.class);
        if (init != null) {
            return init.getInitializerExpression();
        } else {
            return null;
        }
    }

    @Nullable
    public GLSLInitializer getInitializer(){
        return findChildByClass(GLSLInitializer.class);
    }

    @Nullable
    public GLSLQualifiedDeclaration getParentDeclaration() {
        return findParentByClass(GLSLQualifiedDeclaration.class);
    }

    /**
     * Clarifies array dimensions if this defines implicitly sized array by initializer.
     * Does nothing otherwise.
     */
    private GLSLType clarifyArrayType(GLSLType baseType){
        if(!(baseType instanceof final GLSLArrayType myArrayType)) {
            return baseType; //No need to clarify non-array types
        }
        final int[] myDimensions = myArrayType.getDimensions();

        {   //Try clarifying using initializer list
            GLSLInitializer rawInitializer = getInitializer();
            if (rawInitializer instanceof GLSLInitializerList initializerList) {
                //Clarify using initializer list
                for (int i = 0; i < myDimensions.length; i++) {
                    GLSLInitializer[] initializers = initializerList.getInitializers();
                    if (myDimensions[i] == GLSLArrayType.UNDEFINED_SIZE_DIMENSION) {
                        //Can clarify that!
                        myDimensions[i] = initializers.length;
                    }
                    if (initializers.length >= 1 && initializers[0] instanceof GLSLInitializerList) {
                        initializerList = (GLSLInitializerList) initializers[0];
                    } else {
                        //Can't clarify any more
                        break;
                    }
                }
                return baseType;//Dimensions are changed in place, so no need to create new instance
            }
        }

        {   //Try clarifying using expression
            GLSLExpression rawExpression = getInitializerExpression();
            if(rawExpression != null){
                GLSLType type = rawExpression.getType();
                if(type instanceof GLSLArrayType arrayType){
                    //Great, it is being correctly initialized, try to copy as many missing dimensions as we can
                    final int[] dimensions = arrayType.getDimensions();
                    for (int i = 0; i < myDimensions.length && i < dimensions.length; i++) {
                        if(myDimensions[i] == GLSLArrayType.UNDEFINED_SIZE_DIMENSION){
                            //Copy that
                            myDimensions[i] = dimensions[i];
                        }
                    }
                    return baseType; //Dimensions are changed in place, so no need to create new instance
                }//else - not even valid initializer
            }
        }

        return baseType; //Could not clarify
    }

    @NotNull
    public GLSLType getType() {
        GLSLQualifiedDeclaration declaration = getParentDeclaration();
        if(declaration == null)return GLSLTypes.UNKNOWN_TYPE;
        GLSLTypeSpecifier declarationType = declaration.getTypeSpecifierNode();
        if(declarationType == null)return GLSLTypes.UNKNOWN_TYPE;

        GLSLType declaredType = declarationType.getType();
        if(!declaredType.isValidType())return GLSLTypes.UNKNOWN_TYPE;

        GLSLArraySpecifier[] arraySpecifiers = findChildrenByClass(GLSLArraySpecifier.class);
        if(arraySpecifiers.length == 0){
            return clarifyArrayType(declaredType);
        }else{
            //Must _prepend_ some dimensions to the type
            //In: "vec4[2][4] b[3]" b is "vec4[3][2][4]", not "vec4[2][4][3]"
            if(declaredType instanceof GLSLArrayType declaredArrayType){
                //Already an array, must append the dimensions
                int[] existingDimensions = declaredArrayType.getDimensions();
                int[] combinedDimensions = new int[existingDimensions.length + arraySpecifiers.length];
                System.arraycopy(existingDimensions, 0, combinedDimensions, arraySpecifiers.length, existingDimensions.length);
                for (int i = 0; i < arraySpecifiers.length; i++) {
                    combinedDimensions[i] = arraySpecifiers[i].getDimensionSize();
                }
                return clarifyArrayType(new GLSLArrayType(declaredArrayType.getBaseType(), combinedDimensions));
            }else{
                int[] dimensions = new int[arraySpecifiers.length];
                for (int i = 0; i < dimensions.length; i++) {
                    dimensions[i] = arraySpecifiers[i].getDimensionSize();
                }
                return clarifyArrayType(new GLSLArrayType(declaredType, dimensions));
            }
        }
    }

    @NotNull
    public GLSLQualifiedType getQualifiedType() {
        final GLSLType type = getType();
        final GLSLQualifiedDeclaration declaration = getParentDeclaration();
        if(declaration == null || declaration.getQualifierList() == null)return new GLSLQualifiedType(type);
        return new GLSLQualifiedType(type, declaration.getQualifierList().getQualifiers());
    }

    @Override
    public boolean processDeclarations(@NotNull PsiScopeProcessor processor, @NotNull ResolveState state, PsiElement lastParent, @NotNull PsiElement place) {
        if (PsiTreeUtil.isAncestor(this,place, false)) {
            return true;
        }
        return processor.execute(this, state);
    }

    @Override
    public @NotNull String declaredNoun() {
        if (findParentByClass(GLSLParameterDeclaration.class) != null) {
            return "function parameter";
        }
        if (findParentByClass(GLSLFunctionDefinition.class) != null) {
            return "variable";
        }
        return "global variable";
    }

    @Override
    public @NotNull SearchScope getUseScope() {
        final GLSLCompoundStatement parentCompound = findParentByClass(GLSLCompoundStatement.class);
        if (parentCompound != null) {
            // Covers local variables
            return new LocalSearchScope(parentCompound);
        }
        final GLSLFunctionDeclaration functionDeclaration = findParentByClass(GLSLFunctionDeclaration.class);
        if (functionDeclaration != null) {
            // Covers function parameters
            return new LocalSearchScope(functionDeclaration);
        }

        return super.getUseScope();
    }

    @Override
    public String toString() {
        return "Declarator: " + getName() + " : " + getType().getTypename();
    }

    public static @NotNull String toString(GLSLDeclarator[] declarators) {
        StringBuilder b = new StringBuilder();
        boolean first = true;
        for (GLSLDeclarator declarator : declarators) {
            if (!first) {
                b.append(", ");
            }
            String identifier = declarator.getVariableName();
            if (identifier == null){
                b.append("(unknown)");
            } else {
                b.append(identifier);
            }
            first = false;
        }
        return b.toString();
    }
}
