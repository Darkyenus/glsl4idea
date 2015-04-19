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

import glslplugin.lang.elements.types.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * GLSLOperator is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Mar 8, 2009
 *         Time: 12:38:52 PM
 */
public enum GLSLOperator {
    // BINARY OPERATORS
    // Arithmetic operators
    ADDITION("+", getBasicArithmeticOperatorAlternatives("+")),
    SUBTRACTION("-", getBasicArithmeticOperatorAlternatives("-")),
    MULTIPLICATION("*", getMultiplicationOperatorAlternatives()),
    DIVISION("/", getBasicArithmeticOperatorAlternatives("/")),

    // Assignment operators
    ASSIGN("=", new AssignmentTypeAlternativesDelegate()),

    ADDITION_ASSIGN("+=", getAssignmentTypeAlternatives("+=", getBasicArithmeticOperatorAlternatives("+"))),
    SUBTRACTION_ASSIGN("-=", getAssignmentTypeAlternatives("-=", getBasicArithmeticOperatorAlternatives("-"))),
    MULTIPLICATION_ASSIGN("*=", getAssignmentTypeAlternatives("*=", getMultiplicationOperatorAlternatives())),
    DIVISION_ASSIGN("/=", getAssignmentTypeAlternatives("/=", getBasicArithmeticOperatorAlternatives("/"))),

    // Logical operators
    LOGIC_AND("&&", getLogicalOperatorAlternatives("&&")),
    LOGIC_OR("||", getLogicalOperatorAlternatives("||")),
    LOGIC_XOR("^^", getLogicalOperatorAlternatives("^^")),
    // BINARY_AND,
    // BINARY_OR,
    // BINARY_XOR,

    // Relational operators
    GREATER(">", getComparisonOperatorAlternatives(">")),
    LESSER("<", getComparisonOperatorAlternatives("<")),
    GREATER_EQUAL(">=", getComparisonOperatorAlternatives(">=")),
    LESSER_EQUAL("<=", getComparisonOperatorAlternatives("<=")),

    // Equality operators
    EQUAL("==", new EqualityTypeAlternativesDelegate("==")),
    NOT_EQUAL("!=", new EqualityTypeAlternativesDelegate("!=")),

    // UNARY OPERATORS
    PLUS("+", getUnaryArithmeticOperatorAlternatives("+")),
    MINUS("-", getUnaryArithmeticOperatorAlternatives("-")),
    INCREMENT("++", getIncrementOperatorAlternatives("++")),
    DECREMENT("--", getIncrementOperatorAlternatives("--")),
    LOGIC_NEGATION("!", new GLSLBasicFunctionType("!", GLSLTypes.BOOL, GLSLTypes.BOOL)),
    // BINARY_NEGATION,

    MEMBER("."),;


    private final String textRepresentation;
    private FunctionTypeAlternativesDelegate delegate;

    GLSLOperator(String text, GLSLFunctionType... functionTypeAlternatives) {
        this(text, new FunctionTypeAlternativesDelegateImpl(functionTypeAlternatives));
    }

    GLSLOperator(String textRepresentation, FunctionTypeAlternativesDelegate delegate) {
        this.textRepresentation = textRepresentation;
        this.delegate = delegate;
    }

    public String getTextRepresentation() {
        return textRepresentation;
    }

    GLSLFunctionType[] getFunctionTypeAlternatives(GLSLType[] types) {
        return delegate.getFunctionTypeAlternatives(types);
    }

    // This is the list of alternatives for +-/, * is special due to the vector/matrix multiplication
    private static GLSLFunctionType[] getBasicArithmeticOperatorAlternatives(String name) {
        List<GLSLFunctionType> dest = new ArrayList<GLSLFunctionType>();
        getCommonArithmeticOperatorAlternatives(name, dest);
        for (int cols = 2; cols <= 4; cols++) {
            for (int rows = 2; rows <= 4; rows++) {
                GLSLMatrixType mat = GLSLMatrixType.getType(cols, rows);
                dest.add(new GLSLBasicFunctionType(name, mat, mat, mat));
            }
        }
        return dest.toArray(new GLSLFunctionType[dest.size()]);
    }

    private static GLSLFunctionType[] getMultiplicationOperatorAlternatives() {
        List<GLSLFunctionType> dest = new ArrayList<GLSLFunctionType>();
        getCommonArithmeticOperatorAlternatives("*", dest);
        // matrix-matrix AND matrix-vector
        for (int M = 2; M <= 4; M++) {
            for (int N = 2; N <= 4; N++) {
                GLSLMatrixType matMxN = GLSLMatrixType.getType(M, N);
                GLSLVectorType vecM = GLSLVectorType.getType(M, GLSLTypes.FLOAT);
                GLSLVectorType vecN = GLSLVectorType.getType(N, GLSLTypes.FLOAT);

                dest.add(new GLSLBasicFunctionType("*", vecM, matMxN, vecN));
                dest.add(new GLSLBasicFunctionType("*", vecN, vecM, matMxN));

                for (int I = 2; I <= 4; I++) {
                    GLSLMatrixType matIxM = GLSLMatrixType.getType(I, M);
                    GLSLMatrixType matIxN = GLSLMatrixType.getType(I, N);

                    dest.add(new GLSLBasicFunctionType("*", matIxN, matMxN, matIxM));
                }
            }
        }
        return dest.toArray(new GLSLFunctionType[dest.size()]);
    }

    private static void getCommonArithmeticOperatorAlternatives(String name, List<GLSLFunctionType> dest) {
        // Scalars
        dest.add(new GLSLBasicFunctionType(name, GLSLTypes.INT, GLSLTypes.INT, GLSLTypes.INT));
        dest.add(new GLSLBasicFunctionType(name, GLSLTypes.FLOAT, GLSLTypes.FLOAT, GLSLTypes.FLOAT));

        // Scalar-vector and vector-vector
        for (int i = 2; i <= 4; i++) {
            GLSLType vec = GLSLVectorType.getType(i, GLSLTypes.FLOAT);
            GLSLType ivec = GLSLVectorType.getType(i, GLSLTypes.INT);
            dest.add(new GLSLBasicFunctionType(name, ivec, GLSLTypes.INT, ivec));
            dest.add(new GLSLBasicFunctionType(name, vec, GLSLTypes.INT, vec));
            dest.add(new GLSLBasicFunctionType(name, ivec, ivec, GLSLTypes.INT));
            dest.add(new GLSLBasicFunctionType(name, vec, vec, GLSLTypes.INT));
            dest.add(new GLSLBasicFunctionType(name, vec, GLSLTypes.FLOAT, ivec));
            dest.add(new GLSLBasicFunctionType(name, vec, GLSLTypes.FLOAT, vec));
            dest.add(new GLSLBasicFunctionType(name, vec, ivec, GLSLTypes.FLOAT));
            dest.add(new GLSLBasicFunctionType(name, vec, vec, GLSLTypes.FLOAT));
            dest.add(new GLSLBasicFunctionType(name, vec, vec, vec));
            dest.add(new GLSLBasicFunctionType(name, ivec, ivec, ivec));
        }
    }

    private static GLSLFunctionType[] getComparisonOperatorAlternatives(String name) {
        return new GLSLFunctionType[]{
                new GLSLBasicFunctionType(name, GLSLTypes.BOOL, GLSLTypes.INT, GLSLTypes.INT),
                new GLSLBasicFunctionType(name, GLSLTypes.BOOL, GLSLTypes.FLOAT, GLSLTypes.FLOAT)
        };
    }

    private static GLSLFunctionType[] getLogicalOperatorAlternatives(String name) {
        return new GLSLFunctionType[]{
                new GLSLBasicFunctionType(name, GLSLTypes.BOOL, GLSLTypes.BOOL, GLSLTypes.BOOL),
        };
    }

    private static GLSLFunctionType[] getUnaryArithmeticOperatorAlternatives(String name) {
        List<GLSLFunctionType> dest = new ArrayList<GLSLFunctionType>();
        dest.add(new GLSLBasicFunctionType(name, GLSLTypes.INT, GLSLTypes.INT));
        dest.add(new GLSLBasicFunctionType(name, GLSLTypes.FLOAT, GLSLTypes.FLOAT));
        for (int i = 2; i <= 4; i++) {
            GLSLType vec = GLSLVectorType.getType(i, GLSLTypes.FLOAT);
            GLSLType ivec = GLSLVectorType.getType(i, GLSLTypes.INT);
            dest.add(new GLSLBasicFunctionType(name, vec, vec));
            dest.add(new GLSLBasicFunctionType(name, ivec, ivec));
            for (int j = 2; j <= 4; j++) {
                GLSLType mat = GLSLMatrixType.getType(i, j);
                dest.add(new GLSLBasicFunctionType(name, mat, mat));
            }
        }
        return dest.toArray(new GLSLFunctionType[dest.size()]);
    }

    private static GLSLFunctionType[] getIncrementOperatorAlternatives(String name) {
        return new GLSLFunctionType[]{
                new GLSLBasicFunctionType(name, GLSLTypes.INT, GLSLTypes.INT),
                new GLSLBasicFunctionType(name, GLSLTypes.FLOAT, GLSLTypes.FLOAT)
        };
    }

    /**
     * Converts a set of alternatives of an operator to the set of the corresponding assignment operator.
     * <p/>
     * For all alternatives, <code>(a,b):c</code>, in the input where <code>c</code>
     * is implicitly convertible to <code>a</code> we get an assignment operator with the following signature:
     * <pre>
     * (a,b):a
     * </pre>
     * <p/>
     * For example: '*' has an alternative (vec4,mat4):vec4 which becomes a '*=' operator with signature (vec4,mat4):vec4
     *
     * @param name                   the name of the operator
     * @param arithmeticAlternatives the alternatives of the arithmetic operator
     * @return the alternatives for the resulting assignemnt operator.
     */
    private static GLSLFunctionType[] getAssignmentTypeAlternatives(String name, GLSLFunctionType[] arithmeticAlternatives) {
        List<GLSLFunctionType> dest = new ArrayList<GLSLFunctionType>(arithmeticAlternatives.length);
        for (GLSLFunctionType alternative : arithmeticAlternatives) {
            if (alternative instanceof GLSLBasicFunctionType) {
                GLSLBasicFunctionType basicAlternative = (GLSLBasicFunctionType) alternative;

                assert basicAlternative.getParameterTypes().length == 2;

                GLSLType returnType = basicAlternative.getBaseType();
                GLSLType leftType = basicAlternative.getParameterTypes()[0];
                GLSLType rightType = basicAlternative.getParameterTypes()[1];

                if (returnType.isConvertibleTo(leftType)) {
                    dest.add(new GLSLBasicFunctionType(name, leftType, leftType, rightType));
                }
            }
        }
        return dest.toArray(new GLSLFunctionType[dest.size()]);
    }


    /////////////////////////////////////////////////////////////////////////
    // The delegates and their implementations.
    // This is needed because it is not possible to subclass an enum
    // Alternatively this can be implemented as a class hierarchy.
    /////////////////////////////////////////////////////////////////////////
    private interface FunctionTypeAlternativesDelegate {
        GLSLFunctionType[] getFunctionTypeAlternatives(GLSLType[] types);
    }

    /**
     * The basic implementations which just chooses suitable alternative from a list.
     */
    private static class FunctionTypeAlternativesDelegateImpl implements FunctionTypeAlternativesDelegate {
        @NotNull
        GLSLFunctionType[] alternatives;

        private FunctionTypeAlternativesDelegateImpl(@NotNull GLSLFunctionType[] alternatives) {
            this.alternatives = alternatives;
        }

        public
        @NotNull
        GLSLFunctionType[] getFunctionTypeAlternatives(@NotNull GLSLType[] types) {
            return GLSLFunctionType.findApplicableTypes(alternatives, types);
        }
    }

    /**
     * Equality operators are on the form: (type,type):bool
     */
    private static class EqualityTypeAlternativesDelegate implements FunctionTypeAlternativesDelegate {
        private final String name;

        public EqualityTypeAlternativesDelegate(String name) {
            this.name = name;
        }

        public GLSLFunctionType[] getFunctionTypeAlternatives(GLSLType[] types) {
            assert types.length == 2;
            GLSLType type;
            if (types[0].isConvertibleTo(types[1])) {
                type = types[1];
            } else if (types[1].isConvertibleTo(types[0])) {
                type = types[0];
            } else {
                return GLSLFunctionType.EMPTY_ARRAY;
            }
            return new GLSLFunctionType[]{new GLSLBasicFunctionType(name, GLSLTypes.BOOL, type, type)};
        }
    }

    /**
     * Assignment (only =) operators are on the form: (type,type):type
     * and is is the left operand which determines the type.
     */
    private static class AssignmentTypeAlternativesDelegate implements FunctionTypeAlternativesDelegate {
        public GLSLFunctionType[] getFunctionTypeAlternatives(GLSLType[] types) {
            assert types.length == 2;
            GLSLType type = types[0];
            return new GLSLFunctionType[]{new GLSLBasicFunctionType("=", type, type, type)};
        }
    }
}
