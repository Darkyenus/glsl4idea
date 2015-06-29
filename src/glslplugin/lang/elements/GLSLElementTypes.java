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

package glslplugin.lang.elements;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import glslplugin.lang.GLSLLanguage;
import glslplugin.lang.elements.expressions.GLSLLiteral;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class GLSLElementTypes {
    public static final IFileElementType FILE = new IFileElementType(Language.findInstance(GLSLLanguage.class));
    public static final IElementType BLOCK = new GLSLElementType("BLOCK");

    public static final IElementType PREPROCESSOR_DIRECTIVE = new GLSLElementType("PREPROCESSOR_DIRECTIVE");

    public static final IElementType EMPTY_STATEMENT = new GLSLElementType("EMPTY_STATEMENT");
    public static final IElementType JUMP_STATEMENT = new GLSLElementType("JUMP_STATEMENT");

    public static final IElementType VARIABLE_NAME_EXPRESSION = new GLSLElementType("VARIABLE_NAME_EXPRESSION");
    public static final IElementType PRIMARY_EXPRESSION = new GLSLElementType("PRIMARY_EXPRESSION");
    public static final IElementType POSTFIX_EXPRESSION = new GLSLElementType("POSTFIX_EXPRESSION");
    public static final IElementType EXPRESSION = new GLSLElementType("EXPRESSION");
    public static final IElementType CONSTANT_EXPRESSION = new GLSLElementType("CONSTANT_EXPRESSION");
    public static final IElementType GROUPED_EXPRESSION = new GLSLElementType("GROUPED_EXPRESSION");
    public static final IElementType SUBSCRIPT_EXPRESSION = new GLSLElementType("SUBSCRIPT_EXPRESSION");
    public static final IElementType FIELD_SELECTION_EXPRESSION = new GLSLElementType("FIELD_SELECTION_EXPRESSION");
    public static final IElementType METHOD_CALL_EXPRESSION = new GLSLElementType("METHOD_CALL_EXPRESSION");
    public static final IElementType POSTFIX_OPERATOR_EXPRESSION = new GLSLElementType("POSTFIX_OPERATOR_EXPRESSION");
    public static final IElementType PREFIX_OPERATOR_EXPRESSION = new GLSLElementType("PREFIX_OPERATOR_EXPRESSION");

    public static final IElementType VARIABLE_NAME = new GLSLElementType("VARIABLE_NAME");
    public static final IElementType METHOD_NAME = new GLSLElementType("METHOD_NAME");
    public static final IElementType FIELD_NAME = new GLSLElementType("FIELD_NAME");
    public static final IElementType FUNCTION_NAME = new GLSLElementType("FUNCTION_NAME");

    public static final IElementType FUNCTION_DECLARATION = new GLSLElementType("FUNCTION_DECLARATION");
    public static final IElementType FUNCTION_PARAMETER_DECLARATOR = new GLSLElementType("FUNCTION_PARAMETER_DECLARATOR");
    public static final IElementType FUNCTION_PARAMETER_DECLARATOR_LIST = new GLSLElementType("FUNCTION_PARAMETER_DECLARATOR_LIST");
    public static final IElementType PARAMETER_QUALIFIER = new GLSLElementType("PARAMETER_QUALIFIER");

    public static final IElementType STRUCT_SPECIFIER = new GLSLElementType("STRUCT_SPECIFIER");
    public static final IElementType STRUCT_DECLARATION_LIST = new GLSLElementType("STRUCT_DECLARATION_LIST");
    public static final IElementType STRUCT_DECLARATION = new GLSLElementType("STRUCT_DECLARATION");
    public static final IElementType STRUCT_DECLARATOR = new GLSLElementType("STRUCT_DECLARATOR");
    public static final IElementType STRUCT_DECLARATOR_LIST = new GLSLElementType("STRUCT_DECLARATOR_LIST");

    public static final IElementType TYPE_SPECIFIER_PRIMITIVE = new GLSLElementType("TYPE_SPECIFIER_PRIMITIVE");
    public static final IElementType TYPE_SPECIFIER_STRUCT = new GLSLElementType("TYPE_SPECIFIER_STRUCT");
    public static final IElementType TYPE_SPECIFIER_STRUCT_REFERENCE = new GLSLElementType("TYPE_SPECIFIER_STRUCT_REFERENCE");
//    public static final IElementType TYPE_SPECIFIER_NAMED = new GLSLElementType("TYPE_SPECIFIER_NAMED");

    public static final IElementType INIT_DECLARATOR_LIST = new GLSLElementType("INIT_DECLARATOR_LIST");
    public static final IElementType VARIABLE_DECLARATION = new GLSLElementType("VARIABLE_DECLARATION");
    public static final IElementType INTERFACE_BLOCK = new GLSLElementType("INTERFACE_BLOCK");
    public static final IElementType FUNCTION_DEFINITION = new GLSLElementType("FUNCTION_DEFINITION");
    public static final IElementType QUALIFIER = new GLSLElementType("QUALIFIER");
    public static final IElementType QUALIFIER_LIST = new GLSLElementType("QUALIFIER_LIST");
    public static final IElementType LAYOUT_QUALIFIER_ID = new GLSLElementType("LAYOUT_QUALIFIER_ID");
    public static final IElementType TYPE_SPECIFIER = new GLSLElementType("TYPE_SPECIFIER");
    public static final IElementType LAYOUT_QUALIFIER_STATEMENT = new GLSLElementType("LAYOUT_QUALIFIER_STATEMENT");

    public static final IElementType PARAMETER_DECLARATION_LIST = new GLSLElementType("PARAMETER_DECLARATION_LIST");
    public static final IElementType PARAMETER_DECLARATION = new GLSLElementType("PARAMETER_DECLARATION");
    public static final IElementType PARAMETER_DECLARATOR = new GLSLElementType("PARAMETER_DECLARATOR");
    public static final IElementType FULLY_SPECIFIED_PARAMETER_TYPE = new GLSLElementType("FULLY_SPECIFIED_PARAMETER_TYPE");


    public static final IElementType DECLARATOR_LIST = new GLSLElementType("DECLARATOR_LIST");
    public static final IElementType DECLARATOR = new GLSLElementType("DECLARATOR");
    public static final IElementType ARRAY_DECLARATOR = new GLSLElementType("ARRAY_DECLARATOR");
    public static final IElementType INITIALIZER = new GLSLElementType("INITIALIZER");
    public static final IElementType INITIALIZER_LIST = new GLSLElementType("INITIALIZER_LIST");

    public static final IElementType COMPOUND_STATEMENT = new GLSLElementType("COMPOUND_STATEMENT");

    public static final IElementType ASSIGNMENT_OPERATOR = new GLSLElementType("ASSIGNMENT_OPERATOR");
    public static final IElementType UNARY_OPERATOR = new GLSLElementType("UNARY_OPERATOR");

    //    public static final IElementType UNARY_EXPRESSION = new GLSLElementType("UNARY_EXPRESSION");
    public static final IElementType ASSIGNMENT_EXPRESSION = new GLSLElementType("ASSIGNMENT_EXPRESSION");
    public static final IElementType CONDITIONAL_EXPRESSION = new GLSLElementType("CONDITIONAL_EXPRESSION");
    public static final IElementType LOGICAL_AND_EXPRESSION = new GLSLElementType("LOGICAL_AND_EXPRESSION");
    public static final IElementType LOGICAL_XOR_EXPRESSION = new GLSLElementType("LOGICAL_XOR_EXPRESSION");
    public static final IElementType LOGICAL_OR_EXPRESSION = new GLSLElementType("LOGICAL_OR_EXPRESSION");
    public static final IElementType BINARY_AND_EXPRESSION = new GLSLElementType("BINARY_AND_EXPRESSION");
    public static final IElementType BINARY_XOR_EXPRESSION = new GLSLElementType("BINARY_XOR_EXPRESSION");
    public static final IElementType BINARY_OR_EXPRESSION = new GLSLElementType("BINARY_OR_EXPRESSION");
    public static final IElementType EQUALITY_EXPRESSION = new GLSLElementType("EQUALITY_EXPRESSION");
    public static final IElementType RELATIONAL_EXPRESSION = new GLSLElementType("RELATIONAL_EXPRESSION");
    public static final IElementType BIT_SHIFT_EXPRESSION = new GLSLElementType("BIT_SHIFT_EXPRESSION");
    public static final IElementType ADDITIVE_EXPRESSION = new GLSLElementType("ADDITIVE_EXPRESSION");
    public static final IElementType MULTIPLICATIVE_EXPRESSION = new GLSLElementType("MULTIPLICATIVE_EXPRESSION");

    public static final IElementType FUNCTION_CALL_EXPRESSION = new GLSLElementType("FUNCTION_CALL_EXPRESSION");
    public static final IElementType PARAMETER_LIST = new GLSLElementType("PARAMETER_LIST");
    public static final IElementType STATEMENT_LIST = new GLSLElementType("STATEMENT_LIST");
    public static final IElementType STATEMENT = new GLSLElementType("STATEMENT");
    public static final IElementType SIMPLE_STATEMENT = new GLSLElementType("SIMPLE_STATEMENT");
    public static final IElementType EXPRESSION_STATEMENT = new GLSLElementType("EXPRESSION_STATEMENT");
    public static final IElementType DECLARATION_STATEMENT = new GLSLElementType("DECLARATION_STATEMENT");

    public static final IElementType BREAK_STATEMENT = new GLSLElementType("BREAK_STATEMENT");
    public static final IElementType CONTINUE_STATEMENT = new GLSLElementType("CONTINUE_STATEMENT");
    public static final IElementType DISCARD_STATEMENT = new GLSLElementType("DISCARD_STATEMENT");
    public static final IElementType RETURN_STATEMENT = new GLSLElementType("RETURN_STATEMENT");

    public static final IElementType IF_STATEMENT = new GLSLElementType("IF_STATEMENT");
    public static final IElementType FOR_STATEMENT = new GLSLElementType("FOR_STATEMENT");
    public static final IElementType DO_STATEMENT = new GLSLElementType("DO_STATEMENT");
    public static final IElementType WHILE_STATEMENT = new GLSLElementType("WHILE_STATEMENT");

    public static final IElementType PRECISION_STATEMENT = new GLSLElementType("PRECISION_STATEMENT");

    public static final IElementType FOR_INIT_STATEMENT = new GLSLElementType("FOR_INIT_STATEMENT");
    public static final IElementType FOR_REST_STATEMENT = new GLSLElementType("FOR_REST_STATEMENT");
    public static final IElementType CONDITION = new GLSLElementType("CONDITION");

    //Preprocessor dropins
    public static final class PreprocessedExpressionElementType extends GLSLElementType {

        public final String text;

        public PreprocessedExpressionElementType(String text) {
            super("PREPROCESSED_EXPRESSION", false);
            this.text = text;
        }
    }
    public static final class PreprocessedLiteralElementType extends GLSLElementType {

        public final GLSLLiteral.Type type;
        public final String text;

        public PreprocessedLiteralElementType(GLSLLiteral.Type type, String text) {
            super("PREPROCESSED_LITERAL", false);
            this.type = type;
            this.text = text;
        }
    }
    public static final IElementType PREPROCESSED_EMPTY = new GLSLElementType("PREPROCESSED_EMPTY");
    public static final class PreprocessedUnknownElementType extends GLSLElementType {
        public final String text;

        public PreprocessedUnknownElementType(String text) {
            super("PREPROCESSED_UNKNOWN", false);
            this.text = text;
        }
    }
}
