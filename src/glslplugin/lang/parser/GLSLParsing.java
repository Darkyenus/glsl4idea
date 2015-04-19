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

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import glslplugin.lang.elements.GLSLTokenTypes;

import static glslplugin.lang.elements.GLSLElementTypes.*;
import static glslplugin.lang.elements.GLSLTokenTypes.*;

/**
 * GLSLParsing does all the parsing. It has methods which reflects the rules of the grammar.
 * <p/>
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 19, 2009
 *         Time: 3:16:56 PM
 */
public class GLSLParsing {
    // The general approach for error return and flagging is that an error should only be returned when not flagged.
    // So, if an error is encountered; EITHER flag it in the editor OR propagate it down the call stack.


    // Traits of all binary operators.
    // They must be listed in order of precedence.
    private OperatorLevelTraits[] operatorPrecedence = new OperatorLevelTraits[] {
            new OperatorLevelTraits(TokenSet.create(OR_OP), "sub expression", LOGICAL_OR_EXPRESSION),
            new OperatorLevelTraits(TokenSet.create(XOR_OP), "sub expression", LOGICAL_XOR_EXPRESSION),
            new OperatorLevelTraits(TokenSet.create(AND_OP), "sub expression", LOGICAL_AND_EXPRESSION),
            new OperatorLevelTraits(EQUALITY_OPERATORS, "sub expression", EQUALITY_EXPRESSION),
            new OperatorLevelTraits(RELATIONAL_OPERATORS, "sub expression", RELATIONAL_EXPRESSION),
            new OperatorLevelTraits(ADDITIVE_OPERATORS, "part", ADDITIVE_EXPRESSION),
            new OperatorLevelTraits(MULTIPLICATIVE_OPERATORS, "factor", MULTIPLICATIVE_EXPRESSION),
    };


    /* The source of operatorTokens and the target for the AST nodes. */
    private PsiBuilder b;

    GLSLParsing(PsiBuilder builder) {
        b = builder;
    }

    /**
     * Skips forward in the stream until a token in the given set is encountered.
     * When the method returns the matching token will be the current if found,
     * otherwise it will consume all remaining operatorTokens.
     *
     * @param set the set containing the token types to look for.
     */
    private void skipTo(TokenSet set) {
        while (b.getTokenType() != null && set.contains(b.getTokenType())) {
            b.advanceLexer();
        }
    }

    /**
     * Skips forward in the stream until a token in the given token type is encountered.
     * When the method returns the matching token will be the current if found,
     * otherwise it will consume all remaining operatorTokens.
     *
     * @param type token type to look for.
     */
    private void skipTo(IElementType type) {
        while (b.getTokenType() != null && b.getTokenType() != type) {
            b.advanceLexer();
        }
    }

    /**
     * Verifies that the current token is of the given type, if not it will flag an error.
     *
     * @param type  the expected token type.
     * @param error an appropriate error message if any other token is found instead.
     * @return indicates whether the match was successful or not.
     */
    private boolean match(IElementType type, String error) {
        if (b.eof()) {
            return false;
        }
        boolean match = b.getTokenType() == type;
        if (match) {
            b.advanceLexer();
        } else {
            b.error(error);
        }
        return match;
    }

    /**
     * Consumes the next token if it is of the given types, otherwise it is ignored.
     *
     * @param types the expected token types.
     * @return indicates whether the match was successful or not.
     */
    private boolean tryMatch(IElementType... types) {
        if (b.eof()) {
            return false;
        }
        boolean match = false;
        for (IElementType type : types) {
            match |= b.getTokenType() == type;
        }
        if (match) {
            b.advanceLexer();
        }
        return match;
    }

    /**
     * Consumes the next token if it is contained in the given token set, otherwise it is ignored.
     *
     * @param types a token set containing the expected token types.
     * @return indicates whether the match was successful or not.
     */
    private boolean tryMatch(TokenSet types) {
        boolean match = types.contains(b.getTokenType());
        if (match) {
            b.advanceLexer();
        }
        return match;
    }

    /**
     * Entry for parser. Tries to parse whole shader file.
     */
    public void parseTranslationUnit() {
        // translation_unit: external_declaration+

        PsiBuilder.Marker unit = b.mark();
        do {
            if (!parseExternalDeclaration()) {
                b.advanceLexer();
                b.error("Unable to parse external declaration.");
            }
        }
        while (!b.eof());
        unit.done(TRANSLATION_UNIT);
    }

    /**
     * Parse whatever can be at the top of the file hierarchy
     */
    private boolean parseExternalDeclaration() {
        // external_declaration: function_definition
        //                     | declaration
        // A common prefix for both: fully_specified_type
        // Expanding the rule to obtain:
        // external_declaration: qualifier-list type-specifier declarator-list ';'
        //                     | qualifier-list type-specifier prototype ';'
        //                     | qualifier-list type-specifier prototype compound-statement
        // Note: after type specifier, we only need to look up IDENTIfIER '(' to determine
        //       whether or not it is a prototype or a declarator-list.

        PsiBuilder.Marker mark = b.mark();

        // This bunch of conditionals are responsible to handle really invalid input.
        // Specifically, when b.getTokenType() is not in the first set of external-declaration
        // Please add more if found lacking.
        // TODO: Add something similar to parseStatement
        if (b.getTokenType() == LEFT_PAREN ||
                CONSTANT_TOKENS.contains(b.getTokenType()) ||
                UNARY_OPERATORS.contains(b.getTokenType())) {
            parseExpression();
            tryMatch(SEMICOLON);
            mark.error("Expression not allowed here.");
            return true;
        }
        String text = b.getTokenText();
        if (b.getTokenType() == IF_KEYWORD ||
                b.getTokenType() == FOR_KEYWORD ||
                b.getTokenType() == WHILE_KEYWORD ||
                b.getTokenType() == DO_KEYWORD) {
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
            mark = b.mark();
        }

        parseQualifiedTypeSpecifier();

        PsiBuilder.Marker postType = b.mark();

        if (b.getTokenType() == SEMICOLON) {
            // Declaration with no declarators.
            // (struct definitions will look like this)
            postType.drop();
            parseDeclaratorList(); // the list will always be empty.
            match(SEMICOLON, "Missing ';'");
            mark.done(VARIABLE_DECLARATION);
            return true;

        } else if (b.getTokenType() ==  IDENTIFIER || b.getTokenType() == LEFT_PAREN) {
            // Identifier means either declarators, or function declaration/definition
            match(IDENTIFIER, "Missing function name");

            if (b.getTokenType() == SEMICOLON ||
                    b.getTokenType() == COMMA ||
                    b.getTokenType() == LEFT_BRACKET ||
                    b.getTokenType() == EQUAL) {
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

                PsiBuilder.Marker declarator = b.mark();
                parseIdentifier();
                declarator.done(DECLARATOR);

                match(LEFT_PAREN, "Expected '(' after function identifier.");
                parseParameterDeclarationList();
                match(RIGHT_PAREN, "Missing ')' after function prototype");

                // Prototype is now done, so look for ';' or '{'

                if (tryMatch(SEMICOLON)) {
                    mark.done(FUNCTION_DECLARATION);
                } else if (b.getTokenType() == LEFT_BRACE) {
                    parseCompoundStatement();
                    mark.done(FUNCTION_DEFINITION);
                } else {
                    // Neither ';' nor '{' found, mark as a prototype with missing ';'
                    mark.done(FUNCTION_DECLARATION);
                    b.error("Missing ';' after function declaration.");
                }
                return true;
            } else if (TYPE_SPECIFIER_NONARRAY_TOKENS.contains(b.getTokenType())) {
                // simulate declarators, and return success to make parsing continue.
                postType.done(IDENTIFIER);
                postType = postType.precede();
                postType.done(DECLARATOR);
                postType = postType.precede();
                postType.done(DECLARATOR_LIST);
                mark.done(VARIABLE_DECLARATION);
                b.error("Missing ';' after declaration.");
                return true;
            }
        } else if (GLSLTokenTypes.OPERATORS.contains(b.getTokenType()) ||
                b.getTokenType() == DOT ||
                b.getTokenType() == LEFT_BRACKET) {
            // this will handle most expressions
            postType.drop();
            mark.rollbackTo();
            mark = b.mark();
            parseExpression();
            tryMatch(SEMICOLON);
            mark.error("Expression not allowed here.");
            return true;
        } else if (GLSLTokenTypes.FLOW_KEYWORDS.contains(b.getTokenType()) ||
                GLSLTokenTypes.CONSTANT_TOKENS.contains(b.getTokenType())) {
            postType.drop();
            text = b.getTokenText();
            b.advanceLexer();
            mark.error("Unexpected '" + text + "'");
            return true;
        } else if (TYPE_SPECIFIER_NONARRAY_TOKENS.contains(b.getTokenType())) {
            // simulate declarators, and return success to make parsing continue.
            postType.done(DECLARATOR_LIST);
            mark.done(VARIABLE_DECLARATION);
            b.error("Missing ';' after declaration.");
            return true;
        }

        mark.rollbackTo();
        return false;
    }

    private void parseQualifiedTypeSpecifier() {
        parseQualifierList(true);
        parseTypeSpecifier();
        parseQualifierList(false);
    }

    private void parseParameterDeclarationList() {
        // parameter_declaration_list: <nothing>
        //                           | VOID
        //                           | parameter_declaration (',' parameter_declaration)*
        final PsiBuilder.Marker mark = b.mark();

        if (tryMatch(VOID_TYPE)) {
            // Do nothing.
        } else if (b.getTokenType() != RIGHT_PAREN) {
            do {
                parseParameterDeclaration();
            } while (tryMatch(COMMA));
        }
        mark.done(PARAMETER_DECLARATION_LIST);
    }

    private void parseParameterDeclaration() {
        // parameter_declaration: [parameter_qualifier] [type_qualifier] IDENTIFIER [array_declarator]
        final PsiBuilder.Marker mark = b.mark();

        parseQualifiedTypeSpecifier();

        if (b.getTokenType() == IDENTIFIER) {
            parseStructOrParameterDeclarator(PARAMETER_DECLARATOR);
        } else {
            // Fake a declarator.
            PsiBuilder.Marker mark2 = b.mark();
            mark2.done(PARAMETER_DECLARATOR);
        }

        mark.done(PARAMETER_DECLARATION);
    }

    private void parseCompoundStatement() {
        // compound_statement: '{' '}'
        //                   | '{' statement_list '}'
        PsiBuilder.Marker mark = b.mark();
        match(LEFT_BRACE, "'{' expected.");
        if (eof(mark)) return;
        if (b.getTokenType() != RIGHT_BRACE) {
            parseStatementList();
        }
        if (eof()) {
            mark.drop();
        } else {
            match(RIGHT_BRACE, "'}' expected.");
            mark.done(COMPOUND_STATEMENT);
        }
    }

    private boolean eof(PsiBuilder.Marker... marksToClose) {
        if (b.eof()) {
            if (marksToClose.length > 0) {
                for (PsiBuilder.Marker mark : marksToClose) {
                    mark.error("Premature end of file.");
                }
            } else {
                b.error("Premature end of file.");
            }
            return true;
        } else {
            return false;
        }
    }

    private void parseStatementList() {
        // statement_list: statement*
        // NOTE: terminates with '}', but we check for FirstSet(statement)
        //       instead for increased robustness

        while ((STATEMENT_FIRST_SET.contains(b.getTokenType()) || OPERATORS.contains(b.getTokenType())) && !eof()) {
            if (!parseStatement()) {
                return;
            }
        }
    }

    private boolean parseStatement() {
        // statement: simple_statement | compound_statement

        if (b.getTokenType() == LEFT_BRACE) {
            parseCompoundStatement();
            return true;
        }
        if (parseSimpleStatement()) {
            return true;
        } else {
            b.error("Expected a statement.");
            return false;
        }
    }

    private void eatInvalidOperators() {
        PsiBuilder.Marker mark = b.mark();
        while (OPERATORS.contains(b.getTokenType())) {
            String operator = b.getTokenText();
            b.advanceLexer();
            mark.error("Unexpected operator '" + operator + "'.");
            mark = b.mark();
        }
        mark.drop();
    }

    private boolean parseSimpleStatement() {
        // simple_statement: declaration_statement
        //                 | expression_statement
        //                 | selection_statement
        //                 | iteration_statement
        //                 | jump_statement
        eatInvalidOperators();

        final IElementType type = b.getTokenType();
        boolean result;

        if (EXPRESSION_FIRST_SET.contains(type) || QUALIFIER_TOKENS.contains(type)) {
            // This set also includes the first set of declaration_statement
            if (lookaheadDeclarationStatement()) {
                result = parseVariableDeclarationStatement();
            } else {
                result = parseExpressionStatement();
            }
        } else if (type == IF_KEYWORD) {
            result = parseSelectionStatement();
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
        } else {
            return false;
        }
        return result;
    }

    private boolean parseReturnStatement() {
        // return_statement: 'return' [expression] ';'
        PsiBuilder.Marker mark = b.mark();
        match(RETURN_JUMP_STATEMENT, "Missing 'return'.");
        if (b.getTokenType() != SEMICOLON) {
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
        PsiBuilder.Marker mark = b.mark();
        match(CONTINUE_JUMP_STATEMENT, "Missing 'continue'.");
        match(SEMICOLON, "Missing ';' after 'continue'.");
        mark.done(CONTINUE_STATEMENT);
        return true;
    }

    private boolean parseDiscardStatement() {
        // discard_statement: 'discard' ';'
        PsiBuilder.Marker mark = b.mark();
        match(DISCARD_JUMP_STATEMENT, "Missing 'discard'.");
        match(SEMICOLON, "Missing ';' after 'discard'.");
        mark.done(DISCARD_STATEMENT);
        return true;
    }

    private boolean parseBreakStatement() {
        // break_statement: 'break' ';'
        PsiBuilder.Marker mark = b.mark();
        match(BREAK_JUMP_STATEMENT, "Missing 'break'.");
        match(SEMICOLON, "Missing ';' after 'break'.");
        mark.done(BREAK_STATEMENT);
        return true;
    }

    private boolean parseForStatement() {
        // for_iteration_statement: 'for' '(' for_init_statement for_rest_statement ')' statement_no_new_scope
        // NOTE: refactored to:
        // for_iteration_statement: 'for' '(' (expression|declaration) ';' expression
        PsiBuilder.Marker mark = b.mark();

        match(FOR_KEYWORD, "Missing 'for'.");
        match(LEFT_PAREN, "Missing '(' after 'for'.");

        parseForInitStatement();
        match(SEMICOLON, "Missing ';' in for statement.");

        if (b.getTokenType() != SEMICOLON) {
            parseCondition();
        }
        match(SEMICOLON, "Missing ';' in for statement.");

        if (b.getTokenType() != RIGHT_PAREN) {
            // Only parse the expression if it is present.
            parseExpression();
        }

        match(RIGHT_PAREN, "Missing ')' after 'for'.");
        parseStatement();

        mark.done(FOR_STATEMENT);
        return true;
    }

    private void parseCondition() {
        // condition: expression
        //          | fully_specified_type IDENTIFIER '=' initializer
        // NOTE: The spec, allows the condition expression in 'for' and 'while' loops
        //       to declare a single variable.

        PsiBuilder.Marker conditionMark = b.mark();
        if (lookaheadDeclarationStatement()) {
            PsiBuilder.Marker mark = b.mark();

            parseQualifiedTypeSpecifier();

            PsiBuilder.Marker list = b.mark();
            PsiBuilder.Marker declarator = b.mark();

            parseIdentifier();
            match(EQUAL, "Missing '=' in condition initializer.");
            parseInitializer();

            declarator.done(DECLARATOR);
            list.done(DECLARATOR_LIST);

            mark.done(VARIABLE_DECLARATION);

        } else {
            parseExpression();
        }
        conditionMark.done(CONDITION);
    }

    private void parseForInitStatement() {
        // for_init_statement: expression_statement | declaration_statement

        if (b.getTokenType() == IDENTIFIER) {
            // needs lookahead
            PsiBuilder.Marker rollback = b.mark();
            b.advanceLexer();
            if (b.getTokenType() == IDENTIFIER) {
                // IDENTIFIER IDENTIFIER means declaration statement
                // where the first is the type specifier
                rollback.rollbackTo();
                parseVariableDeclaration();
            } else if (OPERATORS.contains(b.getTokenType()) ||
                    b.getTokenType() == DOT ||
                    b.getTokenType() == LEFT_BRACKET ||
                    b.getTokenType() == QUESTION ||
                    b.getTokenType() == LEFT_PAREN) {
                // This should be the complete follow set of IDENTIFIER in the
                // context limited to expressions
                rollback.rollbackTo();
                parseExpression();
            }

        } else if (TYPE_SPECIFIER_NONARRAY_TOKENS.contains(b.getTokenType()) ||
                QUALIFIER_TOKENS.contains(b.getTokenType())) {
            parseVariableDeclaration();
        } else if (UNARY_OPERATORS.contains(b.getTokenType()) ||
                FUNCTION_IDENTIFIER_TOKENS.contains(b.getTokenType()) ||
                b.getTokenType() == LEFT_PAREN) {
            parseExpression();
        } else if (b.getTokenType() == SEMICOLON) {
            // Do nothing here
        } else {
            // Token not in first set, how did we end up here?
            // TODO: Add error handling!
        }
    }

    private boolean parseDoIterationStatement() {
        // do_iteration_statement: 'do' statement 'while' '(' expression ')' ';'
        PsiBuilder.Marker mark = b.mark();

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
        PsiBuilder.Marker mark = b.mark();

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
        PsiBuilder.Marker mark = b.mark();

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
        PsiBuilder.Marker mark = b.mark();
        if (tryMatch(ELSE_KEYWORD)) {
            parseStatement();
            mark.done(ELSE_STATEMENT);
        } else {
            mark.drop();
        }
    }

    private boolean parseExpressionStatement() {
        // expression_statement: [expression] ';'
        PsiBuilder.Marker mark = b.mark();

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

    private boolean parseVariableDeclarationStatement() {
        // declaration_statement: declaration
        PsiBuilder.Marker mark = b.mark();

        if (!parseVariableDeclaration()) {
            mark.error("Expected variable declaration.");
            return false;
        } else {
            match(SEMICOLON, "Expected ';' after variable declaration.");
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
        // they share type_specifier. So if found; look for the following identifier.
        PsiBuilder.Marker rollback = b.mark();

        boolean result = lookaheadForDeclarationStatementImpl();

        rollback.rollbackTo();
        return result;
    }

    private boolean lookaheadForDeclarationStatementImpl() {
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
    }

    private boolean parseVariableDeclaration() {
        // declaration: function_prototype SEMICOLON
        //            | init_declarator_list SEMICOLON

        PsiBuilder.Marker mark = b.mark();

        parseQualifiedTypeSpecifier();
        parseDeclaratorList();
        mark.done(VARIABLE_DECLARATION);

        return true;
    }

    private void parseDeclaratorList() {
        // init_declarator_list: fully_specified_type
        //                     | fully_specified_type declarator ( ',' declarator )*
        PsiBuilder.Marker mark = b.mark();
        if (b.getTokenType() == IDENTIFIER) {
            do {
                parseDeclarator();
            } while (tryMatch(COMMA));
        }
        mark.done(DECLARATOR_LIST);
    }

    private void parseDeclarator() {
        // declarator: IDENTIFIER [ '[' [ constant_expression ] ']' ] [ '=' initializer ]
        final PsiBuilder.Marker mark = b.mark();
        parseIdentifier();
        if (b.getTokenType() == LEFT_BRACKET) {
            parseArrayDeclarator();
        }
        if (tryMatch(EQUAL)) {
            final PsiBuilder.Marker mark2 = b.mark();

            parseInitializer();

            mark2.done(INITIALIZER);
        }
        mark.done(DECLARATOR);
    }

    private void parseArrayDeclarator() {
        final PsiBuilder.Marker mark = b.mark();

        match(LEFT_BRACKET, "Expected '['.");
        if (b.getTokenType() != RIGHT_BRACKET) {
            parseConstantExpression();
        }
        match(RIGHT_BRACKET, "Missing closing ']' after array declarator.");

        mark.done(ARRAY_DECLARATOR);

        if (b.getTokenType() == LEFT_BRACKET) {
            PsiBuilder.Marker err = b.mark();
            while (tryMatch(LEFT_BRACKET)) {
                parseConstantExpression();
                match(RIGHT_BRACKET, "Missing closing ']' after array declarator.");
            }
            err.error("Multiple dimensional arrays not allowed.");
        }
    }

    private void parseInitializer() {
        // initializer: assignment_expression
        parseAssignmentExpression();
    }

    private boolean parseAssignmentExpression() {
        // assignment_expression: conditional_expression
        //                      | unary_expression assignment_operator assignment_expression
        // NOTE: both conditional_expression and assignment_expression starts with unary_expression
        // CHANGED TO: (to reduce the need for lookahead. use the annotation passs to verify l-values)
        // assignment_expression: conditional_expression (assignment_operator conditional_expression)*
        PsiBuilder.Marker mark = b.mark();

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
        PsiBuilder.Marker mark = b.mark();
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

        PsiBuilder.Marker mark = b.mark();

        if (!parseAssignmentExpression()) {
            mark.error("Expected an expression.");
            return false;
        }
        while (tryMatch(COMMA)) {
            if (!parseAssignmentExpression()) {
                mark.error("Expected an expression.");
                return false;
            }
            mark.done(EXPRESSION);
            mark = mark.precede();
        }

        mark.drop();
        return true;
    }

    private class OperatorLevelTraits {
        private TokenSet operatorTokens;
        private String partName;
        private IElementType elementType;

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

    private boolean parseOperatorExpression() {
        return parseOperatorExpressionLevel(0);
    }

    private boolean parseOperatorExpression(int level) {
        PsiBuilder.Marker mark = b.mark();
        if (!parseOperatorExpressionLevel(level+1)) {
            mark.drop();
            return false;
        }

        OperatorLevelTraits operatorLevel = operatorPrecedence[level];
        while (tryMatch(operatorLevel.getOperatorTokens())) {
            if (parseOperatorExpressionLevel(level+1)) {
                mark.done(operatorLevel.getElementType());
                mark = mark.precede();
            } else {
                PsiBuilder.Marker operatorMark = b.mark();
                if(tryMatch(OPERATORS)) {
                    do {
                        operatorMark.error("Operator out of place.");
                        if(parseOperatorExpressionLevel(level+1)) {
                            mark.done(operatorLevel.getElementType());
                            mark = mark.precede();
                            break;
                        } else {
                            operatorMark = b.mark();
                        }
                    } while(tryMatch(OPERATORS));
                } else {
                    operatorMark.drop();
                    mark.error(String.format("Expected a(n) %s expression.", operatorLevel.getPartName()));
                    return false;
                }
            }
        }
        mark.drop();
        return true;
    }

    private boolean parseOperatorExpressionLevel(int level) {
        if(level == operatorPrecedence.length) {
            return parseUnaryExpression();
        } else {
            return parseOperatorExpression(level);
        }
    }

    private boolean parseUnaryExpression() {
        // unary_expression: postfix_expression
        //                 | unary_operator unary_expression
        // note: moved INC_OP and DEC_OP to unary_operator
        PsiBuilder.Marker mark = b.mark();

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
        PsiBuilder.Marker mark = b.mark();
        boolean result;
        if (lookupFunctionCall()) {
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
                if (lookaheadMethodCall()) {
                    parseFunctionCallImpl(true);
                    mark.done(METHOD_CALL_EXPRESSION);
                } else {
                    parseFieldIdentifier();
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
     * Figures out whether the next sequence of operatorTokens are a method call or a field selection.
     * The preceding '.' token is consumed before this is called.
     *
     * @return true if the sequence contains a method call, false otherwise.
     */
    private boolean lookaheadMethodCall() {
        PsiBuilder.Marker mark = b.mark();
        boolean result = false;

        if (tryMatch(TYPE_SPECIFIER_NONARRAY_TOKENS)) {
            result = true;
        } else if (tryMatch(IDENTIFIER)) {
            if (tryMatch(LEFT_PAREN)) {
                result = true;
            }
        }

        mark.rollbackTo();
        return result;
    }

    /**
     * Looks ahead to determine whether the start of a postfix_expression is
     * a function call or a primary expression.
     *
     * @return true if it is a function or method call, false otherwise.
     */
    private boolean lookupFunctionCall() {
        // For some reason the grammar includes method calls?
        // They should probably be reserved, but they're included since
        // they're in the spec.
        // It turns out that arrays support the length() method.
        PsiBuilder.Marker rollback = b.mark();
        boolean result = false;

        if (tryMatch(TYPE_SPECIFIER_NONARRAY_TOKENS)) {
            result = true;
        } else if (tryMatch(IDENTIFIER)) {
            if (tryMatch(LEFT_PAREN)) {
                result = true;
            }
        }
        rollback.rollbackTo();
        return result;
    }

    private boolean parseFunctionCall() {
        PsiBuilder.Marker mark = b.mark();

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

    private String parseFunctionIdentifier(boolean markAsMethodIdentifier) {
        // function_identifier: IDENTIFIER
        //                    | type_name [ array_declarator ] 
        PsiBuilder.Marker mark = b.mark();
        String name = b.getTokenText();
        if (tryMatch(FUNCTION_IDENTIFIER_TOKENS)) {
            if (b.getTokenType() == LEFT_BRACKET) {
                parseArrayDeclarator();
            }
            mark.done(markAsMethodIdentifier ? METHOD_NAME : FUNCTION_NAME);
            return name;
        } else {
            mark.error("Expected function identifier.");
            return null;
        }
    }

    private void parseParameterList() {
        // parameter_list: VOID | (nothing)
        //               | assignment_expression (',' assignment_expression)
        PsiBuilder.Marker mark = b.mark();

        if (b.getTokenType() == VOID_TYPE) {
            b.advanceLexer();
        } else if (b.getTokenType() == RIGHT_PAREN) {
            // do nothing
        } else if (parseAssignmentExpression()) {
            while (tryMatch(COMMA)) {
                if (!parseAssignmentExpression()) {
                    b.error("Assignment expression expected.");
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
        //                   | INTCONSTANT
        //                   | FLOATCONSTANT
        //                   | BOOLCONSTANT
        //                   | '(' expression ')'
        final PsiBuilder.Marker mark = b.mark();
        final IElementType type = b.getTokenType();
        if (type == IDENTIFIER) {
            final PsiBuilder.Marker mark2 = b.mark();
            b.advanceLexer();
            mark2.done(VARIABLE_NAME);
            mark.done(VARIABLE_NAME_EXPRESSION);
            return true;
        } else if (type == INTEGER_CONSTANT || type == FLOAT_CONSTANT || type == BOOL_CONSTANT) {
            b.advanceLexer();
            mark.done(CONSTANT_EXPRESSION);
            return true;
        } else if (type == LEFT_PAREN) {
            b.advanceLexer();
            if (!parseExpression()) {
                if (b.getTokenType() == RIGHT_PAREN) {
                    b.error("Expected expression after '('");
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

    private String parseIdentifier() {
        final PsiBuilder.Marker mark = b.mark();
        boolean success = b.getTokenType() == IDENTIFIER;
        if (success) {
            String name = b.getTokenText();
            b.advanceLexer();
            mark.done(VARIABLE_NAME);
            return name;
        } else {
            mark.error("Expected an identifier.");
            return null;
        }
    }

    private String parseFieldIdentifier() {
        final PsiBuilder.Marker mark = b.mark();
        boolean success = b.getTokenType() == IDENTIFIER;
        if (success) {
            String name = b.getTokenText();
            b.advanceLexer();
            mark.done(FIELD_NAME);
            return name;
        } else {
            mark.error("Expected an identifier.");
            return null;
        }
    }

    private boolean parseTypeSpecifier() {
        // type_specifier_noarray
        // type_specifier_noarray "[" const_expr "]"

        final PsiBuilder.Marker mark = b.mark();

        if (!parseTypeSpecifierNoArray()) {
            mark.drop();
            return false;
        }

        if (b.getTokenType() == LEFT_BRACKET) {
            parseArrayDeclarator();
        }
        mark.done(TYPE_SPECIFIER);
        return true;
    }

    private boolean parseConstantExpression() {
        // constant_expression: conditional_expression
        return parseConditionalExpression();
    }

    private boolean parseTypeSpecifierNoArray() {
        // type_specifier_noarray: all_built_in_types
        //                       | struct_specifier
        //                       | type_name
        // todo: implement       | INVARIANT IDENTIFIER  (vertex only)
        // note: This also accepts IDENTIFIERS

        final PsiBuilder.Marker mark = b.mark();

        if (b.getTokenType() == STRUCT) {
            parseStructSpecifier();
            mark.done(TYPE_SPECIFIER_STRUCT);
        } else if (TYPE_SPECIFIER_NONARRAY_TOKENS.contains(b.getTokenType())) {
            b.advanceLexer();
            mark.done(TYPE_SPECIFIER_PRIMITIVE);
        } else if (b.getTokenType() == IDENTIFIER) {
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
        //                 | STRUCT LEFT_BRACE struct_delcaration_list RIGHT_BRACE
        // note: these are the same except the first is named

        match(STRUCT, "Expected 'struct'.");

        if (b.getTokenType() == IDENTIFIER) {
            parseIdentifier();
        }

        match(LEFT_BRACE, "'{' expected after 'struct'.");

        parseStructDeclarationList();

        match(RIGHT_BRACE, "Closing '}' for struct expected.");
    }

    private void parseStructDeclarationList() {
        // struct_declaration_list: struct_declaration (',' struct_declaration)*
        // note: we should initially find ',' for a new declarator or '}' at the end of the struct

        final PsiBuilder.Marker mark = b.mark();

        if (b.getTokenType() == RIGHT_BRACE) {
            b.error("Empty struct is not allowed.");
        }

        while (GLSLTokenTypes.TYPE_SPECIFIER_NONARRAY_TOKENS.contains(b.getTokenType()) ||
                b.getTokenType() == GLSLTokenTypes.IDENTIFIER) {
            parseStructDeclaration();
        }

        mark.done(STRUCT_DECLARATION_LIST);
    }

    private void parseStructDeclaration() {
        // type_specifier struct_declarator_list ';'

        final PsiBuilder.Marker mark = b.mark();

        parseQualifiedTypeSpecifier();
        parseStructDeclaratorList();
        match(SEMICOLON, "Expected ';' after declaration.");

        mark.done(STRUCT_DECLARATION);
    }

    private void parseStructDeclaratorList() {
        // struct_declarator_list: struct_declarator (',' struct_declarator)*

        final PsiBuilder.Marker mark = b.mark();
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

        final PsiBuilder.Marker mark = b.mark();

        parseIdentifier();

        if (b.getTokenType() == LEFT_BRACKET) {
            b.advanceLexer();
            parseConstantExpression();
            match(RIGHT_BRACKET, "Expected ']' after constant expression.");
        }

        PsiBuilder.Marker declaratorEnd = b.mark();
        if (tryMatch(EQUAL)) {
            parseInitializer();
            declaratorEnd.error("Initializer not allowed herer.");
        } else {
            declaratorEnd.drop();
        }
        mark.done(type);
    }

    private void parseQualifierList(boolean validPlacement) {

        final PsiBuilder.Marker mark = b.mark();

        while (QUALIFIER_TOKENS.contains(b.getTokenType())) {
            final PsiBuilder.Marker mark2 = b.mark();

            b.advanceLexer();

            if(validPlacement)
                mark2.done(QUALIFIER);
            else
                mark2.error("Qualifier not allowed here.");
        }
        if (validPlacement)
            mark.done(QUALIFIER_LIST);
        else
            mark.drop();
    }

}
