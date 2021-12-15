package glslplugin.lang.elements.expressions.operator;

import glslplugin.lang.elements.expressions.operator.GLSLOperator.*;

/**
 * Contains all operators.
 *
 * @author Jan Polák
 */
public final class GLSLOperators {

    //Arithmetic operators
    public static final GLSLBinaryOperator ADDITION = ArithmeticBinaryOperator.ADDITION;
    public static final GLSLBinaryOperator SUBTRACTION = ArithmeticBinaryOperator.SUBTRACTION;
    public static final GLSLBinaryOperator MULTIPLICATION = ArithmeticMultiplicationBinaryOperator.MULTIPLICATION;
    public static final GLSLBinaryOperator DIVISION = ArithmeticBinaryOperator.DIVISION;
    public static final GLSLBinaryOperator MODULO = ArithmeticModuloBinaryOperator.MODULO;

    // Logical operators
    public static final GLSLBinaryOperator LOGIC_AND = new LogicalBinaryOperator("&&");
    public static final GLSLBinaryOperator LOGIC_OR = new LogicalBinaryOperator("||");
    public static final GLSLBinaryOperator LOGIC_XOR = new LogicalBinaryOperator("^^");

    // Bit operators
    public static final GLSLBinaryOperator BINARY_AND = new BitwiseBinaryOperator("&");
    public static final GLSLBinaryOperator BINARY_XOR = new BitwiseBinaryOperator("^");
    public static final GLSLBinaryOperator BINARY_OR = new BitwiseBinaryOperator("|");
    public static final GLSLBinaryOperator BINARY_LEFT_SHIFT = new ShiftBinaryOperator("<<");
    public static final GLSLBinaryOperator BINARY_RIGHT_SHIFT = new ShiftBinaryOperator(">>");

    // Relational operators
    public static final GLSLBinaryOperator GREATER = new RelationalBinaryOperator(">");
    public static final GLSLBinaryOperator LESSER = new RelationalBinaryOperator("<");
    public static final GLSLBinaryOperator GREATER_EQUAL = new RelationalBinaryOperator(">=");
    public static final GLSLBinaryOperator LESSER_EQUAL = new RelationalBinaryOperator("<=");

    // Equality operators
    public static final GLSLBinaryOperator EQUAL = new EqualityBinaryOperator("==");
    public static final GLSLBinaryOperator NOT_EQUAL = new EqualityBinaryOperator("!=");

    // UNARY OPERATORS
    public static final GLSLUnaryOperator PLUS = ArithmeticUnaryOperator.PLUS;
    public static final GLSLUnaryOperator MINUS = ArithmeticUnaryOperator.MINUS;
    public static final GLSLUnaryOperator INCREMENT = ArithmeticUnaryOperator.INCREMENT;
    public static final GLSLUnaryOperator DECREMENT = ArithmeticUnaryOperator.DECREMENT;
    public static final GLSLUnaryOperator LOGIC_NEGATION = LogicalUnaryOperator.LOGIC_NEGATION;
    public static final GLSLUnaryOperator BINARY_NEGATION = OnesComplementOperator.BINARY_NEGATION;

    // Assignment operators
    public static final GLSLAssignmentOperator ASSIGN = new GLSLAssignmentOperator("=");

    public static final GLSLAssignmentOperator ADDITION_ASSIGN = new BinaryOperatorAssignmentOperator("+=", ADDITION);
    public static final GLSLAssignmentOperator SUBTRACTION_ASSIGN = new BinaryOperatorAssignmentOperator("-=", SUBTRACTION);
    public static final GLSLAssignmentOperator MULTIPLICATION_ASSIGN = new BinaryOperatorAssignmentOperator("*=", MULTIPLICATION);
    public static final GLSLAssignmentOperator DIVISION_ASSIGN = new BinaryOperatorAssignmentOperator("/=", DIVISION);
    public static final GLSLAssignmentOperator MODULO_ASSIGN = new BinaryOperatorAssignmentOperator("%=", MODULO);
    public static final GLSLAssignmentOperator LEFT_SHIFT_ASSIGN = new BinaryOperatorAssignmentOperator("<<=", BINARY_LEFT_SHIFT);
    public static final GLSLAssignmentOperator RIGHT_SHIFT_ASSIGN = new BinaryOperatorAssignmentOperator(">>=", BINARY_RIGHT_SHIFT);
    public static final GLSLAssignmentOperator BINARY_AND_ASSIGN = new BinaryOperatorAssignmentOperator("&=", BINARY_AND);
    public static final GLSLAssignmentOperator BINARY_XOR_ASSIGN = new BinaryOperatorAssignmentOperator("^=", BINARY_XOR);
    public static final GLSLAssignmentOperator BINARY_OR_ASSIGN = new BinaryOperatorAssignmentOperator("|=", BINARY_OR);

    // Special operators
    public static final GLSLOperator MEMBER = new GLSLOperator(".");
}
