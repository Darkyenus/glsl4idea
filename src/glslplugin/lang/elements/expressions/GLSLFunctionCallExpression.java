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

package glslplugin.lang.elements.expressions;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLIdentifier;
import glslplugin.lang.elements.GLSLReferenceElement;
import glslplugin.lang.elements.declarations.*;
import glslplugin.lang.elements.reference.GLSLConstructorReference;
import glslplugin.lang.elements.reference.GLSLFunctionReference;
import glslplugin.lang.elements.reference.GLSLReferenceBase;
import glslplugin.lang.elements.reference.GLSLTypeReference;
import glslplugin.lang.elements.types.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * GLSLFunctionCallExpression is a function call expression or a constructor expression.
 * What it really is determines what methods (of this class) are sensible to call.
 *
 * There are three possible valid children states:
 *  TYPE_SPECIFIER                      = Constructor mode of built-in types or their arrays
 *  FUNCTION_NAME ARRAY_DECLARATOR*     = Constructor mode of struct arrays
 *  FUNCTION_NAME                       = Constructor mode of structs or an actual function call, determined by references in scope
 * All modes always followed by LEFT_PAREN parameter list RIGHT_PAREN.
 * (That is, if the tree is syntactically valid.)
 *
 * Examples:
 * int A = int(4);
 * int[] B = int[3](1,2,3);
 * MyVec C = MyVec(3.5,6.7);
 * MyVec[] D = MyVec[2]( MyVec(1,2), MyVec(3,4) );
 * bool E = randomBoolean();
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 29, 2009
 *         Time: 10:34:04 AM
 */
public class GLSLFunctionCallExpression extends GLSLExpression implements GLSLReferenceElement {
    public GLSLFunctionCallExpression(@NotNull ASTNode astNode) {
        super(astNode);
    }

    //region Tree mining
    //Constructor only
    @Nullable
    private GLSLTypeSpecifier getConstructorTypeSpecifier(){
        return findChildByClass(GLSLTypeSpecifier.class);
    }

    @NotNull
    private GLSLArraySpecifier[] getConstructorArraySpecifiers(){
        return findChildrenByClass(GLSLArraySpecifier.class);
    }

    //Shared
    @Nullable
    public GLSLIdentifier getIdentifier() {
        return findChildByClass(GLSLIdentifier.class);
    }

    @Nullable
    public GLSLParameterList getParameterList() {
        return findChildByClass(GLSLParameterList.class);
    }

    @NotNull
    public GLSLType[] getParameterTypes(){
        GLSLParameterList parameterList = getParameterList();
        if (parameterList != null) return parameterList.getParameterTypes();
        else {
            return GLSLType.EMPTY_ARRAY;
        }
    }
    //endregion

    //region Shared
    private boolean isStructReference(GLSLIdentifier identifier){
        return findDefinedStruct(identifier) != null;
    }

    public boolean isConstructor(){
        if (getConstructorTypeSpecifier() != null) {
            return true;
        }
        if (getConstructorArraySpecifiers().length > 0) {
            return true;
        }
        GLSLIdentifier identifier = getIdentifier();
        if (identifier == null) {
            return false; // Default to function call in the case of malformed tree
        }
        return isStructReference(identifier);
    }

    @NotNull
    private GLSLType getTypeOfStructConstructor(GLSLIdentifier identifier, GLSLArraySpecifier[] arraySpecifiers) {
        GLSLStructType structType = findDefinedStruct(identifier);
        if (structType == null) {
            return GLSLTypes.UNKNOWN_TYPE;
        }
        if (arraySpecifiers.length == 0) {
            return structType;
        } else {
            int[] dimensions = new int[arraySpecifiers.length];
            for (int i = 0; i < arraySpecifiers.length; i++) {
                dimensions[i] = arraySpecifiers[i].getDimensionSize();
            }
            clarifyConstructorArrayDimensions(dimensions);
            return new GLSLArrayType(structType, dimensions);
        }
    }

    private GLSLType getTypeOfFunctionCall(GLSLIdentifier identifier, GLSLType[] parameterTypes){
        List<GLSLFunctionType> functionTypes = findDefinedFunctions(identifier.getName(), parameterTypes);
        if (functionTypes.size() == 1) {
            return functionTypes.get(0).getReturnType();
        } else {
            //Can't resolve with certainty
            return GLSLTypes.UNKNOWN_TYPE;
        }
    }

    @NotNull
    @Override
    public GLSLType getType() {
        { //If name is built in type, resolve as constructor
            GLSLTypeSpecifier constructorTypeSpecifier = getConstructorTypeSpecifier();
            if (constructorTypeSpecifier != null) {
                GLSLType constructorType = constructorTypeSpecifier.getType();
                //NOTE: Built-in types have different handling of array sizes, it is a part of their type specifier
                // Structs don't have that - their array sizes are separate elements after specifier
                if (constructorType instanceof GLSLArrayType) {
                    //That array may be implicitly sized, if so - clarify it using parameter list
                    GLSLArrayType arrayType = (GLSLArrayType) constructorType;
                    final int[] dimensions = arrayType.getDimensions();
                    clarifyConstructorArrayDimensions(dimensions);
                }
                return constructorType;
            }
        }

        { //Not a built in type, both possibilities still open
            GLSLIdentifier identifier = getIdentifier();
            if (identifier == null) {
                return GLSLTypes.UNKNOWN_TYPE; //Can't deduce type
            }
            GLSLArraySpecifier[] constructorArraySpecifiers = getConstructorArraySpecifiers();
            if (constructorArraySpecifiers.length > 0 || isStructReference(identifier)) {
                //It has some array specifiers or is struct type, it has to be constructor
                return getTypeOfStructConstructor(identifier, constructorArraySpecifiers);
            } else {
                return getTypeOfFunctionCall(identifier, getParameterTypes());
            }
        }
    }
    //endregion

    //region Struct only
    @Nullable
    private GLSLStructType findDefinedStruct(GLSLIdentifier identifier){
        final GLSLStructDefinition typeDefinition = GLSLTypeReference.findTypeDefinition(identifier, identifier.getName());
        if (typeDefinition == null) {
            return null;
        }
        return typeDefinition.getType();
    }

    private void clarifyConstructorArrayDimensions(final int[] dimensions){
        final GLSLParameterList parameterList = getParameterList();
        if (parameterList == null) {
            return;
        }
        if (dimensions.length >= 1) {
            if (dimensions[0] == GLSLArrayType.UNDEFINED_SIZE_DIMENSION) {
                dimensions[0] = parameterList.getParameters().length;
            }
        }
        for (int i = 1; i < dimensions.length; i++) {
            if (dimensions[i] == GLSLArrayType.UNDEFINED_SIZE_DIMENSION) {
                //Clarify further
                //TODO
            }
        }
    }
    //endregion

    //region Function only
    @NotNull
    private List<GLSLFunctionType> findDefinedFunctions(String name, GLSLType[] parameterTypes){
        ArrayList<GLSLFunctionType> result = new ArrayList<>();
        PsiElement current = findParentByClass(GLSLFunctionDefinition.class);
        while (current != null) {
            if (current instanceof GLSLFunctionDeclaration) {
                GLSLFunctionType functionType = ((GLSLFunctionDeclaration) current).getType();
                if (name.equals(functionType.getName())) {
                    switch (functionType.getParameterCompatibilityLevel(parameterTypes)) {
                        case COMPATIBLE_WITH_IMPLICIT_CONVERSION:
                            result.add(functionType);
                            break;
                        case DIRECTLY_COMPATIBLE:
                            result.clear();
                            result.add(functionType);
                            return result;
                        case INCOMPATIBLE:
                            break;
                        default:
                            assert false : "Unsupported compatibility level.";
                    }
                }
            }
            current = current.getPrevSibling();
        }
        return result;
    }

    @NotNull
    public String getFunctionName() {
        GLSLIdentifier identifier = getIdentifier();
        if (identifier != null) {
            return identifier.getName();
        }
        return "(unknown)";
    }

    /**
     * @return All function types this call can possibly call
     */
    @NotNull
    public List<GLSLFunctionType> getPossibleCalledFunctions() {
        final GLSLIdentifier identifier = getIdentifier();
        if (identifier == null) {
            return Collections.emptyList();
        }
        return findDefinedFunctions(identifier.getText(), getParameterTypes());
    }

    /**
     * @return Called function type or null if ambiguous
     */
    @Nullable
    public GLSLFunctionType getCalledFunctionType(){
        final List<GLSLFunctionType> possibilities = getPossibleCalledFunctions();
        if (possibilities.size() == 1) {
            return possibilities.get(0);
        } else {
            return null;
        }
    }

    @NotNull
    public GLSLReferenceBase<GLSLIdentifier, ? extends GLSLElement> getReferenceProxy() {
        if (isConstructor()) {
            return new GLSLConstructorReference(this);
        } else {
            return new GLSLFunctionReference(this);
        }
    }
    //endregion

    @Override
    public String toString() {
        if (isConstructor()) {
            return "Constructor call: " + getType().getTypename();
        } else {
            return "Function call: " + getFunctionName();
        }
    }

    @Override
    public String getName() {
        if (isConstructor()) {
            return getType().getTypename();
        } else {
            return getFunctionName();
        }
    }
}
