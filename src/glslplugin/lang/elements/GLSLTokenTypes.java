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

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

public class GLSLTokenTypes {
    public static final IElementType IDENTIFIER = new GLSLElementType("IDENTIFIER");
//    public static final IElementType FIELD_SELECTION = new GLSLElementType("FIELD_SELECTION");

    public static final IElementType INTEGER_CONSTANT = new GLSLElementType("INTEGER_CONSTANT");
    public static final IElementType FLOAT_CONSTANT = new GLSLElementType("FLOAT_CONSTANT");
    public static final IElementType BOOL_CONSTANT = new GLSLElementType("BOOL_CONSTANT");

    public static final IElementType VOID_TYPE = new GLSLElementType("VOID_TYPE");
    public static final IElementType FLOAT_TYPE = new GLSLElementType("FLOAT_TYPE");
    public static final IElementType INT_TYPE = new GLSLElementType("INT_TYPE");
    public static final IElementType BOOL_TYPE = new GLSLElementType("BOOL_TYPE");
    public static final IElementType VEC2_TYPE = new GLSLElementType("VEC2_TYPE");
    public static final IElementType VEC3_TYPE = new GLSLElementType("VEC3_TYPE");
    public static final IElementType VEC4_TYPE = new GLSLElementType("VEC4_TYPE");
    public static final IElementType IVEC2_TYPE = new GLSLElementType("IVEC2_TYPE");
    public static final IElementType IVEC3_TYPE = new GLSLElementType("IVEC3_TYPE");
    public static final IElementType IVEC4_TYPE = new GLSLElementType("IVEC4_TYPE");
    public static final IElementType BVEC2_TYPE = new GLSLElementType("BVEC2_TYPE");
    public static final IElementType BVEC3_TYPE = new GLSLElementType("BVEC3_TYPE");
    public static final IElementType BVEC4_TYPE = new GLSLElementType("BVEC4_TYPE");
    public static final IElementType MAT2_TYPE = new GLSLElementType("MAT2_TYPE");
    public static final IElementType MAT3_TYPE = new GLSLElementType("MAT3_TYPE");
    public static final IElementType MAT4_TYPE = new GLSLElementType("MAT4_TYPE");
    public static final IElementType MAT2X2_TYPE = new GLSLElementType("MAT2X2_TYPE");
    public static final IElementType MAT2X3_TYPE = new GLSLElementType("MAT2X3_TYPE");
    public static final IElementType MAT2X4_TYPE = new GLSLElementType("MAT2X4_TYPE");
    public static final IElementType MAT3X2_TYPE = new GLSLElementType("MAT3X2_TYPE");
    public static final IElementType MAT3X3_TYPE = new GLSLElementType("MAT3X3_TYPE");
    public static final IElementType MAT3X4_TYPE = new GLSLElementType("MAT3X4_TYPE");
    public static final IElementType MAT4X2_TYPE = new GLSLElementType("MAT4X2_TYPE");
    public static final IElementType MAT4X3_TYPE = new GLSLElementType("MAT4X3_TYPE");
    public static final IElementType MAT4X4_TYPE = new GLSLElementType("MAT4X4_TYPE");
    public static final IElementType SAMPLER1D_TYPE = new GLSLElementType("SAMPLER1D_TYPE");
    public static final IElementType SAMPLER2D_TYPE = new GLSLElementType("SAMPLER2D_TYPE");
    public static final IElementType SAMPLER3D_TYPE = new GLSLElementType("SAMPLER3D_TYPE");
    public static final IElementType SAMPLERCUBE_TYPE = new GLSLElementType("SAMPLERCUBE_TYPE");
    public static final IElementType SAMPLER1DSHADOW_TYPE = new GLSLElementType("SAMPLER1DSHADOW_TYPE");
    public static final IElementType SAMPLER2DSHADOW_TYPE = new GLSLElementType("SAMPLER2DSHADOW_TYPE");
    public static final IElementType NAMED_TYPE = new GLSLElementType("NAMED_TYPE");

    public static final IElementType CONST_KEYWORD = new GLSLElementType("CONST_KEYWORD");
    public static final IElementType ATTRIBUTE_KEYWORD = new GLSLElementType("ATTRIBUTE_KEYWORD");
    public static final IElementType UNIFORM_KEYWORD = new GLSLElementType("UNIFORM_KEYWORD");
    public static final IElementType VARYING_KEYWORD = new GLSLElementType("VARYING_KEYWORD");
    public static final IElementType CENTROID_KEYWORD = new GLSLElementType("CENTROID_KEYWORD");
    public static final IElementType INVARIANT_KEYWORD = new GLSLElementType("INVARIANT_KEYWORD");
    public static final IElementType PATCH_KEYWORD = new GLSLElementType("PATCH_KEYWORD");
    public static final IElementType SAMPLE_KEYWORD = new GLSLElementType("SAMPLE_KEYWORD");
    public static final IElementType BUFFER_KEYWORD = new GLSLElementType("BUFFER_KEYWORD");
    public static final IElementType SHARED_KEYWORD = new GLSLElementType("SHARED_KEYWORD");
    public static final IElementType COHERENT_KEYWORD = new GLSLElementType("COHERENT_KEYWORD");
    public static final IElementType VOLATILE_KEYWORD = new GLSLElementType("VOLATILE_KEYWORD");
    public static final IElementType RESTRICT_KEYWORD = new GLSLElementType("RESTRICT_KEYWORD");
    public static final IElementType READONLY_KEYWORD = new GLSLElementType("READONLY_KEYWORD");
    public static final IElementType WRITEONLY_KEYWORD = new GLSLElementType("WRITEONLY_KEYWORD");
    public static final IElementType SUBROUTINE_KEYWORD = new GLSLElementType("SUBROUTINE_KEYWORD");
    public static final IElementType PRECISE_KEYWORD = new GLSLElementType("PRECISE_KEYWORD");
    public static final IElementType SMOOTH_KEYWORD = new GLSLElementType("SMOOTH_KEYWORD");
    public static final IElementType FLAT_KEYWORD = new GLSLElementType("FLAT_KEYWORD");
    public static final IElementType NOPERSPECTIVE_KEYWORD = new GLSLElementType("NOPERSPECTIVE_KEYWORD");

    public static final IElementType PRECISION_KEYWORD = new GLSLElementType("PRECISION_KEYWORD");

    public static final IElementType IN_KEYWORD = new GLSLElementType("IN_KEYWORD");
    public static final IElementType OUT_KEYWORD = new GLSLElementType("OUT_KEYWORD");
    public static final IElementType INOUT_KEYWORD = new GLSLElementType("INOUT_KEYWORD");

    public static final IElementType WHILE_KEYWORD = new GLSLElementType("WHILE_KEYWORD");
    public static final IElementType DO_KEYWORD = new GLSLElementType("DO_KEYWORD");
    public static final IElementType FOR_KEYWORD = new GLSLElementType("FOR_KEYWORD");

    public static final IElementType BREAK_JUMP_STATEMENT = new GLSLElementType("BREAK_JUMP_STATEMENT");
    public static final IElementType CONTINUE_JUMP_STATEMENT = new GLSLElementType("CONTINUE_JUMP_STATEMENT");
    public static final IElementType RETURN_JUMP_STATEMENT = new GLSLElementType("RETURN_JUMP_STATEMENT");
    public static final IElementType DISCARD_JUMP_STATEMENT = new GLSLElementType("DISCARD_JUMP_STATEMENT");

    public static final IElementType STRUCT = new GLSLElementType("STRUCT");

    public static final IElementType IF_KEYWORD = new GLSLElementType("IF_KEYWORD");
    public static final IElementType ELSE_KEYWORD = new GLSLElementType("ELSE_KEYWORD");

    public static final IElementType COMMENT_LINE = new GLSLElementType("COMMENT_LINE");
    public static final IElementType COMMENT_BLOCK = new GLSLElementType("COMMENT_BLOCK");

    public static final IElementType LEFT_BRACE = new GLSLElementType("LEFT_BRACE");
    public static final IElementType RIGHT_BRACE = new GLSLElementType("RIGHT_BRACE");
    public static final IElementType LEFT_PAREN = new GLSLElementType("LEFT_PAREN");
    public static final IElementType RIGHT_PAREN = new GLSLElementType("RIGHT_PAREN");
    public static final IElementType LEFT_BRACKET = new GLSLElementType("LEFT_BRACKET");
    public static final IElementType RIGHT_BRACKET = new GLSLElementType("RIGHT_BRACKET");

    public static final IElementType EQUAL = new GLSLElementType("EQUAL");
    public static final IElementType MUL_ASSIGN = new GLSLElementType("MUL_ASSIGN");
    public static final IElementType DIV_ASSIGN = new GLSLElementType("DIV_ASSIGN");
    public static final IElementType ADD_ASSIGN = new GLSLElementType("ADD_ASSIGN");
    public static final IElementType SUB_ASSIGN = new GLSLElementType("SUB_ASSIGN");
    public static final IElementType PLUS = new GLSLElementType("UNARY_PLUS");
    public static final IElementType DASH = new GLSLElementType("DASH");
    public static final IElementType SLASH = new GLSLElementType("SLASH");
    public static final IElementType STAR = new GLSLElementType("STAR");
    public static final IElementType DEC_OP = new GLSLElementType("DEC_OP");
    public static final IElementType INC_OP = new GLSLElementType("INC_OP");
    public static final IElementType EQ_OP = new GLSLElementType("EQ_OP");
    public static final IElementType LEFT_ANGLE = new GLSLElementType("LEFT_ANGLE");
    public static final IElementType RIGHT_ANGLE = new GLSLElementType("RIGHT_ANGLE");
    public static final IElementType GE_OP = new GLSLElementType("GE_OP");
    public static final IElementType LE_OP = new GLSLElementType("LE_OP");
    public static final IElementType NE_OP = new GLSLElementType("NE_OP");
    public static final IElementType AND_OP = new GLSLElementType("AND_OP");
    public static final IElementType OR_OP = new GLSLElementType("OR_OP");
    public static final IElementType XOR_OP = new GLSLElementType("XOR_OP");

    public static final IElementType QUESTION = new GLSLElementType("QUESTION");
    public static final IElementType COLON = new GLSLElementType("COLON");
    public static final IElementType BANG = new GLSLElementType("BANG");
    public static final IElementType DOT = new GLSLElementType("DOT");
    public static final IElementType SEMICOLON = new GLSLElementType("SEMICOLON");
    public static final IElementType COMMA = new GLSLElementType("COMMA");

    public static final IElementType WHITE_SPACE = TokenType.WHITE_SPACE;
    public static final IElementType UNKNOWN = new GLSLElementType("UNKNOWN");

    public static final IElementType PRECISION_STATEMENT = new GLSLElementType("PRECISION_STATEMENT");

    public static final IElementType COMPILER_DIRECTIVE_VERSION = new GLSLElementType("COMPILER_DIRECTIVE_VERSION");
    public static final IElementType COMPILER_DIRECTIVE_EXTENSION = new GLSLElementType("COMPILER_DIRECTIVE_EXTENSION");
    public static final IElementType COMPILER_DIRECTIVE_PRAGMA = new GLSLElementType("COMPILER_DIRECTIVE_PRAGMA");
    public static final IElementType COMPILER_DIRECTIVE_OTHER = new GLSLElementType("COMPILER_DIRECTIVE_OTHER");

    // Type specifiers
    public static final TokenSet FLOAT_TYPE_SPECIFIER_NONARRAY = TokenSet.create(FLOAT_TYPE, VEC2_TYPE, VEC3_TYPE, VEC4_TYPE);

    public static final TokenSet INTEGER_TYPE_SPECIFIER_NONARRAY = TokenSet.create(INT_TYPE, IVEC2_TYPE, IVEC3_TYPE, IVEC4_TYPE);

    public static final TokenSet BOOL_TYPE_SPECIFIER_NONARRAY = TokenSet.create(BOOL_TYPE, BVEC2_TYPE, BVEC3_TYPE, BVEC4_TYPE);

    public static final TokenSet MATRIX_TYPE_SPECIFIER_NONARRAY =
            TokenSet.create(MAT2_TYPE, MAT3_TYPE, MAT4_TYPE, MAT2X2_TYPE, MAT2X3_TYPE, MAT2X4_TYPE, MAT3X2_TYPE,
                    MAT3X3_TYPE, MAT3X4_TYPE, MAT4X2_TYPE, MAT4X3_TYPE, MAT4X4_TYPE);

    public static final TokenSet TEXTURE_TYPE_SPECIFIER_NONARRAY =
            TokenSet.create(SAMPLER1D_TYPE, SAMPLER2D_TYPE, SAMPLER3D_TYPE, SAMPLERCUBE_TYPE, SAMPLER1DSHADOW_TYPE,
                    SAMPLER2DSHADOW_TYPE);

    public static final TokenSet TYPE_SPECIFIER_NONARRAY_TOKENS =
            merge(TokenSet.create(VOID_TYPE), FLOAT_TYPE_SPECIFIER_NONARRAY, INTEGER_TYPE_SPECIFIER_NONARRAY,
                    BOOL_TYPE_SPECIFIER_NONARRAY, MATRIX_TYPE_SPECIFIER_NONARRAY, TEXTURE_TYPE_SPECIFIER_NONARRAY,
                    TokenSet.create(STRUCT, NAMED_TYPE));
    //

    //When modifying, don't forget to modify GLSLQualifier as well
    public static final TokenSet QUALIFIER_TOKENS = TokenSet.create(
            //GLSL Storage qualifiers
            CONST_KEYWORD,
            ATTRIBUTE_KEYWORD,
            UNIFORM_KEYWORD,
            VARYING_KEYWORD,
            CENTROID_KEYWORD,
            PATCH_KEYWORD,
            SAMPLE_KEYWORD,
            BUFFER_KEYWORD,
            SHARED_KEYWORD,
            //GLSL Memory qualifiers
            COHERENT_KEYWORD,
            VOLATILE_KEYWORD,
            RESTRICT_KEYWORD,
            READONLY_KEYWORD,
            WRITEONLY_KEYWORD,
            //GLSL Invariant qualifier
            INVARIANT_KEYWORD,
            //GLSL Subroutine qualifier
            SUBROUTINE_KEYWORD,
            //GLSL Precise qualifier
            PRECISE_KEYWORD,
            //GLSL ES Storage qualifiers
            PRECISION_KEYWORD,
            //GLSL Parameter modifiers
            IN_KEYWORD,
            OUT_KEYWORD,
            INOUT_KEYWORD,
            //GLSL Interpolation modifiers
            SMOOTH_KEYWORD,
            FLAT_KEYWORD,
            NOPERSPECTIVE_KEYWORD);

    public static final TokenSet COMMENTS = TokenSet.create(COMMENT_BLOCK, COMMENT_LINE, COMPILER_DIRECTIVE_VERSION, COMPILER_DIRECTIVE_EXTENSION, COMPILER_DIRECTIVE_PRAGMA, COMPILER_DIRECTIVE_OTHER, PRECISION_STATEMENT);

    public static final TokenSet ITERATION_KEYWORDS = TokenSet.create(WHILE_KEYWORD, DO_KEYWORD, FOR_KEYWORD);
    public static final TokenSet JUMP_KEYWORDS = TokenSet.create(BREAK_JUMP_STATEMENT, CONTINUE_JUMP_STATEMENT, RETURN_JUMP_STATEMENT, DISCARD_JUMP_STATEMENT);

    public static final TokenSet SELECTION_KEYWORDS = TokenSet.create(IF_KEYWORD, ELSE_KEYWORD);
    public static final TokenSet FLOW_KEYWORDS = merge(SELECTION_KEYWORDS, JUMP_KEYWORDS, ITERATION_KEYWORDS);

    public static final TokenSet ASSIGNMENT_OPERATORS = TokenSet.create(EQUAL, MUL_ASSIGN, DIV_ASSIGN, ADD_ASSIGN, SUB_ASSIGN);
    public static final TokenSet UNARY_OPERATORS = TokenSet.create(INC_OP, DEC_OP, PLUS, DASH, BANG);
    public static final TokenSet EQUALITY_OPERATORS = TokenSet.create(EQ_OP, NE_OP);
    public static final TokenSet RELATIONAL_OPERATORS = TokenSet.create(LEFT_ANGLE, RIGHT_ANGLE, LE_OP, GE_OP);
    public static final TokenSet ADDITIVE_OPERATORS = TokenSet.create(PLUS, DASH);
    public static final TokenSet MULTIPLICATIVE_OPERATORS = TokenSet.create(STAR, SLASH);
    public static final TokenSet LOGICAL_OPERATORS = TokenSet.create(AND_OP, OR_OP, XOR_OP);
    public static final TokenSet OPERATORS = merge(ASSIGNMENT_OPERATORS, UNARY_OPERATORS, EQUALITY_OPERATORS,
            RELATIONAL_OPERATORS, ADDITIVE_OPERATORS, MULTIPLICATIVE_OPERATORS, LOGICAL_OPERATORS);
    public static final TokenSet CONSTANT_TOKENS = TokenSet.create(BOOL_CONSTANT, INTEGER_CONSTANT, FLOAT_CONSTANT);

    public static final TokenSet EXPRESSION_FIRST_SET = merge(TokenSet.create(
                    INTEGER_CONSTANT, FLOAT_CONSTANT, BOOL_CONSTANT, // constants
                    INC_OP, DEC_OP, PLUS, DASH, BANG, // unary operators
                    IDENTIFIER, // function call, variable name, typename
                    LEFT_PAREN, // group
                    SEMICOLON // empty statement
            ),
            TYPE_SPECIFIER_NONARRAY_TOKENS
    );
    public static final TokenSet STATEMENT_FIRST_SET = merge(TokenSet.create(
                    LEFT_BRACE, // compound_statement
                    BREAK_JUMP_STATEMENT, CONTINUE_JUMP_STATEMENT, RETURN_JUMP_STATEMENT,
                    DISCARD_JUMP_STATEMENT, IF_KEYWORD,
                    DO_KEYWORD, FOR_KEYWORD, WHILE_KEYWORD // flow control
            ),
            QUALIFIER_TOKENS,
            EXPRESSION_FIRST_SET
    );
    public static final TokenSet FUNCTION_IDENTIFIER_TOKENS = merge(TokenSet.create(IDENTIFIER), TYPE_SPECIFIER_NONARRAY_TOKENS);

    public static TokenSet merge(TokenSet... sets) {
        TokenSet tokenSet = TokenSet.create();
        for (TokenSet set : sets) {
            tokenSet = TokenSet.orSet(tokenSet, set);
        }
        return tokenSet;
    }
}
