package glslplugin.lang.elements.expressions.operator;

import glslplugin.lang.elements.types.*;
import org.jetbrains.annotations.NotNull;
import static glslplugin.lang.elements.types.GLSLTypes.*;

/**
 * Base class for all operators.
 *
 * Contains prototypes for all operator types and their specific implementations.
 *
 * @author Darkyen
 */
public class GLSLOperator {

    private final String textRepresentation;

    public GLSLOperator(@NotNull String textRepresentation) {
        this.textRepresentation = textRepresentation;
    }

    @NotNull
    public final String getTextRepresentation(){
        return textRepresentation;
    }

    //region Prototypes

    /**
     * Unary operator - takes one input and has one output
     * <br>
     * For example: "-5"
     */
    public static abstract class GLSLUnaryOperator extends GLSLOperator {

        public GLSLUnaryOperator(@NotNull String textRepresentation) {
            super(textRepresentation);
        }

        public abstract boolean isValidInput(GLSLType type);
        @NotNull
        public abstract GLSLType getResultType(GLSLType input);
    }

    /**
     * Binary operator - takes two inputs and produces one output
     * <br>
     * For example: "3 + 4"
     */
    public static abstract class GLSLBinaryOperator extends GLSLOperator {

        public GLSLBinaryOperator(@NotNull String textRepresentation) {
            super(textRepresentation);
        }

        public boolean isValidInput(GLSLType firstType, GLSLType secondType){
            //If the types are invalid, input is valid (this is not the greatest problem right now)
            if(!firstType.isValidType() || !secondType.isValidType())return true;
            //When types are valid, input is valid if the result is valid
            return getResultType(firstType, secondType).isValidType();
        }

        @NotNull
        public abstract GLSLType getResultType(GLSLType firstInput, GLSLType secondInput);
    }

    /**
     * Assignment operator - takes the value on the right and somehow assigns it to the value on the left
     * Result type is always of the value on the left. Expression on the left must be L type.
     * <br>
     * For example: "i += 1"
     */
    public static class GLSLAssignmentOperator extends GLSLOperator {

        public GLSLAssignmentOperator(@NotNull String textRepresentation) {
            super(textRepresentation);
        }

        public boolean isValidInput(GLSLType leftType, GLSLType rightType) {
            //If the types are invalid, input is valid (this is not the greatest problem right now)
            if (!leftType.isValidType() || !rightType.isValidType()) return true;

            return leftType == rightType || rightType.isConvertibleTo(leftType);
        }
    }

    //endregion

    //region Implementations

    /**
     * Arithmetic binary operator like + - and / as defined by GLSL spec 4.30.6 5.9
     *
     * Note that * has its own subclass
     */
    protected static class ArithmeticBinaryOperator extends GLSLBinaryOperator {

        public ArithmeticBinaryOperator(@NotNull String textRepresentation) {
            super(textRepresentation);
        }

        @Override
        public GLSLType getResultType(GLSLType firstInput, GLSLType secondInput) {
            if(!firstInput.isValidType() || !secondInput.isValidType())return UNKNOWN_TYPE;
            //Both types are scalars
            if(firstInput instanceof GLSLScalarType && secondInput instanceof GLSLScalarType){
                return unifyTypes(firstInput, secondInput);
            }
            //One type is scalar and the other is vector or matrix
            if(firstInput instanceof GLSLScalarType && (secondInput instanceof GLSLVectorType || secondInput instanceof GLSLMatrixType)){
                return getResultOfVectorOrMatrixAndScalar(secondInput, (GLSLScalarType) firstInput);
            }
            if(secondInput instanceof GLSLScalarType && (firstInput instanceof GLSLVectorType || firstInput instanceof GLSLMatrixType)){
                return getResultOfVectorOrMatrixAndScalar(firstInput, (GLSLScalarType) secondInput);
            }
            //Two operands are vectors of same size
            if(firstInput instanceof GLSLVectorType && secondInput instanceof GLSLVectorType){
                GLSLVectorType first = (GLSLVectorType) firstInput;
                GLSLVectorType second = (GLSLVectorType) secondInput;
                if(first.getNumComponents() == second.getNumComponents()){
                    GLSLType unified = unifyTypes(first.getBaseType(), second.getBaseType());
                    if(unified.isValidType()){
                        return GLSLVectorType.getType(unified, first.getNumComponents());
                    }else{
                        return UNKNOWN_TYPE;
                    }
                }
            }
            //Something else
            return getResultTypeOther(firstInput, secondInput);
        }

        private GLSLType getResultOfVectorOrMatrixAndScalar(GLSLType vectorMatrixType, GLSLScalarType scalarType){
            GLSLType unified = unifyTypes(vectorMatrixType.getBaseType(), scalarType);
            if(!unified.isValidType())return UNKNOWN_TYPE;
            else{
                if(vectorMatrixType instanceof GLSLMatrixType){
                    GLSLMatrixType matrixType = (GLSLMatrixType) vectorMatrixType;
                    return GLSLMatrixType.getType(unified, matrixType.getNumColumns(), matrixType.getNumRows());
                }else{
                    GLSLVectorType vectorType = (GLSLVectorType) vectorMatrixType;
                    return GLSLVectorType.getType(unified, vectorType.getNumComponents());
                }
            }
        }

        protected GLSLType getResultTypeOther(GLSLType firstInput, GLSLType secondInput){
            //Only for + - and /
            //Two operands are matrices of exactly same size
            if(firstInput instanceof GLSLMatrixType && secondInput instanceof GLSLMatrixType){
                GLSLMatrixType first = (GLSLMatrixType) firstInput;
                GLSLMatrixType second = (GLSLMatrixType) secondInput;
                if(first.getNumColumns() == second.getNumColumns() && first.getNumRows() == second.getNumRows()){
                    GLSLType unified = unifyTypes(first.getBaseType(), second.getBaseType());
                    if(unified.isValidType()){
                        return GLSLMatrixType.getType(unified, first.getNumColumns(), second.getNumRows());
                    }else{
                        return UNKNOWN_TYPE;
                    }
                }
            }
            return UNKNOWN_TYPE;
        }
    }

    protected static class ArithmeticMultiplicationBinaryOperator extends ArithmeticBinaryOperator {

        public ArithmeticMultiplicationBinaryOperator(@NotNull String textRepresentation) {
            super(textRepresentation);
        }

        private int getRows(GLSLType matrixOrVector, boolean columnVector){
            if(matrixOrVector instanceof GLSLMatrixType){
                return ((GLSLMatrixType)matrixOrVector).getNumRows();
            }else{
                return columnVector ? ((GLSLVectorType)matrixOrVector).getNumComponents() : 1;
            }
        }

        private int getColumns(GLSLType matrixOrVector, boolean rowVector){
            if(matrixOrVector instanceof GLSLMatrixType){
                return ((GLSLMatrixType)matrixOrVector).getNumColumns();
            }else{
                return rowVector ? ((GLSLVectorType)matrixOrVector).getNumComponents() : 0;
            }
        }

        @Override
        protected GLSLType getResultTypeOther(GLSLType firstInput, GLSLType secondInput) {
            //Only for *
            //Mat * mat || vec * mat || mat * vec
            if((firstInput instanceof GLSLMatrixType && secondInput instanceof GLSLMatrixType)
                    || (firstInput instanceof GLSLVectorType && secondInput instanceof GLSLMatrixType)
                    || (firstInput instanceof GLSLMatrixType && secondInput instanceof GLSLVectorType)){
                GLSLType unified = unifyTypes(firstInput.getBaseType(), secondInput.getBaseType());
                if(!unified.isValidType())return UNKNOWN_TYPE;
                int columnsLeft = getColumns(firstInput, true);
                int rowsRight = getRows(secondInput, true);
                if(columnsLeft != rowsRight)return UNKNOWN_TYPE;

                int rowsLeft = getRows(firstInput, false);
                int columnsRight = getColumns(secondInput, false);
                if(rowsLeft != 1 && columnsRight != 1){
                    //Result is matrix
                    return GLSLMatrixType.getType(unified, columnsRight, rowsLeft);
                }else {
                    //Result is a vector
                    if(rowsLeft != 1){
                        return GLSLVectorType.getType(unified, rowsLeft);
                    }else{
                        return GLSLVectorType.getType(unified, columnsRight);
                    }
                }
            }
            return UNKNOWN_TYPE;
        }
    }

    protected static class ArithmeticModuloBinaryOperator extends GLSLBinaryOperator {

        public ArithmeticModuloBinaryOperator(@NotNull String textRepresentation) {
            super(textRepresentation);
        }

        @NotNull
        @Override
        public GLSLType getResultType(GLSLType firstInput, GLSLType secondInput) {
            if(!firstInput.isValidType() || !secondInput.isValidType())return UNKNOWN_TYPE;
            GLSLType unified = unifyTypes(firstInput.getBaseType(), secondInput.getBaseType());
            //Operates only on integers
            if(unified != INT && unified != UINT)return UNKNOWN_TYPE;

            //Two vectors
            if(firstInput instanceof GLSLVectorType && secondInput instanceof GLSLVectorType){
                int firstVectorSize = ((GLSLVectorType)firstInput).getNumComponents();
                if(firstVectorSize != ((GLSLVectorType)secondInput).getNumComponents()){
                    //Vectors must have the same size
                    return UNKNOWN_TYPE;
                }else{
                    //Modulo applied component-wise
                    return GLSLVectorType.getType(unified, firstVectorSize);
                }
            }
            //Vector and scalar
            if(firstInput instanceof GLSLVectorType && secondInput instanceof GLSLScalarType){
                return GLSLVectorType.getType(unified, ((GLSLVectorType)firstInput).getNumComponents());
            }else if(secondInput instanceof GLSLVectorType && firstInput instanceof GLSLScalarType){
                return GLSLVectorType.getType(unified, ((GLSLVectorType)secondInput).getNumComponents());
            }
            //Scalar and scalar
            return unified;
        }
    }

    /**
     * Operators such as unary +, -, --, ++
     */
    protected static class ArithmeticUnaryOperator extends GLSLUnaryOperator {

        public ArithmeticUnaryOperator(@NotNull String textRepresentation) {
            super(textRepresentation);
        }

        @Override
        public boolean isValidInput(GLSLType type) {
            if(!type.isValidType())return true;
            //Those operators work on integer/float scalars, vectors and matrices
            return type.getBaseType() instanceof GLSLScalarType && type.getBaseType() != BOOL;
        }

        @NotNull
        @Override
        public GLSLType getResultType(GLSLType input) {
            if(isValidInput(input)){
                return input;
            }else{
                return UNKNOWN_TYPE;
            }
        }
    }

    /**
     * Operators like <=, >=, <, >
     */
    protected static class RelationalBinaryOperator extends GLSLBinaryOperator {

        public RelationalBinaryOperator(@NotNull String textRepresentation) {
            super(textRepresentation);
        }

        @Override
        public boolean isValidInput(GLSLType firstType, GLSLType secondType) {
            //If the types are invalid, input is valid (this is not the greatest problem right now)
            if(!firstType.isValidType() || !secondType.isValidType())return true;
            //Operates only on integer/float scalars
            return firstType instanceof GLSLScalarType && secondType instanceof GLSLScalarType &&
                    firstType != BOOL && secondType != BOOL;
        }

        @NotNull
        @Override
        public GLSLType getResultType(GLSLType firstInput, GLSLType secondInput) {
            //Relational operators always result in bool
            return BOOL;
        }
    }

    /**
     * == and !=
     */
    protected static class EqualityBinaryOperator extends GLSLBinaryOperator {

        public EqualityBinaryOperator(@NotNull String textRepresentation) {
            super(textRepresentation);
        }

        @Override
        public boolean isValidInput(GLSLType firstType, GLSLType secondType) {
            return true;
        }

        @NotNull
        @Override
        public GLSLType getResultType(GLSLType firstInput, GLSLType secondInput) {
            return BOOL;
        }
    }

    /**
     * That is &&, || and ^^
     */
    protected static class LogicalBinaryOperator extends GLSLBinaryOperator {

        public LogicalBinaryOperator(@NotNull String textRepresentation) {
            super(textRepresentation);
        }

        @Override
        public boolean isValidInput(GLSLType firstType, GLSLType secondType) {
            //If the types are invalid, input is valid (this is not the greatest problem right now)
            if(!firstType.isValidType() || !secondType.isValidType())return true;

            //operates only on two booleans
            return firstType == BOOL && secondType == BOOL;
        }

        @NotNull
        @Override
        public GLSLType getResultType(GLSLType firstInput, GLSLType secondInput) {
            return BOOL;
        }
    }

    /**
     * Logical not: !
     */
    protected static class LogicalUnaryOperator extends GLSLUnaryOperator {

        public LogicalUnaryOperator(@NotNull String textRepresentation) {
            super(textRepresentation);
        }

        @Override
        public boolean isValidInput(GLSLType type) {
            return !type.isValidType() || type == BOOL;
        }

        @NotNull
        @Override
        public GLSLType getResultType(GLSLType input) {
            return BOOL;
        }
    }

    /**
     * Bitwise invert operator: ~
     */
    protected static class OnesComplementOperator extends GLSLUnaryOperator {

        public OnesComplementOperator(@NotNull String textRepresentation) {
            super(textRepresentation);
        }

        @Override
        public boolean isValidInput(GLSLType type) {
            if(!type.isValidType())return true;
            if(type instanceof GLSLMatrixType)return false;
            //Operates on integer scalars and vectors
            GLSLType baseType = type.getBaseType();
            return baseType == INT || baseType == UINT;
        }

        @NotNull
        @Override
        public GLSLType getResultType(GLSLType input) {
            if(isValidInput(input))return input;
            else return UNKNOWN_TYPE;
        }
    }

    /**
     * Bitwise shift: << and >>
     */
    protected static class ShiftBinaryOperator extends GLSLBinaryOperator {

        public ShiftBinaryOperator(@NotNull String textRepresentation) {
            super(textRepresentation);
        }

        @Override
        public boolean isValidInput(GLSLType firstType, GLSLType secondType) {
            //If the types are invalid, input is valid (this is not the greatest problem right now)
            if(!firstType.isValidType() || !secondType.isValidType())return true;

            if(firstType instanceof GLSLScalarType && !(secondType instanceof GLSLScalarType)){
                //If first is scalar, second must be scalar as well
                return false;
            }

            if(firstType instanceof GLSLMatrixType || secondType instanceof GLSLMatrixType){
                //Operates only on scalars and vectors
                return false;
            }

            GLSLType firstBaseType = firstType.getBaseType();
            GLSLType secondBaseType = secondType.getBaseType();
            return GLSLScalarType.isIntegerScalar(firstBaseType) && GLSLScalarType.isIntegerScalar(secondBaseType);
        }

        @NotNull
        @Override
        public GLSLType getResultType(GLSLType firstInput, GLSLType secondInput) {
            if(isValidInput(firstInput, secondInput)){
                return firstInput;
            }else return UNKNOWN_TYPE;
        }
    }

    /**
     * Bitwise operators: &, ^ and |
     */
    protected static class BitwiseBinaryOperator extends GLSLBinaryOperator {

        public BitwiseBinaryOperator(@NotNull String textRepresentation) {
            super(textRepresentation);
        }

        @NotNull
        @Override
        public GLSLType getResultType(GLSLType firstInput, GLSLType secondInput) {
            if(!firstInput.isValidType() || !secondInput.isValidType())return UNKNOWN_TYPE;

            if(firstInput instanceof GLSLMatrixType || secondInput instanceof GLSLMatrixType){
                //Operates only on scalars and vectors
                return UNKNOWN_TYPE;
            }
            GLSLType firstBaseType = firstInput.getBaseType();
            GLSLType secondBaseType = secondInput.getBaseType();

            if(firstBaseType != secondBaseType){
                //Fundamental types must match
                return UNKNOWN_TYPE;
            }
            if(!GLSLScalarType.isIntegerScalar(firstBaseType) || !GLSLScalarType.isIntegerScalar(secondBaseType)){
                //Operates only on integer scalars and vectors
                return UNKNOWN_TYPE;
            }

            if(firstInput instanceof GLSLVectorType && secondInput instanceof GLSLVectorType){
                if(((GLSLVectorType)firstInput).getNumComponents() != ((GLSLVectorType)secondInput).getNumComponents()){
                    //Vectors must have the same size
                    return UNKNOWN_TYPE;
                }
            }
            //Both/first is/are vector(s)
            if(firstInput instanceof GLSLVectorType){
                GLSLVectorType.getType(firstBaseType, ((GLSLVectorType)firstInput).getNumComponents());
            }
            //Second is vector
            if(secondInput instanceof GLSLVectorType){
                GLSLVectorType.getType(firstBaseType, ((GLSLVectorType)secondInput).getNumComponents());
            }
            //Both are scalars
            return firstBaseType;
        }
    }

    protected static class BinaryOperatorAssingnmentOperator extends GLSLAssignmentOperator {

        private final GLSLBinaryOperator operator;

        public BinaryOperatorAssingnmentOperator(@NotNull String textRepresentation, GLSLBinaryOperator operator) {
            super(textRepresentation);
            this.operator = operator;
        }

        @Override
        public boolean isValidInput(GLSLType leftType, GLSLType rightType) {
            if(operator.isValidInput(leftType, rightType)){
                return super.isValidInput(leftType, operator.getResultType(leftType, rightType));
            }else return false;
        }
    }
    //endregion
}
