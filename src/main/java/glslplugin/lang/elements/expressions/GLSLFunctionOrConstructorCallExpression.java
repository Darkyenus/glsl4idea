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
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReferenceBase;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.declarations.GLSLArraySpecifier;
import glslplugin.lang.elements.declarations.GLSLFunctionDeclaration;
import glslplugin.lang.elements.declarations.GLSLStructDefinition;
import glslplugin.lang.elements.declarations.GLSLTypeSpecifier;
import glslplugin.lang.elements.reference.GLSLBuiltInPsiUtilService;
import glslplugin.lang.elements.reference.GLSLReferenceUtil;
import glslplugin.lang.elements.types.GLSLArrayType;
import glslplugin.lang.elements.types.GLSLFunctionType;
import glslplugin.lang.elements.types.GLSLMatrixType;
import glslplugin.lang.elements.types.GLSLScalarType;
import glslplugin.lang.elements.types.GLSLStructType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypeCompatibilityLevel;
import glslplugin.lang.elements.types.GLSLTypes;
import glslplugin.lang.elements.types.GLSLVectorType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

import static com.intellij.util.ArrayUtil.EMPTY_INT_ARRAY;

/**
 * GLSLFunctionOrConstructorCallExpression is a function call expression or a constructor expression.
 * What it really is determines what methods (of this class) are sensible to call.
 * <p>
 * There are three possible valid children states:
 *  TYPE_SPECIFIER                      = Constructor mode of built-in types or their arrays
 *  FUNCTION_NAME ARRAY_DECLARATOR*     = Constructor mode of struct arrays
 *  FUNCTION_NAME                       = Constructor mode of structs or an actual function call, determined by references in scope
 * All modes always followed by LEFT_PAREN parameter list RIGHT_PAREN.
 * (That is, if the tree is syntactically valid.)
 * <p>
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
public class GLSLFunctionOrConstructorCallExpression extends GLSLExpression {
    public GLSLFunctionOrConstructorCallExpression(@NotNull ASTNode astNode) {
        super(astNode);
    }

    //region Tree mining
    //Constructor only
    @Nullable
    private GLSLTypeSpecifier getConstructorTypeSpecifier(){
        return findChildByClass(GLSLTypeSpecifier.class);
    }

    private GLSLArraySpecifier @NotNull [] getConstructorArraySpecifiers(){
        return findChildrenByClass(GLSLArraySpecifier.class);
    }

    private int @NotNull [] getConstructorArrayDimensions() {
        final GLSLArraySpecifier[] arraySpecifiers = getConstructorArraySpecifiers();
        if (arraySpecifiers.length == 0) {
            return EMPTY_INT_ARRAY;
        }
        int[] dimensions = new int[arraySpecifiers.length];
        for (int i = 0; i < arraySpecifiers.length; i++) {
            dimensions[i] = arraySpecifiers[i].getDimensionSize();
        }
        return dimensions;
    }

    //Shared
    @Nullable
    public PsiElement getFunctionOrConstructedTypeNameIdentifier() {
        return findChildByType(GLSLTokenTypes.IDENTIFIER);
    }

    @Nullable
    public GLSLParameterList getParameterList() {
        return findChildByClass(GLSLParameterList.class);
    }

    @NotNull
    public GLSLType[] getParameterTypes(){
        GLSLParameterList parameterList = getParameterList();
        if(parameterList != null)return parameterList.getParameterTypes();
        else return GLSLType.EMPTY_ARRAY;
    }
    //endregion

    //region Shared

    public boolean isConstructor() {
        if(getConstructorTypeSpecifier() != null) return true;
        if(getConstructorArraySpecifiers().length > 0) return true;

        final FunctionCallOrConstructorReference reference = getReference();
        if (reference == null) return false;
        final FunctionCallOrConstructorReference.GLSLResolveResult[] glslResolveResults = reference.multiResolve(false);
        if (glslResolveResults.length != 1) return false;
        // Constructor always resolves into a struct, even if it is a dummy struct
        return glslResolveResults[0].getElement() instanceof GLSLStructDefinition;
    }

    @NotNull
    @Override
    public GLSLType getType() {
        final FunctionCallOrConstructorReference reference = getReference();
        if (reference == null) return GLSLTypes.UNKNOWN_TYPE;
        final FunctionCallOrConstructorReference.GLSLResolveResult[] glslResolveResults = reference.multiResolve(false);
        if (glslResolveResults.length != 1) return GLSLTypes.UNKNOWN_TYPE;
        return glslResolveResults[0].resultType;
    }
    //endregion
    private static void clarifyConstructorArrayDimensions(final int[] dimensions, GLSLParameterList parameterList){
        if(parameterList == null) return;
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

    @Nullable
    public String getFunctionOrConstructedTypeName() {
        PsiElement identifier = getFunctionOrConstructedTypeNameIdentifier();
        return identifier == null ? null : identifier.getText();
    }
    //endregion

    public static class FunctionCallOrConstructorReference
            extends PsiPolyVariantReferenceBase<GLSLFunctionOrConstructorCallExpression>
            implements PsiScopeProcessor {

        public FunctionCallOrConstructorReference(@NotNull GLSLFunctionOrConstructorCallExpression source, TextRange range) {
            super(source, range,false);
        }

        private String onlyNamed = null;
        private final ArrayList<GLSLFunctionDeclaration> functionDeclarations = new ArrayList<>();
        private final ArrayList<GLSLStructDefinition> structDefinitions = new ArrayList<>();

        public static class GLSLResolveResult extends PsiElementResolveResult {

            public static final GLSLResolveResult[] EMPTY_ARRAY = new GLSLResolveResult[0];
            public final GLSLType resultType;

            public GLSLResolveResult(@NotNull PsiElement element, boolean validResult, GLSLType resultType) {
                super(element, validResult);
                this.resultType = resultType;
            }
        }

        @Override
        public GLSLResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
            try {
                final GLSLFunctionOrConstructorCallExpression element = getElement();
                final GLSLTypeSpecifier typeSpec = element.getConstructorTypeSpecifier();
                final String customType = element.getFunctionOrConstructedTypeName();

                if (typeSpec != null) {
                    // Built-in type constructor, it does not matter what the array specifiers are
                    GLSLType type = typeSpec.getType();

                    // NOTE: Built-in types have different handling of array sizes, it is a part of their type specifier
                    // Structs don't have that - their array sizes are separate elements after specifier
                    GLSLType baseType;
                    if (type instanceof GLSLArrayType) {
                        baseType = type.getBaseType();
                    } else {
                        baseType = type;
                    }

                    final GLSLBuiltInPsiUtilService bipus = element.getProject().getService(GLSLBuiltInPsiUtilService.class);
                    final GLSLStructDefinition builtInType;
                    if (baseType instanceof GLSLScalarType) {
                        builtInType = bipus.getScalarDefinition((GLSLScalarType) baseType);
                    } else if (baseType instanceof GLSLVectorType) {
                        builtInType = bipus.getVecDefinition((GLSLVectorType) baseType);
                    } else if (baseType instanceof GLSLMatrixType) {
                        builtInType = bipus.getMatrixDefinition((GLSLMatrixType) baseType);
                    } else {
                        return GLSLResolveResult.EMPTY_ARRAY;
                    }

                    if (type instanceof GLSLArrayType arrayType) {
                        // Array may be implicitly sized: if so, clarify it using parameter list
                        final int[] dimensions = arrayType.getDimensions();
                        final int[] newDimensions = Arrays.copyOf(dimensions, dimensions.length);
                        final GLSLParameterList parameterList = element.getParameterList();
                        clarifyConstructorArrayDimensions(newDimensions, parameterList);
                        type = new GLSLArrayType(type.getBaseType(), newDimensions);
                    }

                    return new GLSLResolveResult[]{
                            new GLSLResolveResult(builtInType, true, type)
                    };
                } else if (customType != null) {
                    // Lookup the struct or function
                    onlyNamed = customType;
                    PsiTreeUtil.treeWalkUp(this, element, null, ResolveState.initial());

                    final ArrayList<GLSLResolveResult> results = new ArrayList<>();

                    final int @NotNull[] constructorArraySpecifiers = element.getConstructorArrayDimensions();
                    if (!functionDeclarations.isEmpty() && constructorArraySpecifiers.length == 0) {
                        // Try to match function
                        boolean gotDirectlyCompatible = false;
                        final @NotNull GLSLType[] parameterTypes = element.getParameterTypes();
                        for (GLSLFunctionDeclaration functionDeclaration : functionDeclarations) {
                            final GLSLFunctionType functionType = functionDeclaration.getType();

                            final GLSLTypeCompatibilityLevel level = functionType.getParameterCompatibilityLevel(parameterTypes);
                            if (level == GLSLTypeCompatibilityLevel.COMPATIBLE_WITH_IMPLICIT_CONVERSION) {
                                if (!gotDirectlyCompatible) {
                                    results.add(new GLSLResolveResult(functionDeclaration, true, functionType.getReturnType()));
                                }
                            } else if (level == GLSLTypeCompatibilityLevel.DIRECTLY_COMPATIBLE) {
                                if (!gotDirectlyCompatible) {
                                    // Everything up to now was not directly compatible, throw it away
                                    results.clear();
                                    gotDirectlyCompatible = true;
                                }
                                results.add(new GLSLResolveResult(functionDeclaration, true, functionType.getReturnType()));
                            }
                        }
                    }

                    clarifyConstructorArrayDimensions(constructorArraySpecifiers, element.getParameterList());
                    for (GLSLStructDefinition definition : structDefinitions) {
                        final GLSLStructType structType = definition.getType();
                        final GLSLType resultType;
                        if (constructorArraySpecifiers.length == 0) {
                            // This is a direct instantiation
                            resultType = structType;
                        } else {
                            // This is an array instantiation
                            resultType = new GLSLArrayType(structType, constructorArraySpecifiers);
                        }
                        results.add(new GLSLResolveResult(definition, true, resultType));
                    }

                    return results.toArray(GLSLResolveResult.EMPTY_ARRAY);
                } else {
                    // Broken
                    return GLSLResolveResult.EMPTY_ARRAY;
                }
            } finally {
                functionDeclarations.clear();
                structDefinitions.clear();
            }
        }

        @Override
        public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state) {
            final String onlyNamed = this.onlyNamed;
            if (element instanceof GLSLFunctionDeclaration dec) {
                if (onlyNamed == null || onlyNamed.equals(dec.getName())) {
                    functionDeclarations.add(dec);
                }
            } else if (element instanceof GLSLStructDefinition def) {
                if (onlyNamed == null || onlyNamed.equals(def.getName())) {
                    structDefinitions.add(def);
                }
            }
            return true;// Continue
        }

        @Override
        public Object @NotNull [] getVariants() {
            try {
                onlyNamed = null;
                PsiTreeUtil.treeWalkUp(this, getElement(), null, ResolveState.initial());

                final ArrayList<Object> variants = new ArrayList<>();

                for (GLSLScalarType scalar : GLSLScalarType.SCALARS) {
                    variants.add(scalar.getTypename());
                }
                for (GLSLVectorType[] sharedBaseType : GLSLVectorType.VECTOR_TYPES.values()) {
                    for (GLSLVectorType type : sharedBaseType) {
                        variants.add(type.getTypename());
                    }
                }
                for (GLSLMatrixType[][] sharedBaseType : GLSLMatrixType.MATRIX_TYPES.values()) {
                    for (GLSLMatrixType[] column : sharedBaseType) {
                        for (GLSLMatrixType type : column) {
                            variants.add(type.getTypename());
                        }
                    }
                }
                variants.addAll(functionDeclarations);
                variants.addAll(structDefinitions);

                return variants.toArray();
            } finally {
                functionDeclarations.clear();
                structDefinitions.clear();
            }
        }
    }

    @Override
    public FunctionCallOrConstructorReference getReference() {
        final TextRange range;
        final GLSLTypeSpecifier constructorType = getConstructorTypeSpecifier();
        if (constructorType != null) {
            range = GLSLReferenceUtil.rangeOfIn(constructorType, this);
        } else {
            final PsiElement identifier = getFunctionOrConstructedTypeNameIdentifier();
            if (identifier != null) {
                range = GLSLReferenceUtil.rangeOfIn(identifier, this);
            } else {
                return null;
            }
        }
        return new FunctionCallOrConstructorReference(this, range);
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        return super.getReferences();
    }

    @Override
    public String toString() {
        if (isConstructor()) {
            return "Constructor call: "+getType().getTypename();
        }else{
            return "Function call: " + getFunctionOrConstructedTypeName();
        }
    }

    @Override
    public String getName() {
        if (isConstructor()) {
            return getType().getTypename();
        } else {
            return getFunctionOrConstructedTypeName();
        }
    }
}
