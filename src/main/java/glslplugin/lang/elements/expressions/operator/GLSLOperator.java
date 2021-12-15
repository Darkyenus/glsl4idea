package glslplugin.lang.elements.expressions.operator;

import glslplugin.lang.elements.types.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static glslplugin.lang.elements.types.GLSLTypes.*;

/**
 * Base class for all operators.
 *
 * Contains prototypes for all operator types and their specific implementations.
 *
 * @author Jan Polák
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

        /**
         * Can be meaningfully called only if isValidInput returned true.
         * Then it still might not return valid value.
         *
         * @param prefix - whether this operator is in prefix position
         * @return result of applying this operator to the input
         */
        @Nullable
        public abstract Object getResultValue(Object input, boolean prefix);
    }

    /**
     * Binary operator - takes two inputs and produces one output
     * <br>
     * For example: "3 + 4"
     */
    public static abstract class GLSLBinaryOperator extends GLSLOperator {

        protected GLSLBinaryOperator(@NotNull String textRepresentation) {
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

        /**
         * Can be meaningfully called only if isValidInput returned true.
         * Then it still might not return valid value.
         *
         * @return result of applying this operator to the input
         */
        @Nullable
        public abstract Object getResultValue(Object firstInput, Object secondInput);
    }

    /**
     * Assignment operator - takes the value on the right and somehow assigns it to the value on the left
     * Result type is always of the value on the left. Expression on the left must be L type.
     * <br>
     * For example: "i += 1"
     */
    public static class GLSLAssignmentOperator extends GLSLBinaryOperator {

        public GLSLAssignmentOperator(@NotNull String textRepresentation) {
            super(textRepresentation);
        }

        public boolean isValidInput(GLSLType leftType, GLSLType rightType) {
            //If the types are invalid, input is valid (this is not the greatest problem right now)
            if (!leftType.isValidType() || !rightType.isValidType()) return true;

            return leftType == rightType || rightType.isConvertibleTo(leftType);
        }

        @NotNull
        @Override
        public GLSLType getResultType(GLSLType firstInput, GLSLType secondInput) {
            return firstInput;
        }

        @Nullable
        @Override
        public Object getResultValue(Object firstInput, Object secondInput) {
            return null;
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

        public static final GLSLBinaryOperator ADDITION = new ArithmeticBinaryOperator("+");
        public static final GLSLBinaryOperator SUBTRACTION = new ArithmeticBinaryOperator("-");
        public static final GLSLBinaryOperator DIVISION = new ArithmeticBinaryOperator("/");

        private ArithmeticBinaryOperator(@NotNull String textRepresentation) {
            super(textRepresentation);
        }

        @NotNull
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

        @Nullable
        @Override
        public Object getResultValue(Object firstInput, Object secondInput) {
            if(firstInput instanceof Double && secondInput instanceof Double){
                return getResultValueD((Double) firstInput, (Double) secondInput);
            }else if(firstInput instanceof Long && secondInput instanceof Double){
                return getResultValueD(((Long) firstInput).doubleValue(), (Double) secondInput);
            }else if(firstInput instanceof Double && secondInput instanceof Long){
                return getResultValueD((Double) firstInput, ((Long) secondInput).doubleValue());
            }else if(firstInput instanceof Long && secondInput instanceof Long){
                long first = (Long)firstInput;
                long second = (Long)secondInput;
                if(this == ADDITION){
                    return first + second;
                }else if(this == SUBTRACTION){
                    return first - second;
                }else if(this == DIVISION){
                    return first / second;
                }else if(this == ArithmeticMultiplicationBinaryOperator.MULTIPLICATION){
                    //Take care of multiplication here as well,
                    // since it is a subclass and its operation on scalars is pretty standard
                    return first * second;
                }else return null;
            }
            return null;
        }

        private Object getResultValueD(double firstInput, double secondInput){
            if(this == ADDITION){
                return firstInput + secondInput;
            }else if(this == SUBTRACTION){
                return firstInput - secondInput;
            }else if(this == DIVISION){
                return firstInput / secondInput;
            }else if(this == ArithmeticMultiplicationBinaryOperator.MULTIPLICATION){
                return firstInput * secondInput;
            }else return null;
        }
    }

    /**
     *      *
     */
    protected static class ArithmeticMultiplicationBinaryOperator extends ArithmeticBinaryOperator {

        public static final GLSLBinaryOperator MULTIPLICATION = new ArithmeticMultiplicationBinaryOperator("*");

        private ArithmeticMultiplicationBinaryOperator(@NotNull String textRepresentation) {
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
                return rowVector ? ((GLSLVectorType)matrixOrVector).getNumComponents() : 1;
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
                if(rowsLeft > 1 && columnsRight > 1){
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

    /**
     *      %
     */
    protected static class ArithmeticModuloBinaryOperator extends GLSLBinaryOperator {

        public static final GLSLBinaryOperator MODULO = new ArithmeticModuloBinaryOperator("%");

        private ArithmeticModuloBinaryOperator(@NotNull String textRepresentation) {
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

        @Nullable
        @Override
        public Object getResultValue(Object firstInput, Object secondInput) {
            if(firstInput instanceof Long && secondInput instanceof Long){
                return ((Long)firstInput) % ((Long)secondInput);
            }
            return null;
        }
    }

    /**
     * Operators such as unary +, -, --, ++
     */
    protected static class ArithmeticUnaryOperator extends GLSLUnaryOperator {

        public static final GLSLUnaryOperator PLUS = new ArithmeticUnaryOperator("+");
        public static final GLSLUnaryOperator MINUS = new ArithmeticUnaryOperator("-");
        public static final GLSLUnaryOperator INCREMENT = new ArithmeticUnaryOperator("++");
        public static final GLSLUnaryOperator DECREMENT = new ArithmeticUnaryOperator("--");

        private ArithmeticUnaryOperator(@NotNull String textRepresentation) {
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

        @Nullable
        @Override
        public Object getResultValue(Object input, boolean prefix) {
            if(this == PLUS){
                return input;
            }else if(this == MINUS){
                if(input instanceof Double){
                    return -((Double)input);
                }else if(input instanceof Long){
                    return -((Long)input);
                }
            } else {
                //Increment and decrement handling
                if(!prefix)return input;//If it is in postfix, it is returned first, then changed, so nothing happens here
                int change;
                if(this == INCREMENT){
                    change = 1;
                }else if(this == DECREMENT){
                    change = -1;
                }else return null;//Weird.

                if(input instanceof Double){
                    return ((Double)input) + change;
                }else if(input instanceof Long){
                    return ((Long)input) + change;
                }
            }
            //Something is wrong and can't deduce it
            return null;
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

        @Nullable
        @Override
        public Object getResultValue(Object firstInput, Object secondInput) {
            return null;
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

        @Nullable
        @Override
        public Object getResultValue(Object firstInput, Object secondInput) {
            return null;
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

        @Nullable
        @Override
        public Object getResultValue(Object firstInput, Object secondInput) {
            return null;
        }
    }

    /**
     * Logical not: !
     */
    protected static class LogicalUnaryOperator extends GLSLUnaryOperator {

        public static final GLSLUnaryOperator LOGIC_NEGATION = new LogicalUnaryOperator("!");

        private LogicalUnaryOperator(@NotNull String textRepresentation) {
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

        @Nullable
        @Override
        public Object getResultValue(Object input, boolean prefix) {
            if (this == LOGIC_NEGATION) {
                if (input instanceof Boolean) {
                    return !((Boolean) input);
                }
            }
            return null;
        }
    }

    /**
     * Bitwise invert operator: ~
     */
    protected static class OnesComplementOperator extends GLSLUnaryOperator {

        public static final GLSLUnaryOperator BINARY_NEGATION = new OnesComplementOperator("~");

        private OnesComplementOperator(@NotNull String textRepresentation) {
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

        @Nullable
        @Override
        public Object getResultValue(Object input, boolean prefix) {
            if (this == BINARY_NEGATION) {
                if (input instanceof Long) {
                    return ~((Long) input);
                }
            }
            return null;
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

        @Nullable
        @Override
        public Object getResultValue(Object firstInput, Object secondInput) {
            return null;
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
                //Fundamental types must match (even "(un)signed-ness")
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

                //Both are vectors
                return firstInput;
            }
            //First is vector
            if(firstInput instanceof GLSLVectorType){
                return firstInput;
            }
            //Second is vector
            if(secondInput instanceof GLSLVectorType){
                return secondInput;
            }
            //Both are scalars
            return firstBaseType;
        }

        @Nullable
        @Override
        public Object getResultValue(Object firstInput, Object secondInput) {
            return null;
        }
    }

    protected static class BinaryOperatorAssignmentOperator extends GLSLAssignmentOperator {

        private final GLSLBinaryOperator operator;

        public BinaryOperatorAssignmentOperator(@NotNull String textRepresentation, GLSLBinaryOperator operator) {
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
