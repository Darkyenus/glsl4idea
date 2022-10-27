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

package glslplugin.lang.parser;

import com.intellij.lang.ForeignLeafType;
import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import glslplugin.lang.elements.GLSLTokenTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static glslplugin.lang.elements.GLSLElementTypes.ADDITIVE_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.ARRAY_DECLARATOR;
import static glslplugin.lang.elements.GLSLElementTypes.ASSIGNMENT_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.BINARY_AND_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.BINARY_OR_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.BINARY_XOR_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.BIT_SHIFT_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.BREAK_STATEMENT;
import static glslplugin.lang.elements.GLSLElementTypes.CASE_STATEMENT;
import static glslplugin.lang.elements.GLSLElementTypes.COMPOUND_STATEMENT;
import static glslplugin.lang.elements.GLSLElementTypes.CONDITION;
import static glslplugin.lang.elements.GLSLElementTypes.CONDITIONAL_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.CONSTANT_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.CONTINUE_STATEMENT;
import static glslplugin.lang.elements.GLSLElementTypes.DECLARATION_STATEMENT;
import static glslplugin.lang.elements.GLSLElementTypes.DECLARATOR;
import static glslplugin.lang.elements.GLSLElementTypes.DECLARATOR_LIST;
import static glslplugin.lang.elements.GLSLElementTypes.DEFAULT_STATEMENT;
import static glslplugin.lang.elements.GLSLElementTypes.DISCARD_STATEMENT;
import static glslplugin.lang.elements.GLSLElementTypes.DO_STATEMENT;
import static glslplugin.lang.elements.GLSLElementTypes.EQUALITY_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.SEQUENCE_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.EXPRESSION_STATEMENT;
import static glslplugin.lang.elements.GLSLElementTypes.FIELD_SELECTION_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.FLOW_ATTRIBUTE;
import static glslplugin.lang.elements.GLSLElementTypes.FOR_STATEMENT;
import static glslplugin.lang.elements.GLSLElementTypes.FUNCTION_CALL_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.FUNCTION_DECLARATION;
import static glslplugin.lang.elements.GLSLElementTypes.FUNCTION_DEFINITION;
import static glslplugin.lang.elements.GLSLElementTypes.GROUPED_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.IF_STATEMENT;
import static glslplugin.lang.elements.GLSLElementTypes.INITIALIZER;
import static glslplugin.lang.elements.GLSLElementTypes.INITIALIZER_LIST;
import static glslplugin.lang.elements.GLSLElementTypes.INTERFACE_BLOCK;
import static glslplugin.lang.elements.GLSLElementTypes.LAYOUT_QUALIFIER_ID;
import static glslplugin.lang.elements.GLSLElementTypes.LAYOUT_QUALIFIER_STATEMENT;
import static glslplugin.lang.elements.GLSLElementTypes.LOGICAL_AND_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.LOGICAL_OR_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.LOGICAL_XOR_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.METHOD_CALL_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.MULTIPLICATIVE_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.PARAMETER_DECLARATION;
import static glslplugin.lang.elements.GLSLElementTypes.PARAMETER_DECLARATOR;
import static glslplugin.lang.elements.GLSLElementTypes.PARAMETER_LIST;
import static glslplugin.lang.elements.GLSLElementTypes.POSTFIX_OPERATOR_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.PRECISION_STATEMENT;
import static glslplugin.lang.elements.GLSLElementTypes.PREFIX_OPERATOR_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.QUALIFIER;
import static glslplugin.lang.elements.GLSLElementTypes.QUALIFIER_LIST;
import static glslplugin.lang.elements.GLSLElementTypes.RELATIONAL_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.RETURN_STATEMENT;
import static glslplugin.lang.elements.GLSLElementTypes.STRUCT_DECLARATOR;
import static glslplugin.lang.elements.GLSLElementTypes.STRUCT_DECLARATOR_LIST;
import static glslplugin.lang.elements.GLSLElementTypes.STRUCT_MEMBER_DECLARATION;
import static glslplugin.lang.elements.GLSLElementTypes.SUBSCRIPT_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.SWITCH_STATEMENT;
import static glslplugin.lang.elements.GLSLElementTypes.TYPE_SPECIFIER;
import static glslplugin.lang.elements.GLSLElementTypes.TYPE_SPECIFIER_PRIMITIVE;
import static glslplugin.lang.elements.GLSLElementTypes.TYPE_SPECIFIER_STRUCT;
import static glslplugin.lang.elements.GLSLElementTypes.TYPE_SPECIFIER_STRUCT_REFERENCE;
import static glslplugin.lang.elements.GLSLElementTypes.VARIABLE_DECLARATION;
import static glslplugin.lang.elements.GLSLElementTypes.VARIABLE_NAME_EXPRESSION;
import static glslplugin.lang.elements.GLSLElementTypes.WHILE_STATEMENT;
import static glslplugin.lang.elements.GLSLTokenTypes.ADDITIVE_OPERATORS;
import static glslplugin.lang.elements.GLSLTokenTypes.AMPERSAND;
import static glslplugin.lang.elements.GLSLTokenTypes.AND_OP;
import static glslplugin.lang.elements.GLSLTokenTypes.ASSIGNMENT_OPERATORS;
import static glslplugin.lang.elements.GLSLTokenTypes.BIT_SHIFT_OPERATORS;
import static glslplugin.lang.elements.GLSLTokenTypes.BREAK_JUMP_STATEMENT;
import static glslplugin.lang.elements.GLSLTokenTypes.CARET;
import static glslplugin.lang.elements.GLSLTokenTypes.CASE_KEYWORD;
import static glslplugin.lang.elements.GLSLTokenTypes.COLON;
import static glslplugin.lang.elements.GLSLTokenTypes.COMMA;
import static glslplugin.lang.elements.GLSLTokenTypes.CONSTANT_TOKENS;
import static glslplugin.lang.elements.GLSLTokenTypes.CONTINUE_JUMP_STATEMENT;
import static glslplugin.lang.elements.GLSLTokenTypes.DASH;
import static glslplugin.lang.elements.GLSLTokenTypes.DEC_OP;
import static glslplugin.lang.elements.GLSLTokenTypes.DEFAULT_KEYWORD;
import static glslplugin.lang.elements.GLSLTokenTypes.DISCARD_JUMP_STATEMENT;
import static glslplugin.lang.elements.GLSLTokenTypes.DOT;
import static glslplugin.lang.elements.GLSLTokenTypes.DO_KEYWORD;
import static glslplugin.lang.elements.GLSLTokenTypes.ELSE_KEYWORD;
import static glslplugin.lang.elements.GLSLTokenTypes.EQUAL;
import static glslplugin.lang.elements.GLSLTokenTypes.EQUALITY_OPERATORS;
import static glslplugin.lang.elements.GLSLTokenTypes.EXPRESSION_FIRST_SET;
import static glslplugin.lang.elements.GLSLTokenTypes.FOR_KEYWORD;
import static glslplugin.lang.elements.GLSLTokenTypes.IDENTIFIER;
import static glslplugin.lang.elements.GLSLTokenTypes.IF_KEYWORD;
import static glslplugin.lang.elements.GLSLTokenTypes.INC_OP;
import static glslplugin.lang.elements.GLSLTokenTypes.INTERFACE_QUALIFIER_TOKENS;
import static glslplugin.lang.elements.GLSLTokenTypes.LAYOUT_KEYWORD;
import static glslplugin.lang.elements.GLSLTokenTypes.LEFT_BRACE;
import static glslplugin.lang.elements.GLSLTokenTypes.LEFT_BRACKET;
import static glslplugin.lang.elements.GLSLTokenTypes.LEFT_PAREN;
import static glslplugin.lang.elements.GLSLTokenTypes.MULTIPLICATIVE_OPERATORS;
import static glslplugin.lang.elements.GLSLTokenTypes.OPERATORS;
import static glslplugin.lang.elements.GLSLTokenTypes.OR_OP;
import static glslplugin.lang.elements.GLSLTokenTypes.PLUS;
import static glslplugin.lang.elements.GLSLTokenTypes.PRECISION_KEYWORD;
import static glslplugin.lang.elements.GLSLTokenTypes.PRECISION_QUALIFIER_TOKENS;
import static glslplugin.lang.elements.GLSLTokenTypes.PREPROCESSOR_DEFINE;
import static glslplugin.lang.elements.GLSLTokenTypes.PREPROCESSOR_DIRECTIVES;
import static glslplugin.lang.elements.GLSLTokenTypes.PREPROCESSOR_END;
import static glslplugin.lang.elements.GLSLTokenTypes.PREPROCESSOR_INCLUDE;
import static glslplugin.lang.elements.GLSLTokenTypes.PREPROCESSOR_OTHER;
import static glslplugin.lang.elements.GLSLTokenTypes.PREPROCESSOR_UNDEF;
import static glslplugin.lang.elements.GLSLTokenTypes.QUALIFIER_TOKENS;
import static glslplugin.lang.elements.GLSLTokenTypes.QUESTION;
import static glslplugin.lang.elements.GLSLTokenTypes.RELATIONAL_OPERATORS;
import static glslplugin.lang.elements.GLSLTokenTypes.RETURN_JUMP_STATEMENT;
import static glslplugin.lang.elements.GLSLTokenTypes.RIGHT_ANGLE;
import static glslplugin.lang.elements.GLSLTokenTypes.RIGHT_BRACE;
import static glslplugin.lang.elements.GLSLTokenTypes.RIGHT_BRACKET;
import static glslplugin.lang.elements.GLSLTokenTypes.RIGHT_PAREN;
import static glslplugin.lang.elements.GLSLTokenTypes.SEMICOLON;
import static glslplugin.lang.elements.GLSLTokenTypes.SHARED_KEYWORD;
import static glslplugin.lang.elements.GLSLTokenTypes.STATEMENT_FIRST_SET;
import static glslplugin.lang.elements.GLSLTokenTypes.STRUCT;
import static glslplugin.lang.elements.GLSLTokenTypes.SUBROUTINE_KEYWORD;
import static glslplugin.lang.elements.GLSLTokenTypes.SWITCH_KEYWORD;
import static glslplugin.lang.elements.GLSLTokenTypes.TYPE_SPECIFIER_NONARRAY_TOKENS;
import static glslplugin.lang.elements.GLSLTokenTypes.UNARY_OPERATORS;
import static glslplugin.lang.elements.GLSLTokenTypes.VERTICAL_BAR;
import static glslplugin.lang.elements.GLSLTokenTypes.VOID_TYPE;
import static glslplugin.lang.elements.GLSLTokenTypes.WHILE_KEYWORD;
import static glslplugin.lang.elements.GLSLTokenTypes.XOR_OP;

/**
 * GLSLParsing does all the parsing. It has methods which reflects the rules of the grammar.
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 19, 2009
 *         Time: 3:16:56 PM
 */
public class GLSLParsing extends GLSLParsingBase {
    // The general approach for error return and flagging is that an error should only be returned when not flagged.
    // So, if an error is encountered; EITHER flag it in the editor OR propagate it down the call stack.

    // Traits of all binary operators.
    // They must be listed in order of precedence. (low to high)
    private final static OperatorLevelTraits[] operatorPrecedence = new OperatorLevelTraits[]{
            new OperatorLevelTraits(TokenSet.create(OR_OP), "sub expression", LOGICAL_OR_EXPRESSION),
            new OperatorLevelTraits(TokenSet.create(XOR_OP), "sub expression", LOGICAL_XOR_EXPRESSION),
            new OperatorLevelTraits(TokenSet.create(AND_OP), "sub expression", LOGICAL_AND_EXPRESSION),
            new OperatorLevelTraits(TokenSet.create(VERTICAL_BAR), "sub expression", BINARY_OR_EXPRESSION),
            new OperatorLevelTraits(TokenSet.create(CARET), "sub expression", BINARY_XOR_EXPRESSION),
            new OperatorLevelTraits(TokenSet.create(AMPERSAND), "sub expression", BINARY_AND_EXPRESSION),
            new OperatorLevelTraits(EQUALITY_OPERATORS, "sub expression", EQUALITY_EXPRESSION),
            new OperatorLevelTraits(RELATIONAL_OPERATORS, "sub expression", RELATIONAL_EXPRESSION),
            new OperatorLevelTraits(BIT_SHIFT_OPERATORS, "sub expression", BIT_SHIFT_EXPRESSION),
            new OperatorLevelTraits(ADDITIVE_OPERATORS, "part expression", ADDITIVE_EXPRESSION),
            new OperatorLevelTraits(MULTIPLICATIVE_OPERATORS, "factor", MULTIPLICATIVE_EXPRESSION),
    };

    GLSLParsing(PsiBuilder builder) {
        super(builder);
    }

    //Parsing code

    /**
     * Parses preprocessor, assuming that the getTokenType() is at PREPROCESSOR_BEGIN.
     *
     * Called automatically by {@link GLSLParsingBase}, which means that elements may contain
     * some tokens, that are part of preprocessor, not that element.
     * This may cause trouble during working with the PSI tree, so be careful.
     */
    @Override
    protected void parsePreprocessor() {
        // We can't use tryMatch etc. in here because we'll end up
        // potentially parsing a preprocessor directive inside this one.
        Marker preprocessor = mark();
        advanceLexer(); //Get past the PREPROCESSOR_BEGIN ("#")

        IElementType directiveType = getTokenType();

        if (directiveType == PREPROCESSOR_DEFINE) {
            //Parse define
            advanceLexer();//Get past DEFINE

            final String defineIdentifier = getTokenText();
            if (isValidDefineIdentifier(defineIdentifier)) {
                //Valid

                // Check that the '(' is immediately after identifier, otherwise it is not a function macro.
                // We must check through raw lookup because normal lookup skips whitespace and comments.
                // It should be safe to skip directly to parent, because redefinitions are not allowed.
                final IElementType afterIdentifier = b.parent.rawLookup(1);

                advanceLexer();//Get past identifier

                List<String> arguments = null;
                if (afterIdentifier == LEFT_PAREN) {
                    // Function macro!
                    advanceLexer();

                    arguments = new ArrayList<>();
                    parseArguments: while (true) {
                        final IElementType type = getTokenType();
                        if (type == PREPROCESSOR_END) {
                            error("Expected argument or )");
                            break;
                        }
                        if (type == RIGHT_PAREN) {
                            advanceLexer();
                            break;
                        }
                        if (type == COMMA) {
                            error("Expected argument");
                            advanceLexer();
                            continue;
                        }

                        arguments.add(getTokenText());
                        advanceLexer();

                        while (true) {
                            final IElementType commaType = getTokenType();
                            if (commaType == PREPROCESSOR_END) {
                                error("Expected , or )");
                                break parseArguments;
                            } else if (commaType == RIGHT_PAREN) {
                                advanceLexer();
                                break parseArguments;
                            } else if (commaType == COMMA) {
                                advanceLexer();
                                continue parseArguments;
                            } else {
                                error("Expected , or )");
                                advanceLexer();
                            }
                        }
                    }
                }

                List<ForeignLeafType> definition = new ArrayList<>();

                // to allow "nested" defines
                b.setAllowRedefinitions(true);
                try {
                    while (getTokenType() != PREPROCESSOR_END && !eof()) {
                        definition.add(new ForeignLeafType(getTokenType(), getTokenText()));
                        advanceLexer();
                    }
                } finally {
                    b.setAllowRedefinitions(false);
                }
                b.define(defineIdentifier, arguments, definition);
            } else {
                //Invalid
                error("Identifier expected.");
                //Eat rest
                while (!eof()) {
                    if (getTokenType() == PREPROCESSOR_END) {
                        break;
                    }
                    advanceLexer();
                }
            }
        } else if (directiveType == PREPROCESSOR_UNDEF) {
            //Parse undefine
            advanceLexer();//Get past UNDEF

            if(isValidDefineIdentifier(getTokenText())){
                //Valid
                b.define(getTokenText(), null, null);

                advanceLexer();//Get past IDENTIFIER
            }else{
                //Invalid
                error("Identifier expected.");
            }
            //Eat rest
            while (!eof()) {
                if (getTokenType() == PREPROCESSOR_END) {
                    break;
                }else{
                    error("Unexpected token.");
                }
                advanceLexer();
            }
        } else if (directiveType == PREPROCESSOR_INCLUDE) {
            // Since there is no standard, just eat whatever and hope that there is a string with path somewhere in there
            while (!eof()) {
                if (getTokenType() == PREPROCESSOR_END) {
                    break;
                }
                advanceLexer();
            }
        } else {
            //Some other directive, no work here
            while (!eof()) {
                if (getTokenType() == PREPROCESSOR_END) {
                    break;
                }
                advanceLexer();
            }
        }
        advanceLexer();// Get past PREPROCESSOR_END
        if (PREPROCESSOR_DIRECTIVES.contains(directiveType)) {
            preprocessor.done(directiveType);
        } else {
            // Happens when typing new directive at the end of the file (incomplete code)
            // or when malformed/custom directive is created (eg #foo)
            preprocessor.done(PREPROCESSOR_OTHER);
        }
    }

    private static final Pattern IDENTIFIER_REGEX = Pattern.compile("[_a-zA-Z][_a-zA-Z0-9]*");
    private boolean isValidDefineIdentifier(String text){
        if(text == null)return false;
        return IDENTIFIER_REGEX.matcher(text).matches();
    }

    /**
     * Entry for parser. Tries to parse whole shader file.
     */
    public void parseTranslationUnit() {
        // translation_unit: external_declaration*

        while (!eof()) {
            if (!parseExternalDeclaration()) {
                advanceLexer();
                error("Unable to parse external declaration.");
            }
        }
    }

    /**
     * Parse whatever can be at the top of the file hierarchy
     */
    private boolean parseExternalDeclaration() {
        // external_declaration: function_definition
        //                     | declaration
        //                     | interface_block
        // Expanding the rule to obtain:
        // external_declaration: qualifier-list type-specifier prototype [ ';' | compound-statement ]
        //                     | qualifier-list type-specifier declarator-list ';'
        //                     | qualifier-list IDENTIFIER '{' (member'}' [ IDENTIFIER array-specifier? ]';'
        //                     | layout_qualifier interface_qualifier ;
        //
        // A common prefix for all: qualifier-list - layout_qualifier is included in qualifier_list,
        // but can be standalone
        //
        // Note: after type-specifier, we only need to look up IDENTIfIER '(' to determine
        //       whether or not it is a prototype or a declarator-list.

        Marker mark = mark();

        // This bunch of conditionals are responsible to handle really invalid input.
        // Specifically, when getTokenType() is not in the first set of external-declaration
        // Please add more if found lacking.
        // TODO: Add something similar to parseStatement
        if (getTokenType() == LEFT_PAREN ||
                CONSTANT_TOKENS.contains(getTokenType()) ||
                UNARY_OPERATORS.contains(getTokenType())) {
            parseExpression();
            tryMatch(SEMICOLON);
            mark.error("Expression not allowed here.");
            return true;
        }
        String text = getTokenText();
        if (getTokenType() == IF_KEYWORD ||
                getTokenType() == FOR_KEYWORD ||
                getTokenType() == WHILE_KEYWORD ||
                getTokenType() == DO_KEYWORD) {
            parseSimpleStatement();
            mark.error("'" + text + "' statement not allowed here.");
            return true;
        }
        if (tryMatch(RIGHT_PAREN, RIGHT_BRACE, RIGHT_ANGLE, RIGHT_BRACKET, COMMA)) {
            mark.error("Unexpected token '" + text + "'.");
            return true;
        }
        while (tryMatch(OPERATORS)) {
            mark.error("Unexpected token '" + text + "'.");
            mark = mark();
        }

        if (parsePrecisionStatement()) {
            mark.drop();
            return true;
        }

        if (parseLayoutQualifierStatement()){
            mark.drop();
            return true;
        }

        parseQualifierList(true);

        if (getTokenType() == IDENTIFIER && lookAhead(1) == LEFT_BRACE) { // interface block
            //TODO Make sure that this is preceded by storage_qualifier
            parseIdentifier();
            match(LEFT_BRACE, "Expected '{'");

            if (getTokenType() == RIGHT_BRACE) {
                error("Empty interface block is not allowed.");
            }

            while (!tryMatch(RIGHT_BRACE) && !eof()) {
                final Marker member = mark();
                parseQualifierList(true);
                if (!parseTypeSpecifier()) advanceLexer();
                parseDeclaratorList();
                match(SEMICOLON, "Expected ';'");
                member.done(STRUCT_MEMBER_DECLARATION);//TODO Should we call interface block members struct members?
            }

            if (getTokenType() == IDENTIFIER) {
                parseIdentifier();
                if (getTokenType() == LEFT_BRACKET) {
                    parseArrayDeclarator();
                }
            }
            match(SEMICOLON, "Expected ';'");
            mark.done(INTERFACE_BLOCK);
            return true;
        }

        parseTypeSpecifier();
        Marker postType = mark();

        if (getTokenType() == SEMICOLON) {
            // Declaration with no declarators.
            // (struct definitions will look like this)
            postType.drop();
            parseDeclaratorList(); // the list will always be empty.
            match(SEMICOLON, "Missing ';'");
            mark.done(VARIABLE_DECLARATION);
            return true;

        } else if (getTokenType() == IDENTIFIER || getTokenType() == LEFT_PAREN) {
            // Identifier means either declarators, or function declaration/definition
            match(IDENTIFIER, "Missing function name");

            if (getTokenType() == SEMICOLON ||
                    getTokenType() == COMMA ||
                    getTokenType() == LEFT_BRACKET ||
                    getTokenType() == EQUAL) {
                // These are valid operatorTokens after an identifier in a declarator.
                // ... try to parse declarator-list!
                postType.rollbackTo();
                parseDeclaratorList();
                match(SEMICOLON, "Missing ';' after variable declaration");
                mark.done(VARIABLE_DECLARATION);
                return true;

            } else if (tryMatch(LEFT_PAREN)) {
                // Left parenthesis '('
                // This must be a function declaration or definition, parse the prototype first!
                postType.rollbackTo();

                parseIdentifier();

                match(LEFT_PAREN, "Expected '(' after function identifier.");
                {
                    // parameter_declaration_list: <nothing>
                    //                           | VOID
                    //                           | parameter_declaration (',' parameter_declaration)*

                    //noinspection StatementWithEmptyBody
                    if (tryMatch(VOID_TYPE)) {
                        // Do nothing.
                    } else if (getTokenType() != RIGHT_PAREN) {
                        do {
                            parseParameterDeclaration();
                        } while (tryMatch(COMMA));
                    }
                }
                match(RIGHT_PAREN, "Missing ')' after function prototype");

                // Prototype is now done, so look for ';' or '{'

                if (tryMatch(SEMICOLON)) {
                    mark.done(FUNCTION_DECLARATION);
                } else if (getTokenType() == LEFT_BRACE) {
                    parseCompoundStatement();
                    mark.done(FUNCTION_DEFINITION);
                } else {
                    // Neither ';' nor '{' found, mark as a prototype with missing ';'
                    mark.done(FUNCTION_DECLARATION);
                    error("Missing ';' after function declaration.");
                }
                return true;
            } else if (TYPE_SPECIFIER_NONARRAY_TOKENS.contains(getTokenType())) {
                // simulate declarators, and return success to make parsing continue.
                postType.done(IDENTIFIER);
                postType = postType.precede();
                postType.done(DECLARATOR);
                postType = postType.precede();
                postType.done(DECLARATOR_LIST);
                mark.done(VARIABLE_DECLARATION);
                error("Missing ';' after variable declaration.");
                return true;
            }
        } else if (GLSLTokenTypes.OPERATORS.contains(getTokenType()) ||
                getTokenType() == DOT ||
                getTokenType() == LEFT_BRACKET) {
            // this will handle most expressions
            postType.drop();
            mark.rollbackTo();
            mark = mark();
            if (!parseExpression()) {
                //There is no expression! Consume what triggered me. (Would lead to infinite loop otherwise)
                advanceLexer();
            }
            tryMatch(SEMICOLON);
            mark.error("Expression not allowed here.");
            return true;
        } else if (GLSLTokenTypes.FLOW_KEYWORDS.contains(getTokenType()) ||
                GLSLTokenTypes.CONSTANT_TOKENS.contains(getTokenType())) {
            postType.drop();
            text = getTokenText();
            advanceLexer();
            mark.error("Unexpected '" + text + "'");
            return true;
        } else if (TYPE_SPECIFIER_NONARRAY_TOKENS.contains(getTokenType())) {
            // simulate declarators, and return success to make parsing continue.
            postType.done(DECLARATOR_LIST);
            mark.done(VARIABLE_DECLARATION);
            error("Missing ';' after declaration.");
            return true;
        }

        mark.rollbackTo();
        return false;
    }

    private boolean parsePrecisionStatement() {
        // precision_statement: PRECISION precision_qualifier type_specifier_no_precision ;
        if (getTokenType() == PRECISION_KEYWORD) {
            final Marker mark = mark();
            advanceLexer();

            if (!tryMatch(PRECISION_QUALIFIER_TOKENS)) {
                error("Expected precision qualifier.");
            }

            if (!parseTypeSpecifier()) {
                error("Expected type specifier.");
            }
            match(SEMICOLON, "Expected ';'");
            mark.done(PRECISION_STATEMENT);
            return true;
        } else return false;
    }

    private boolean parseQualifiedTypeSpecifier() {
        parseQualifierList(true);
        boolean result = parseTypeSpecifier();
        parseQualifierList(false);
        return result;
    }

    private void parseParameterDeclaration() {
        // parameter_declaration: [parameter_qualifier] [type_qualifier] IDENTIFIER [array_declarator]
        final Marker mark = mark();

        parseQualifiedTypeSpecifier();

        if (getTokenType() == IDENTIFIER) {
            parseStructOrParameterDeclarator(PARAMETER_DECLARATOR);
        } else {
            // Fake a declarator.
            Marker mark2 = mark();
            mark2.done(PARAMETER_DECLARATOR);
        }

        mark.done(PARAMETER_DECLARATION);
    }

    private void parseCompoundStatement() {
        // compound_statement: '{' '}'
        //                   | '{' statement_list '}'
        Marker mark = mark();
        match(LEFT_BRACE, "'{' expected.");
        if (eof(mark)) return;
        if (getTokenType() != RIGHT_BRACE) {
            parseStatementList();
        }
        if (eof()) {
            mark.drop();
        } else {
            match(RIGHT_BRACE, "'}' expected.");
            mark.done(COMPOUND_STATEMENT);
        }
    }

    private void parseStatementList() {
        // statement_list: statement*
        // NOTE: terminates with '}', but we check for FirstSet(statement)
        //       instead for increased robustness

        while ((STATEMENT_FIRST_SET.contains(getTokenType()) || OPERATORS.contains(getTokenType()) || getTokenType() == PRECISION_KEYWORD) && !eof()) {
            if (!parseStatement()) {
                return;
            }
        }
    }

    private boolean parseStatement() {
        // statement: simple_statement | compound_statement

        if (getTokenType() == LEFT_BRACE) {
            parseCompoundStatement();
            return true;
        }
        if (parseSimpleStatement()) {
            return true;
        } else {
            error("Expected a statement.");
            return false;
        }
    }

    private static final TokenSet VALID_FIRST_OPERATORS = TokenSet.create(INC_OP, DEC_OP, PLUS, DASH);

    private void eatInvalidOperators() {
        Marker mark = mark();
        while (OPERATORS.contains(getTokenType()) && !VALID_FIRST_OPERATORS.contains(getTokenType())) {
            String operator = getTokenText();
            advanceLexer();
            mark.error("Unexpected operator '" + operator + "'.");
            mark = mark();
        }
        mark.drop();
    }

    // https://github.com/KhronosGroup/GLSL/blob/master/extensions/ext/GL_EXT_control_flow_attributes.txt
    private void tryParseFlowAttributes() {
        // These are currently not semantically processed at all
        final IElementType type = getTokenType();
        if (type != LEFT_BRACKET) {
            return;
        }
        final Marker mark = mark();
        advanceLexer();
        if (getTokenType() != LEFT_BRACKET) {
            mark.rollbackTo();
            return;
        }
        advanceLexer();

        // We are not very particular about this, just consume tokens until we get to ]]
        //TODO: More precise parsing
        while (getTokenType() != RIGHT_BRACKET && !eof()) {
            advanceLexer();
        }
        advanceLexer();
        if (getTokenType() != RIGHT_BRACKET) {
            mark.rollbackTo();
            return;
        }
        advanceLexer();
        mark.done(FLOW_ATTRIBUTE);
    }

    private boolean parseSimpleStatement() {
        // simple_statement: declaration_statement
        //                 | expression_statement
        //                 | selection_statement
        //                 | switch_statement
        //                 | iteration_statement
        //                 | jump_statement
        tryParseFlowAttributes();
        eatInvalidOperators();

        final IElementType type = getTokenType();
        boolean result;

        if (EXPRESSION_FIRST_SET.contains(type) || QUALIFIER_TOKENS.contains(type) || type == PRECISION_KEYWORD) {
            // This set also includes the first set of declaration_statement
            if (lookaheadDeclarationStatement()) {
                result = parseDeclarationStatement();
            } else {
                result = parseExpressionStatement();
            }
        } else if (type == IF_KEYWORD) {
            result = parseSelectionStatement();
        } else if (type == SWITCH_KEYWORD) {
            result = parseSwitchStatement();
        } else if (type == WHILE_KEYWORD) {
            result = parseWhileIterationStatement();
        } else if (type == DO_KEYWORD) {
            result = parseDoIterationStatement();
        } else if (type == FOR_KEYWORD) {
            result = parseForStatement();
        } else if (type == BREAK_JUMP_STATEMENT) {
            result = parseBreakStatement();
        } else if (type == DISCARD_JUMP_STATEMENT) {
            result = parseDiscardStatement();
        } else if (type == RETURN_JUMP_STATEMENT) {
            result = parseReturnStatement();
        } else if (type == CONTINUE_JUMP_STATEMENT) {
            result = parseContinueStatement();
        } else if (type == CASE_KEYWORD) {
            result = parseCaseStatement();
        } else if (type == DEFAULT_KEYWORD) {
            result = parseDefaultStatement();
        } else {
            return false;
        }
        return result;
    }

    private boolean parseReturnStatement() {
        // return_statement: 'return' [expression] ';'
        Marker mark = mark();
        match(RETURN_JUMP_STATEMENT, "Missing 'return'.");
        if (getTokenType() != SEMICOLON) {
            parseExpression();
            match(SEMICOLON, "Missing ';' after expression.");
        } else {
            match(SEMICOLON, "Missing ';' after 'return'.");
        }
        mark.done(RETURN_STATEMENT);
        return true;
    }

    private boolean parseContinueStatement() {
        // discard_statement: 'continue' ';'
        Marker mark = mark();
        match(CONTINUE_JUMP_STATEMENT, "Missing 'continue'.");
        match(SEMICOLON, "Missing ';' after 'continue'.");
        mark.done(CONTINUE_STATEMENT);
        return true;
    }

    private boolean parseDiscardStatement() {
        // discard_statement: 'discard' ';'
        Marker mark = mark();
        match(DISCARD_JUMP_STATEMENT, "Missing 'discard'.");
        match(SEMICOLON, "Missing ';' after 'discard'.");
        mark.done(DISCARD_STATEMENT);
        return true;
    }

    private boolean parseBreakStatement() {
        // break_statement: 'break' ';'
        Marker mark = mark();
        match(BREAK_JUMP_STATEMENT, "Missing 'break'.");
        match(SEMICOLON, "Missing ';' after 'break'.");
        mark.done(BREAK_STATEMENT);
        return true;
    }

    private boolean parseCaseStatement() {
        // case_statement: 'case' constant_expression ':'
        Marker mark = mark();
        match(CASE_KEYWORD, "Expected 'case'");
        parseConstantExpression();
        match(COLON, "Expected ':'");
        mark.done(CASE_STATEMENT);
        return true;
    }

    private boolean parseDefaultStatement() {
        // default_statement: 'default' ':'
        Marker mark = mark();
        match(DEFAULT_KEYWORD, "Expected 'case'");
        match(COLON, "Expected ':'");
        mark.done(DEFAULT_STATEMENT);
        return true;
    }

    private boolean parseForStatement() {
        // for_iteration_statement: 'for' '(' for_init_statement for_rest_statement ')' statement_no_new_scope
        // NOTE: refactored to:
        // for_iteration_statement: 'for' '(' (expression statement|declaration statement) condition? ';' expression? ')'
        // condition:
        //              expression
        //              fully_specified_type IDENTIFIER '=' initializer
        Marker mark = mark();

        match(FOR_KEYWORD, "Missing 'for'.");
        match(LEFT_PAREN, "Missing '(' after 'for'.");

        parseForInitStatement();

        if (getTokenType() != SEMICOLON) {
            parseCondition();
        }
        match(SEMICOLON, "Missing ';' after condition statement.");

        if (getTokenType() != RIGHT_PAREN) {
            // Only parse the expression if it is present.
            parseExpression();
        }

        match(RIGHT_PAREN, "Missing ')' after 'for'.");
        parseStatement();

        mark.done(FOR_STATEMENT);
        return true;
    }

    private boolean lookaheadConditionDeclaration(){
        final Marker rollback = mark();
        try {
            if(!parseQualifiedTypeSpecifier()){
                return false;
            }
            if(!tryMatch(IDENTIFIER)){
                return false;
            }
            if(!tryMatch(EQUAL)){
                return false;
            }
            //At this point we can be pretty confident that this is a condition-style declaration
            return true;
        } finally {
            rollback.rollbackTo();
        }
    }

    private void parseCondition() {
        // condition: expression
        //          | fully_specified_type IDENTIFIER '=' initializer
        // NOTE: The spec, allows the condition expression in 'for' and 'while' loops
        //       to declare a single variable.

        Marker conditionMark = mark();
        if (lookaheadConditionDeclaration()) {
            Marker mark = mark();

            parseQualifiedTypeSpecifier();

            Marker list = mark();
            Marker declarator = mark();

            parseIdentifier();
            match(EQUAL, "Missing '=' in condition initializer.");
            parseInitializer();

            declarator.done(DECLARATOR);
            list.done(DECLARATOR_LIST);

            mark.done(VARIABLE_DECLARATION);
        }else{
            if(!parseExpression()){
                conditionMark.error("Expression or single variable declaration expected.");
                return;
            }
        }
        conditionMark.done(CONDITION);
    }

    private void parseForInitStatement() {
        // for_init_statement: expression_statement | declaration_statement

        if(tryMatch(SEMICOLON)){
            //Empty statement, don't need to parse further
            return;
        }

        Marker rollback = mark();
        if(lookaheadDeclarationStatement() && parseDeclarationStatement()){
            rollback.drop();
            return;
        }else{
            rollback.rollbackTo();
            rollback = mark();
        }

        if(parseExpressionStatement()){
            rollback.drop();
            return;
        }else{
            rollback.rollbackTo();
            rollback = mark();
        }

        rollback.error("Failed to parse for-init statement.");
    }

    private boolean parseDoIterationStatement() {
        // do_iteration_statement: 'do' statement 'while' '(' expression ')' ';'
        Marker mark = mark();

        match(DO_KEYWORD, "Missing 'do'.");
        parseStatement();
        match(WHILE_KEYWORD, "Missing 'while'.");
        match(LEFT_PAREN, "Missing '(' after 'while'.");
        parseCondition();
        match(RIGHT_PAREN, "Missing ')' after 'while'.");
        match(SEMICOLON, "Missing ';' after 'do-while'.");

        mark.done(DO_STATEMENT);
        return true;
    }

    private boolean parseWhileIterationStatement() {
        // while_iteration_statement: 'while' '(' expression ')' statement
        Marker mark = mark();

        match(WHILE_KEYWORD, "Missing 'while'.");
        match(LEFT_PAREN, "Missing '(' after 'while'.");
        parseCondition();
        match(RIGHT_PAREN, "Missing ')' after 'while'.");
        parseStatement();

        mark.done(WHILE_STATEMENT);
        return true;
    }

    private boolean parseSelectionStatement() {
        // selection_statement: 'if' '(' expression ')' statement [ 'else' statement ]
        Marker mark = mark();

        match(IF_KEYWORD, "Missing 'if'.");
        match(LEFT_PAREN, "Missing '(' after 'if'.");
        parseCondition();
        match(RIGHT_PAREN, "Missing ')' after 'if'.");
        parseStatement();
        tryParseElsePart();

        mark.done(IF_STATEMENT);
        return true;
    }

    private void tryParseElsePart() {
        // else_part: (nothing) | 'else' statement
        if (tryMatch(ELSE_KEYWORD)) {
            parseStatement();
        }
    }

    private boolean parseSwitchStatement() {
        // switch_statement: 'switch' '(' expression ')' '{' statement_list? '}'
        Marker mark = mark();
        match(SWITCH_KEYWORD, "Expected 'switch'");
        match(LEFT_PAREN, "Expected '('");
        parseExpression();
        match(RIGHT_PAREN, "Expected ')'");
        parseCompoundStatement();
        mark.done(SWITCH_STATEMENT);
        return true;
    }

    private boolean parseExpressionStatement() {
        // expression_statement: [expression] ';'
        Marker mark = mark();

        //noinspection StatementWithEmptyBody
        if (tryMatch(SEMICOLON)) {
            // empty statement
        } else {
            if (!parseExpression()) {
                mark.drop();
                return false;
            }
            match(SEMICOLON, "Missing ';' after expression.");
        }
        mark.done(EXPRESSION_STATEMENT);
        return true;
    }

    private boolean parseDeclarationStatement() {
        // declaration_statement: declaration
        //                        precision_statement
        if(getTokenType() == PRECISION_KEYWORD){
            return parsePrecisionStatement();
        }

        Marker mark = mark();

        if (!parseDeclaration()) {
            mark.error("Expected declaration.");
            return false;
        } else {
            match(SEMICOLON, "Expected ';' after declaration statement.");
            mark.done(DECLARATION_STATEMENT);
            return true;
        }
    }

    /**
     * Looks ahead to determine whether a simple_statement is a
     * declaration_statement or expression_statement.
     *
     * @return true if it is a declaration statement, false otherwise
     */
    private boolean lookaheadDeclarationStatement() {
        //Precision statement is a type of declaration statement (GLSL 4.30)
        if(getTokenType() == PRECISION_KEYWORD)return true;

        // they share type_specifier. So if found; look for the following identifier.
        Marker rollback = mark();
        try {
            if (tryMatch(QUALIFIER_TOKENS)) {
                return true;
            }
            if (!parseTypeSpecifier()) {
                return false;
            }
            //noinspection RedundantIfStatement
            if (tryMatch(IDENTIFIER) || tryMatch(SEMICOLON)) {
                return true;
            }

            return false;
        } finally {
            rollback.rollbackTo();
        }
    }

    private boolean parseDeclaration() {
        // declaration: function_prototype SEMICOLON
        //            | init_declarator_list SEMICOLON

        Marker mark = mark();

        if (parseQualifiedTypeSpecifier()) {
            parseDeclaratorList();
            mark.done(VARIABLE_DECLARATION);
            return true;
        } else {
            mark.error("Qualified type specifier expected.");
            return false;
        }
    }

    private void parseDeclaratorList() {
        // init_declarator_list: fully_specified_type
        //                     | fully_specified_type declarator ( ',' declarator )*
        Marker mark = mark();
        if (getTokenType() == IDENTIFIER) {
            do {
                // declarator: IDENTIFIER [ '[' [ constant_expression ] ']' ] [ '=' initializer ]
                final Marker mark1 = mark();
                parseIdentifier();
                if (getTokenType() == LEFT_BRACKET) {
                    parseArrayDeclarator();
                }
                if (tryMatch(EQUAL)) {
                    parseInitializer();
                }
                mark1.done(DECLARATOR);
            } while (tryMatch(COMMA));
        }
        mark.done(DECLARATOR_LIST);
    }

    private void parseArrayDeclarator() {
        do{
            final Marker mark = mark();

            match(LEFT_BRACKET, "Expected '['.");
            if (getTokenType() != RIGHT_BRACKET) {
                parseConstantExpression();
            }
            match(RIGHT_BRACKET, "Missing closing ']' after array declarator.");

            mark.done(ARRAY_DECLARATOR);
        }while(getTokenType() == LEFT_BRACKET); //Parse all ARRAY_DECLARATOR's if multidimensional array
    }

    private boolean parseInitializer() {
        // initializer: assignment_expression
        if (getTokenType() == LEFT_BRACE) {
            parseInitializerList();
        } else {
            Marker mark = mark();
            if (!parseAssignmentExpression()) {
                mark.error("Expected initializer");
                return false;
            }
            mark.done(INITIALIZER);
        }
        return true;
    }

    private void parseInitializerList() {
        // initializer_list: '{' initializer (',' initializer)* ','? '}'
        Marker mark = mark();

        match(LEFT_BRACE, "Expected '{'");

        if (getTokenType() != RIGHT_BRACE) parseInitializer();

        while (getTokenType() != RIGHT_BRACE && !eof()) {
            match(COMMA, "Expected '}' or ','");
            if (getTokenType() == RIGHT_BRACE) break;
            if (!parseInitializer()) { advanceLexer(); }
        }

        match(RIGHT_BRACE, "Expected '}'");

        mark.done(INITIALIZER_LIST);
    }

    private boolean parseAssignmentExpression() {
        // assignment_expression: conditional_expression
        //                      | unary_expression assignment_operator assignment_expression
        // NOTE: both conditional_expression and assignment_expression starts with unary_expression
        // CHANGED TO: (to reduce the need for lookahead. use the annotation pass to verify l-values)
        // assignment_expression: conditional_expression (assignment_operator conditional_expression)*
        Marker mark = mark();

        if (!parseConditionalExpression()) {
            mark.drop();
            return false;
        }

        while (tryMatch(ASSIGNMENT_OPERATORS)) {
            parseConditionalExpression();
            mark.done(ASSIGNMENT_EXPRESSION);
            mark = mark.precede();
        }

        mark.drop();
        return true;
    }

    private boolean parseConditionalExpression() {
        // conditional_expression: logical_or_expression
        //                       | logical_or_expression QUESTION expression COLON assignment_expression
        Marker mark = mark();

        if (!parseOperatorExpression()) {
            mark.drop();
            return false;
        }

        if (tryMatch(QUESTION)) {
            parseExpression();
            match(COLON, "Missing ':' in ternary operator ?:.");
            parseAssignmentExpression();
            mark.done(CONDITIONAL_EXPRESSION);
        } else {
            mark.drop();
        }
        return true;
    }

    private boolean parseExpression() {
        // experssion: assignment_expression
        //           | expression COMMA assignment_expression
        // transformed to:
        // expression: assignment_expression (',' assignment_expression)*

        Marker mark = mark();

        if (!parseAssignmentExpression()) {
            mark.error("Expected an expression.");
            return false;
        }

        if (tryMatch(COMMA)) {
            do {
                if (!parseAssignmentExpression()) {
                    mark.error("Expected an expression.");
                    return false;
                }
            } while (tryMatch(COMMA));
            mark.done(SEQUENCE_EXPRESSION);
        } else {
            mark.drop();
        }
        return true;
    }

    private boolean parseOperatorExpression() {
        return parseOperatorExpressionLevel(0);
    }

    private boolean parseOperatorExpression(int level) {
        Marker mark = mark();
        if (!parseOperatorExpressionLevel(level + 1)) {
            mark.drop();
            return false;
        }

        final OperatorLevelTraits operatorLevel = operatorPrecedence[level];
        while (tryMatch(operatorLevel.getOperatorTokens())) {
            if (parseOperatorExpressionLevel(level + 1)) {
                mark.done(operatorLevel.getElementType());
                mark = mark.precede();
            } else {
                Marker operatorMark = mark();
                outOfPlace:
                if (tryMatch(OPERATORS)) {
                    do {
                        operatorMark.error("Operator out of place.");
                        if (parseOperatorExpressionLevel(level + 1)) {
                            mark.done(operatorLevel.getElementType());
                            mark = mark.precede();
                            break outOfPlace;
                        } else {
                            operatorMark = mark();
                        }
                    } while (tryMatch(OPERATORS));
                    operatorMark.drop();
                } else {
                    operatorMark.drop();
                    mark.error("Expected a(n) " + operatorLevel.getPartName() + ".");
                    return false;
                }
            }
        }
        mark.drop();
        return true;
    }

    private boolean parseOperatorExpressionLevel(int level) {
        if (level == operatorPrecedence.length) {
            return parseUnaryExpression();
        } else {
            return parseOperatorExpression(level);
        }
    }

    private boolean parseUnaryExpression() {
        // unary_expression: postfix_expression
        //                 | unary_operator unary_expression
        // note: moved INC_OP and DEC_OP to unary_operator
        Marker mark = mark();

        if (tryMatch(UNARY_OPERATORS)) {
            parseUnaryExpression();
            mark.done(PREFIX_OPERATOR_EXPRESSION);
            return true;
        } else if (parsePostfixExpression()) {
            mark.drop();
            return true;
        } else {
            mark.drop();
            return false;
        }
    }

    private boolean parsePostfixExpression() {
        // postfix_expression: primary_expression
        //                   | postfix_expression '[' expression ']'
        //                   | function_call
        //                   | postfix_expression '.' FIELD_SELECTION
        //                   | postfix_expression INC_OP
        //                   | postfix_expression DEC_OP
        // (moved from function_or_method_call:)
        //                   | postfix_expression '.' function_call
        Marker mark = mark();
        boolean result;
        if (lookAheadFunctionCall(true)) {
            result = parseFunctionCall();
        } else {
            result = parsePrimaryExpression();
        }
        if (!result) {
            mark.drop();
            return false;
        }
        while (true) {
            if (tryMatch(LEFT_BRACKET)) {
                parseExpression();
                match(RIGHT_BRACKET, "Missing ']' after subscript.");
                mark.done(SUBSCRIPT_EXPRESSION);
            } else if (tryMatch(DOT)) {
                if (lookAheadFunctionCall(false)) {
                    parseFunctionCallImpl(true);
                    mark.done(METHOD_CALL_EXPRESSION);
                } else {
                    parseIdentifier();
                    mark.done(FIELD_SELECTION_EXPRESSION);
                }
            } else if (tryMatch(INC_OP) || tryMatch(DEC_OP)) {
                // do nothing as tryMatch consumes the token for us.
                mark.done(POSTFIX_OPERATOR_EXPRESSION);
            } else {
                break;
            }
            mark = mark.precede();
        }
        mark.drop();
        return true;
    }

    /**
     * Figures out whether the next sequence of tokens denotes a function call and not a field selection.
     *
     * @return true if the immediately approaching tokens contain a function call, false otherwise
     */
    private boolean lookAheadFunctionCall(boolean allowConstructors) {
        Marker mark = mark();
        boolean result = false;

        if (tryMatch(TYPE_SPECIFIER_NONARRAY_TOKENS)) {
            result = true;
        } else if (tryMatch(IDENTIFIER)) {
            if(allowConstructors && getTokenType() == LEFT_BRACKET){
                parseArrayDeclarator();
            }
            if (tryMatch(LEFT_PAREN)) {
                result = true;
            }
        }

        mark.rollbackTo();
        return result;
    }

    private boolean parseFunctionCall() {
        Marker mark = mark();
        parseFunctionCallImpl(false);
        mark.done(FUNCTION_CALL_EXPRESSION);
        return true;
    }

    private void parseFunctionCallImpl(boolean markIdentifierAsMethodIdentifier) {
        // parse_function_call : parse_function_call_or_method
        // parse_function_call_or_method: function_call_generic
        //                              | postfix_expression '.' function_call_generic
        // NOTE: implementing function_call_or_method_directly
        // AND:  postfix_expression '.' function_call_generic is moved to parsePostfixExpression

        parseFunctionIdentifier(markIdentifierAsMethodIdentifier);
        match(LEFT_PAREN, "Missing '('.");
        parseParameterList();
        match(RIGHT_PAREN, "Missing ')'.");
    }

    /**
     * Parses a function, method or constructor identifier.
     * If method identifier is requested, METHOD_NAME is always produced.
     * If function identifier is requested, either TYPE_SPECIFIER or FUNCTION_NAME is emitted.
     * Additionally, in function/constructor mode, one or more ARRAY_DECLARATOR's may be emitted.
     * That is because it might be a struct array constructor.
     *
     * @param method true -> method mode | false -> function/constructor mode
     */
    private boolean parseFunctionIdentifier(boolean method) {
        // function_identifier: IDENTIFIER                          //function/method call
        //                    | type_name [ array_declarator ]      //constructor

        if(!method){
            //Methods can't be constructors
            Marker constructorMark = mark();
            if(parseTypeSpecifier(true)){//true -> only built-in type specifiers
                //Success, it is definitely a constructor
                constructorMark.drop();// (parseTypeSpecifier has added a type specifier element)
                return true;
            }else{
                constructorMark.rollbackTo();
            }
        }

        if(tryMatch(IDENTIFIER)){
            //Function/method call

            //Search for "[x]" AFTER marking the IDENTIFIER, because it is not part of the identifier
            if(!method && getTokenType() == LEFT_BRACKET){
                //If it is a constructor, it may be an array constructor.
                parseArrayDeclarator();
            }
            return true;
        }else{
            mark().error(method ? "Expected method identifier." : "Expected function identifier.");
            return false;
        }
    }

    private void parseParameterList() {
        // parameter_list: VOID | (nothing)
        //               | assignment_expression (',' assignment_expression)
        Marker mark = mark();

        if (getTokenType() == VOID_TYPE) {
            advanceLexer();
        } else //noinspection StatementWithEmptyBody
            if (getTokenType() == RIGHT_PAREN) {
                // do nothing
            } else if (parseAssignmentExpression()) {
                while (tryMatch(COMMA)) {
                    if (!parseAssignmentExpression()) {
                        error("Assignment expression expected.");
                        break;
                    }
                }
            } else {
                mark.error("Expression expected after '('.");
                return;
            }

        mark.done(PARAMETER_LIST);
    }

    private boolean parsePrimaryExpression() {
        // primary_expression: variable_identifier
        //                   | CONSTANT
        //                   | '(' expression ')'
        final Marker mark = mark();

        final IElementType type = getTokenType();
        if (type == IDENTIFIER) {
            advanceLexer();
            mark.done(VARIABLE_NAME_EXPRESSION);
            return true;
        } else if (tryMatch(CONSTANT_TOKENS)) {
            mark.done(CONSTANT_EXPRESSION);
            return true;
        } else if (type == LEFT_PAREN) {
            advanceLexer();
            if (!parseExpression()) {
                if (getTokenType() == RIGHT_PAREN) {
                    error("Expected expression after '('");
                } else {
                    mark.error("Expected expression after '('");
                    return false;
                }
            }
            match(RIGHT_PAREN, "Missing ')'");
            mark.done(GROUPED_EXPRESSION);
            return true;
        } else {
            mark.error("Expected constant, variable identifier or a '(' ')' group");
            return false;
        }
    }

    private void parseIdentifier() {
        if (getTokenType() == IDENTIFIER) {
            advanceLexer();
        } else {
            mark().error("Expected an identifier.");
        }
    }

    /**
     * Parse all allowed type specifiers
     */
    private boolean parseTypeSpecifier(){
        return parseTypeSpecifier(false);
    }

    /**
     * Parse type specifier.
     * onlyBuildIn can be used to accept as types only tokens in TYPE_SPECIFIER_NONARRAY_TOKENS.
     * This can be used in for example parsing constructors.
     *
     * @param onlyBuiltIn if true, only build-in type specifiers are considered valid
     */
    private boolean parseTypeSpecifier(boolean onlyBuiltIn) {
        // type_specifier_noarray
        // type_specifier_noarray "[" const_expr "]"

        final Marker mark = mark();

        if (!parseTypeSpecifierNoArray(onlyBuiltIn)) {
            mark.drop();
            return false;
        }

        if (getTokenType() == LEFT_BRACKET) {
            parseArrayDeclarator();
        }
        mark.done(TYPE_SPECIFIER);
        return true;
    }

    private boolean parseConstantExpression() {
        // constant_expression: conditional_expression
        return parseConditionalExpression();
    }

    private boolean parseTypeSpecifierNoArray(boolean onlyBuiltIn) {
        // type_specifier_noarray: all_built_in_types
        //                       | struct_specifier
        //                       | type_name
        // todo: implement       | INVARIANT IDENTIFIER  (vertex only)
        // note: This also accepts IDENTIFIERS

        final Marker mark = mark();

        if (!onlyBuiltIn && getTokenType() == STRUCT) {
            parseStructSpecifier();
            mark.done(TYPE_SPECIFIER_STRUCT);
        } else if (TYPE_SPECIFIER_NONARRAY_TOKENS.contains(getTokenType())) {
            advanceLexer();
            mark.done(TYPE_SPECIFIER_PRIMITIVE);
        } else if (!onlyBuiltIn && getTokenType() == IDENTIFIER) {
            parseIdentifier();
            mark.done(TYPE_SPECIFIER_STRUCT_REFERENCE);
        } else {
            mark.error("Expected a type specifier.");
            return false;
        }

        return true;
    }

    private void parseStructSpecifier() {
        // struct_specifier: STRUCT IDENTIFIER LEFT_BRACE struct_declaration_list RIGHT_BRACE
        //                 | STRUCT            LEFT_BRACE struct_declaration_list RIGHT_BRACE
        // note: these are the same except the first is named

        match(STRUCT, "Expected 'struct'.");

        if (getTokenType() == IDENTIFIER) {
            parseIdentifier();
        }

        if (!match(LEFT_BRACE, "'{' expected after 'struct'.")) {
            // It is unlikely that { is missing and } is not.
            // This prevents breakdown on parsing something like: struct Foobar;
            return;
        }

        {
            // struct_declaration_list: struct_declaration (',' struct_declaration)*
            // note: we should initially find ',' for a new declarator or '}' at the end of the struct
            if (getTokenType() == RIGHT_BRACE) {
                error("Empty struct is not allowed.");
            }

            while (!eof() && getTokenType() != RIGHT_BRACE) {
                if(!parseStructDeclaration()) {
                    final Marker invalidTokenSkip = mark();
                    advanceLexer();
                    invalidTokenSkip.error("Expected struct member declaration");
                }
            }
        }

        match(RIGHT_BRACE, "Closing '}' for struct expected.");
    }

    private boolean parseStructDeclaration() {
        // type_specifier struct_declarator_list ';'

        final Marker mark = mark();

        if (!parseQualifiedTypeSpecifier()) {
            mark.rollbackTo();
            return false;
        }
        parseStructDeclaratorList();
        match(SEMICOLON, "Expected ';' after struct declaration.");

        mark.done(STRUCT_MEMBER_DECLARATION);
        return true;
    }

    private void parseStructDeclaratorList() {
        // struct_declarator_list: struct_declarator (',' struct_declarator)*

        final Marker mark = mark();
        do {
            if (eof(mark)) return;
            parseStructOrParameterDeclarator(STRUCT_DECLARATOR);
        } while (tryMatch(COMMA));

        mark.done(STRUCT_DECLARATOR_LIST);
    }

    /**
     * Parses a struct declarator or a parameter declarator depending on the argument.
     *
     * @param type equals either STRUCT_DECLARATOR or PARAMETER_DECLARATOR.
     */
    private void parseStructOrParameterDeclarator(IElementType type) {
        // struct_declarator: IDENTIFIER [ '[' constant_expression ']' ]
        // -OR-
        // parameter_declarator: IDENTIFIER [ '[' constant_expression ']' ]

        assert type == STRUCT_DECLARATOR || type == PARAMETER_DECLARATOR;

        final Marker mark = mark();

        parseIdentifier();

        if (getTokenType() == LEFT_BRACKET) {
            parseArrayDeclarator();
        }

        Marker declaratorEnd = mark();
        if (tryMatch(EQUAL)) {
            parseInitializer();
            declaratorEnd.error("Initializer not allowed here.");
        } else {
            declaratorEnd.drop();
        }
        mark.done(type);
    }

    private void parseQualifierList(boolean validPlacement) {

        final Marker mark = mark();

        if (!validPlacement && !parseQualifier()) {
            mark.drop();
            return;
        }

        //noinspection StatementWithEmptyBody
        while (parseQualifier()) {
        }

        if (validPlacement) {
            mark.done(QUALIFIER_LIST);
        } else {
            mark.error("Qualifier not allowed here.");
        }
    }

    private boolean parseQualifier() {
        // qualifier: layout_qualifier
        //          | subroutine_qualifier
        //          | qualifier_token

        if(parseLayoutQualifier())return true;
        if(parseSubroutineQualifier())return true;

        if (QUALIFIER_TOKENS.contains(getTokenType())) {
            final Marker mark = mark();
            advanceLexer();
            mark.done(QUALIFIER);
            return true;
        }
        return false;
    }

    private boolean parseLayoutQualifierStatement(){
        // layout_qualifier_statement: layout_qualifier interface_qualifier ';'
        // (Made up name.) Can be only in global level.
        // Since it looks like variable declaration up until ';', it will return true and parse only
        // if the semicolon is present.
        // NOTE: interface_qualifier is in, out or uniform

        final Marker mark = mark();

        if(!parseLayoutQualifier()){
            mark.rollbackTo();
            return false;
        }

        if(!tryMatch(INTERFACE_QUALIFIER_TOKENS)){
            mark.rollbackTo();
            return false;
        }

        if(!tryMatch(SEMICOLON)){
            mark.rollbackTo();
            return false;
        }

        mark.done(LAYOUT_QUALIFIER_STATEMENT);
        return true;
    }

    private boolean parseLayoutQualifier(){
        // layout_qualifier: LAYOUT '(' layout_qualifier_id_list ')'

        if(getTokenType() == LAYOUT_KEYWORD){
            final Marker mark = mark();
            advanceLexer();
            match(LEFT_PAREN, "Expected '('");
            parseLayoutQualifierList();
            match(RIGHT_PAREN, "Expected ')'");
            mark.done(QUALIFIER);
            return true;
        }else return false;
    }

    private void parseSubroutineTypeName(){
        final Marker typeNameMark = mark();
        if(getTokenType() == IDENTIFIER){
            advanceLexer();
            typeNameMark.done(TYPE_SPECIFIER);
        }else{
            typeNameMark.error("Subroutine type name expected");
        }

    }

    private boolean parseSubroutineQualifier(){
        // subroutine_qualifier: SUBROUTINE ['(' TYPE_NAME [',' TYPE_NAME]* ')']?
        if(getTokenType() == SUBROUTINE_KEYWORD){
            final Marker mark = mark();
            advanceLexer();
            if(tryMatch(LEFT_PAREN)){
                parseSubroutineTypeName();
                while(getTokenType() == COMMA){
                    advanceLexer();
                    parseSubroutineTypeName();
                }

                match(RIGHT_PAREN, "Expected ')'");
            }
            mark.done(QUALIFIER);
            return true;
        }else return false;
    }

    private void parseLayoutQualifierList() {
        // layout_qualifier_id_list: layout_qualifier_id (COMMA layout_qualifier_id)*
        parseLayoutQualifierElement();
        while (tryMatch(COMMA)) {
            parseLayoutQualifierElement();
        }
    }

    private void parseLayoutQualifierElement() {
        // layout_qualifier_id: IDENTIFIER [ EQUAL constant_expression ]
        //                    | SHARED
        final Marker mark = mark();

        if (tryMatch(IDENTIFIER)) {
            if (tryMatch(EQUAL)) {
                if (!parseConstantExpression()) error("Expected constant expression");
            }
        } else if (!tryMatch(SHARED_KEYWORD)) {
            mark.error("Expected 'shared' or an identifier");
            return;
        }
        mark.done(LAYOUT_QUALIFIER_ID);
    }

    private final static class OperatorLevelTraits {
        private final TokenSet operatorTokens;
        private final String partName;
        private final IElementType elementType;

        private OperatorLevelTraits(TokenSet operatorTokens, String partName, IElementType elementType) {
            this.operatorTokens = operatorTokens;
            this.partName = partName;
            this.elementType = elementType;
        }

        public TokenSet getOperatorTokens() {
            return operatorTokens;
        }

        public String getPartName() {
            return partName;
        }

        public IElementType getElementType() {
            return elementType;
        }
    }
}
