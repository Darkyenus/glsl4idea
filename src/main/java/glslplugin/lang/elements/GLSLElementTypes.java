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

public class GLSLElementTypes {
    public static final IFileElementType FILE = new IFileElementType(Language.findInstance(GLSLLanguage.class));

    //Workaround before proper text redefinition of remapped tokens is implemented, see RedefinedTokenElementType below
    //public static final IElementType REDEFINED_TOKEN = new GLSLElementType("REDEFINED_TOKEN");

    public static final IElementType VARIABLE_NAME_EXPRESSION = new GLSLElementType("VARIABLE_NAME_EXPRESSION");
    public static final IElementType EXPRESSION = new GLSLElementType("EXPRESSION");
    public static final IElementType CONSTANT_EXPRESSION = new GLSLElementType("CONSTANT_EXPRESSION");
    public static final IElementType GROUPED_EXPRESSION = new GLSLElementType("GROUPED_EXPRESSION");
    public static final IElementType SUBSCRIPT_EXPRESSION = new GLSLElementType("SUBSCRIPT_EXPRESSION");
    public static final IElementType FIELD_SELECTION_EXPRESSION = new GLSLElementType("FIELD_SELECTION_EXPRESSION");
    public static final IElementType METHOD_CALL_EXPRESSION = new GLSLElementType("METHOD_CALL_EXPRESSION");
    public static final IElementType POSTFIX_OPERATOR_EXPRESSION = new GLSLElementType("POSTFIX_OPERATOR_EXPRESSION");
    public static final IElementType PREFIX_OPERATOR_EXPRESSION = new GLSLElementType("PREFIX_OPERATOR_EXPRESSION");


    public static final IElementType FUNCTION_DECLARATION = new GLSLElementType("FUNCTION_DECLARATION");

    public static final IElementType STRUCT_MEMBER_DECLARATION = new GLSLElementType("STRUCT_MEMBER_DECLARATION");
    public static final IElementType STRUCT_DECLARATOR = new GLSLElementType("STRUCT_DECLARATOR");
    public static final IElementType STRUCT_DECLARATOR_LIST = new GLSLElementType("STRUCT_DECLARATOR_LIST");

    public static final IElementType TYPE_SPECIFIER_PRIMITIVE = new GLSLElementType("TYPE_SPECIFIER_PRIMITIVE");
    public static final IElementType TYPE_SPECIFIER_STRUCT = new GLSLElementType("TYPE_SPECIFIER_STRUCT");
    public static final IElementType TYPE_SPECIFIER_STRUCT_REFERENCE = new GLSLElementType("TYPE_SPECIFIER_STRUCT_REFERENCE");

    public static final IElementType VARIABLE_DECLARATION = new GLSLElementType("VARIABLE_DECLARATION");
    public static final IElementType INTERFACE_BLOCK = new GLSLElementType("INTERFACE_BLOCK");
    public static final IElementType FUNCTION_DEFINITION = new GLSLElementType("FUNCTION_DEFINITION");
    public static final IElementType QUALIFIER = new GLSLElementType("QUALIFIER");
    public static final IElementType QUALIFIER_LIST = new GLSLElementType("QUALIFIER_LIST");
    public static final IElementType LAYOUT_QUALIFIER_ID = new GLSLElementType("LAYOUT_QUALIFIER_ID");
    public static final IElementType TYPE_SPECIFIER = new GLSLElementType("TYPE_SPECIFIER");
    public static final IElementType LAYOUT_QUALIFIER_STATEMENT = new GLSLElementType("LAYOUT_QUALIFIER_STATEMENT");

    public static final IElementType PARAMETER_DECLARATION = new GLSLElementType("PARAMETER_DECLARATION");
    public static final IElementType PARAMETER_DECLARATOR = new GLSLElementType("PARAMETER_DECLARATOR");


    public static final IElementType DECLARATOR_LIST = new GLSLElementType("DECLARATOR_LIST");
    public static final IElementType DECLARATOR = new GLSLElementType("DECLARATOR");
    public static final IElementType ARRAY_DECLARATOR = new GLSLElementType("ARRAY_DECLARATOR");
    public static final IElementType INITIALIZER = new GLSLElementType("INITIALIZER");
    public static final IElementType INITIALIZER_LIST = new GLSLElementType("INITIALIZER_LIST");

    public static final IElementType COMPOUND_STATEMENT = new GLSLElementType("COMPOUND_STATEMENT");

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
    public static final IElementType EXPRESSION_STATEMENT = new GLSLElementType("EXPRESSION_STATEMENT");
    public static final IElementType DECLARATION_STATEMENT = new GLSLElementType("DECLARATION_STATEMENT");

    public static final IElementType BREAK_STATEMENT = new GLSLElementType("BREAK_STATEMENT");
    public static final IElementType CONTINUE_STATEMENT = new GLSLElementType("CONTINUE_STATEMENT");
    public static final IElementType DISCARD_STATEMENT = new GLSLElementType("DISCARD_STATEMENT");
    public static final IElementType RETURN_STATEMENT = new GLSLElementType("RETURN_STATEMENT");
    public static final IElementType CASE_STATEMENT = new GLSLElementType("CASE_STATEMENT");
    public static final IElementType DEFAULT_STATEMENT = new GLSLElementType("DEFAULT_STATEMENT");

    public static final IElementType IF_STATEMENT = new GLSLElementType("IF_STATEMENT");
    public static final IElementType FOR_STATEMENT = new GLSLElementType("FOR_STATEMENT");
    public static final IElementType DO_STATEMENT = new GLSLElementType("DO_STATEMENT");
    public static final IElementType WHILE_STATEMENT = new GLSLElementType("WHILE_STATEMENT");
    public static final IElementType SWITCH_STATEMENT = new GLSLElementType("SWITCH_STATEMENT");

    public static final IElementType PRECISION_STATEMENT = new GLSLElementType("PRECISION_STATEMENT");

    public static final IElementType CONDITION = new GLSLElementType("CONDITION");

    // https://github.com/KhronosGroup/GLSL/blob/master/extensions/ext/GL_EXT_control_flow_attributes.txt
    public static final IElementType FLOW_ATTRIBUTE = new GLSLElementType("FLOW_ATTRIBUTE");

    //Preprocessor dropins
    @Deprecated// Is this still useful?
    public static final class RedefinedTokenElementType extends GLSLElementType {

        public final String text;

        public RedefinedTokenElementType(String text) {
            super("REDEFINED_TOKEN", false);
            this.text = text;
        }
    }
}
