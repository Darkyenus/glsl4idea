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
import glslplugin.lang.elements.expressions.GLSLLiteral;

import java.util.ArrayList;
import java.util.logging.Logger;

import static glslplugin.lang.elements.GLSLElementTypes.*;
import static glslplugin.lang.elements.GLSLTokenTypes.*;

/**
 * GLSLParsing does all the parsing. It has methods which reflects the rules of the grammar.
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 19, 2009
 *         Time: 3:16:56 PM
 */
public final class GLSLParsing extends GLSLParsingBase {
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
     * Parses preprocessor, assuming that the tokenType() is at PREPROCESSOR_BEGIN.
     *
     * Called automatically on advanceLexer(), which means that elements may contain
     * some tokens, that are part of preprocessor, not that element.
     * This may cause trouble during working with the PSI tree, so be careful.
     */
    @Override
    protected final void parsePreprocessor() {
        if(preprocessorTokens != null) Logger.getLogger("GLSLParsing").warning("Parsing preprocessor inside preprocessor");

        // We can't use tryMatch etc. in here because we'll end up
        // potentially parsing a preprocessor directive inside this one.
        PsiBuilder.Marker preprocessor = mark();
        psiBuilder.advanceLexer(); //Get past the PREPROCESSOR_BEGIN ("#")

        if(psiBuilder.getTokenType() == PREPROCESSOR_DEFINE){
            //Parse define
            psiBuilder.advanceLexer();//Get past DEFINE

            if(psiBuilder.getTokenType() == IDENTIFIER){
                //Valid
                final String defineIdentifier = psiBuilder.getTokenText();
                //Can use non-psiBuilder advanceLexer here, to allow "nested" defines
                advanceLexer();//Get past identifier

                if(tokenType() == PREPROCESSOR_END){
                    defines.put(defineIdentifier, PreprocessorDropIn.EMPTY);
                } else {
                    PreprocessorDropInType meaning = PreprocessorDropInType.UNKNOWN;

                    PsiBuilder.Marker defineMeaningMark = mark();

                    if(CONSTANT_TOKENS.contains(tokenType()) && lookAhead() == PREPROCESSOR_END){
                        meaning = PreprocessorDropInType.LITERAL;
                    }

                    if(meaning == PreprocessorDropInType.UNKNOWN && parseConditionalExpression()){
                        if(tokenType() == PREPROCESSOR_END){
                            //It is only a constant expression
                            meaning = PreprocessorDropInType.CONDITIONAL_EXPRESSION;
                        }
                    }
                    defineMeaningMark.rollbackTo();
                    defineMeaningMark = mark();

                    if(meaning == PreprocessorDropInType.UNKNOWN && parseExpression()){
                        if(tokenType() == PREPROCESSOR_END){
                            //It is any expression
                            meaning = PreprocessorDropInType.EXPRESSION;
                        }
                    }
                    defineMeaningMark.rollbackTo();

                    final StringBuilder replacementText = new StringBuilder();

                    final ArrayList<PreprocessorToken> definedTokens = new ArrayList<PreprocessorToken>();
                    while (!isEof()) {
                        PreprocessorToken token = new PreprocessorToken(tokenType(), getTokenText());
                        definedTokens.add(token);
                        replacementText.append(getTokenText()).append(' ');
                        advanceLexer();
                        if (tokenType() == PREPROCESSOR_END) {
                            break;
                        }
                    }

                    final String replacementTextString = replacementText.length() == 0 ? "" : replacementText.substring(0, replacementText.length()-1);
                    defines.put(defineIdentifier, new PreprocessorDropIn(meaning, definedTokens, replacementTextString));
                }

                //TODO Handle function-like defines
            }else{
                //Invalid
                psiBuilder.error("Identifier expected.");
                //Eat rest
                while (!isEof()) {
                    if (psiBuilder.getTokenType() == PREPROCESSOR_END) {
                        break;
                    }
                    psiBuilder.advanceLexer();
                }
            }
        }else if(psiBuilder.getTokenType() == PREPROCESSOR_UNDEF){
            //Parse undefine
            psiBuilder.advanceLexer();//Get past UNDEF

            if(psiBuilder.getTokenType() == IDENTIFIER){
                //Valid
                final String defineIdentifier = psiBuilder.getTokenText();
                defines.remove(defineIdentifier);

                psiBuilder.advanceLexer();//Get past IDENTIFIER
            }else{
                //Invalid
                psiBuilder.error("Identifier expected.");
            }
            //Eat rest
            while (!isEof()) {
                if (psiBuilder.getTokenType() == PREPROCESSOR_END) {
                    break;
                }else{
                    psiBuilder.error("Unexpected token.");
                }
                psiBuilder.advanceLexer();
            }
        }else{
            //Some other directive, no work here
            while (!isEof()) {
                if (psiBuilder.getTokenType() == PREPROCESSOR_END) {
                    break;
                }
                psiBuilder.advanceLexer();
            }
        }
        advanceLexer();//Get past PREPROCESSOR_END
        preprocessor.done(PREPROCESSOR_DIRECTIVE);

        if (tokenType() == PREPROCESSOR_BEGIN) {
            parsePreprocessor();
        }
    }

    /**
     * Entry for parser. Tries to parse whole shader file.
     */
    public void parseTranslationUnit() {
        // translation_unit: external_declaration*

        // We normally parse preprocessor directives whenever we advance the lexer - which means that if the first
        // token is a preprocessor directive we won't catch it, so we just parse them all at the beginning here.
        while (tokenType() == PREPROCESSOR_BEGIN) {
            parsePreprocessor();
        }

        while (!isEof()) {
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

        PsiBuilder.Marker mark = mark();

        // This bunch of conditionals are responsible to handle really invalid input.
        // Specifically, when b.getTokenType() is not in the first set of external-declaration
        // Please add more if found lacking.
        // TODO: Add something similar to parseStatement
        if (tokenType() == LEFT_PAREN ||
                CONSTANT_TOKENS.contains(tokenType()) ||
                UNARY_OPERATORS.contains(tokenType())) {
            parseExpression();
            tryMatch(SEMICOLON);
            mark.error("Expression not allowed here.");
            return true;
        }
        String text = getTokenText();
        if (tokenType() == IF_KEYWORD ||
                tokenType() == FOR_KEYWORD ||
                tokenType() == WHILE_KEYWORD ||
                tokenType() == DO_KEYWORD) {
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

        if (tokenType() == IDENTIFIER && lookAhead() == LEFT_BRACE) { // interface block
            //TODO Make sure that this is preceded by storage_qualifier
            parseIdentifier();
            match(LEFT_BRACE, "Expected '{'");

            if (tokenType() == RIGHT_BRACE) {
                error("Empty interface block is not allowed.");
            }

            while (!tryMatch(RIGHT_BRACE) && !eof()) {
                final PsiBuilder.Marker member = mark();
                parseQualifierList(true);
                if (!parseTypeSpecifierNoArray()) advanceLexer();
                parseDeclaratorList();
                match(SEMICOLON, "Expected ';'");
                member.done(STRUCT_DECLARATION);//TODO Should we call interface block members struct members?
            }

            if (tokenType() == IDENTIFIER) {
                parseIdentifier();
                if (tokenType() == LEFT_BRACKET) {
                    parseArrayDeclarator();
                }
            }
            match(SEMICOLON, "Expected ';'");
            mark.done(INTERFACE_BLOCK);
            return true;
        }

        parseTypeSpecifier();
        PsiBuilder.Marker postType = mark();

        if (tokenType() == SEMICOLON) {
            // Declaration with no declarators.
            // (struct definitions will look like this)
            postType.drop();
            parseDeclaratorList(); // the list will always be empty.
            match(SEMICOLON, "Missing ';'");
            mark.done(VARIABLE_DECLARATION);
            return true;

        } else if (tokenType() == IDENTIFIER || tokenType() == LEFT_PAREN) {
            // Identifier means either declarators, or function declaration/definition
            match(IDENTIFIER, "Missing function name");

            if (tokenType() == SEMICOLON ||
                    tokenType() == COMMA ||
                    tokenType() == LEFT_BRACKET ||
                    tokenType() == EQUAL) {
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

                PsiBuilder.Marker declarator = mark();
                parseIdentifier();
                declarator.done(DECLARATOR);

                match(LEFT_PAREN, "Expected '(' after function identifier.");
                parseParameterDeclarationList();
                match(RIGHT_PAREN, "Missing ')' after function prototype");

                // Prototype is now done, so look for ';' or '{'

                if (tryMatch(SEMICOLON)) {
                    mark.done(FUNCTION_DECLARATION);
                } else if (tokenType() == LEFT_BRACE) {
                    parseCompoundStatement();
                    mark.done(FUNCTION_DEFINITION);
                } else {
                    // Neither ';' nor '{' found, mark as a prototype with missing ';'
                    mark.done(FUNCTION_DECLARATION);
                    error("Missing ';' after function declaration.");
                }
                return true;
            } else if (TYPE_SPECIFIER_NONARRAY_TOKENS.contains(tokenType())) {
                // simulate declarators, and return success to make parsing continue.
                postType.done(IDENTIFIER);
                postType = postType.precede();
                postType.done(DECLARATOR);
                postType = postType.precede();
                postType.done(DECLARATOR_LIST);
                mark.done(VARIABLE_DECLARATION);
                error("Missing ';' after declaration.");
                return true;
            }
        } else if (GLSLTokenTypes.OPERATORS.contains(tokenType()) ||
                tokenType() == DOT ||
                tokenType() == LEFT_BRACKET) {
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
        } else if (GLSLTokenTypes.FLOW_KEYWORDS.contains(tokenType()) ||
                GLSLTokenTypes.CONSTANT_TOKENS.contains(tokenType())) {
            postType.drop();
            text = getTokenText();
            advanceLexer();
            mark.error("Unexpected '" + text + "'");
            return true;
        } else if (TYPE_SPECIFIER_NONARRAY_TOKENS.contains(tokenType())) {
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
        if (tokenType() == PRECISION_KEYWORD) {
            final PsiBuilder.Marker mark = mark();
            advanceLexer();
            match(PRECISION_QUALIFIER, "Expected precision qualifier.");
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

    private void parseParameterDeclarationList() {
        // parameter_declaration_list: <nothing>
        //                           | VOID
        //                           | parameter_declaration (',' parameter_declaration)*
        final PsiBuilder.Marker mark = mark();

        //noinspection StatementWithEmptyBody
        if (tryMatch(VOID_TYPE)) {
            // Do nothing.
        } else if (tokenType() != RIGHT_PAREN) {
            do {
                parseParameterDeclaration();
            } while (tryMatch(COMMA));
        }
        mark.done(PARAMETER_DECLARATION_LIST);
    }

    private void parseParameterDeclaration() {
        // parameter_declaration: [parameter_qualifier] [type_qualifier] IDENTIFIER [array_declarator]
        final PsiBuilder.Marker mark = mark();

        parseQualifiedTypeSpecifier();

        if (tokenType() == IDENTIFIER) {
            parseStructOrParameterDeclarator(PARAMETER_DECLARATOR);
        } else {
            // Fake a declarator.
            PsiBuilder.Marker mark2 = mark();
            mark2.done(PARAMETER_DECLARATOR);
        }

        mark.done(PARAMETER_DECLARATION);
    }

    private void parseCompoundStatement() {
        // compound_statement: '{' '}'
        //                   | '{' statement_list '}'
        PsiBuilder.Marker mark = mark();
        match(LEFT_BRACE, "'{' expected.");
        if (eof(mark)) return;
        if (tokenType() != RIGHT_BRACE) {
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

        while ((STATEMENT_FIRST_SET.contains(tokenType()) || OPERATORS.contains(tokenType()) || tokenType() == PRECISION_KEYWORD) && !eof()) {
            if (!parseStatement()) {
                return;
            }
        }
    }

    private boolean parseStatement() {
        // statement: simple_statement | compound_statement

        if (tokenType() == LEFT_BRACE) {
            parseCompoundStatement();
            return true;
        }
        if (parseSimpleStatement()) {
            return true;
        } else {
            final PsiBuilder.Marker mark = mark();
            if (parsePrecisionStatement()) {
                mark.error("Precision statement can be only in the top level.");
                return true;
            } else {
                mark.drop();
                error("Expected a statement.");
                return false;
            }
        }
    }

    private void eatInvalidOperators() {
        PsiBuilder.Marker mark = mark();
        while (OPERATORS.contains(tokenType())) {
            String operator = getTokenText();
            advanceLexer();
            mark.error("Unexpected operator '" + operator + "'.");
            mark = mark();
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

        final IElementType type = tokenType();
        boolean result;

        if (EXPRESSION_FIRST_SET.contains(type) || QUALIFIER_TOKENS.contains(type)) {
            // This set also includes the first set of declaration_statement
            if (lookaheadDeclarationStatement()) {
                result = parseDeclarationStatement();
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
        PsiBuilder.Marker mark = mark();
        match(RETURN_JUMP_STATEMENT, "Missing 'return'.");
        if (tokenType() != SEMICOLON) {
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
        PsiBuilder.Marker mark = mark();
        match(CONTINUE_JUMP_STATEMENT, "Missing 'continue'.");
        match(SEMICOLON, "Missing ';' after 'continue'.");
        mark.done(CONTINUE_STATEMENT);
        return true;
    }

    private boolean parseDiscardStatement() {
        // discard_statement: 'discard' ';'
        PsiBuilder.Marker mark = mark();
        match(DISCARD_JUMP_STATEMENT, "Missing 'discard'.");
        match(SEMICOLON, "Missing ';' after 'discard'.");
        mark.done(DISCARD_STATEMENT);
        return true;
    }

    private boolean parseBreakStatement() {
        // break_statement: 'break' ';'
        PsiBuilder.Marker mark = mark();
        match(BREAK_JUMP_STATEMENT, "Missing 'break'.");
        match(SEMICOLON, "Missing ';' after 'break'.");
        mark.done(BREAK_STATEMENT);
        return true;
    }

    private boolean parseForStatement() {
        // for_iteration_statement: 'for' '(' for_init_statement for_rest_statement ')' statement_no_new_scope
        // NOTE: refactored to:
        // for_iteration_statement: 'for' '(' (expression|declaration) ';' expression
        PsiBuilder.Marker mark = mark();

        match(FOR_KEYWORD, "Missing 'for'.");
        match(LEFT_PAREN, "Missing '(' after 'for'.");

        parseForInitStatement();
        match(SEMICOLON, "Missing ';' in for statement.");

        if (tokenType() != SEMICOLON) {
            parseCondition();
        }
        match(SEMICOLON, "Missing ';' in for statement.");

        if (tokenType() != RIGHT_PAREN) {
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

        PsiBuilder.Marker conditionMark = mark();
        if (lookaheadDeclarationStatement()) {
            PsiBuilder.Marker mark = mark();

            parseQualifiedTypeSpecifier();

            PsiBuilder.Marker list = mark();
            PsiBuilder.Marker declarator = mark();

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

        if (tokenType() == IDENTIFIER) {
            // needs lookahead
            PsiBuilder.Marker rollback = mark();
            advanceLexer();
            if (tokenType() == IDENTIFIER) {
                // IDENTIFIER IDENTIFIER means declaration statement
                // where the first is the type specifier
                rollback.rollbackTo();
                parseDeclaration();
            } else if (OPERATORS.contains(tokenType()) ||
                    tokenType() == DOT ||
                    tokenType() == LEFT_BRACKET ||
                    tokenType() == QUESTION ||
                    tokenType() == LEFT_PAREN) {
                // This should be the complete follow set of IDENTIFIER in the
                // context limited to expressions
                rollback.rollbackTo();
                parseExpression();
            }

        } else if (TYPE_SPECIFIER_NONARRAY_TOKENS.contains(tokenType()) ||
                QUALIFIER_TOKENS.contains(tokenType())) {
            parseDeclaration();
        } else if (UNARY_OPERATORS.contains(tokenType()) ||
                FUNCTION_IDENTIFIER_TOKENS.contains(tokenType()) ||
                tokenType() == LEFT_PAREN) {
            parseExpression();
        } else //noinspection StatementWithEmptyBody
            if (tokenType() == SEMICOLON) {
                // Do nothing here
            } else {
                // Token not in first set, how did we end up here?
                // TODO: Add error handling!
            }
    }

    private boolean parseDoIterationStatement() {
        // do_iteration_statement: 'do' statement 'while' '(' expression ')' ';'
        PsiBuilder.Marker mark = mark();

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
        PsiBuilder.Marker mark = mark();

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
        PsiBuilder.Marker mark = mark();

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

    private boolean parseExpressionStatement() {
        // expression_statement: [expression] ';'
        PsiBuilder.Marker mark = mark();

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
        PsiBuilder.Marker mark = mark();

        if (!parseDeclaration()) {
            mark.error("Expected declaration.");
            return false;
        } else {
            match(SEMICOLON, "Expected ';' after declaration.");
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
        PsiBuilder.Marker rollback = mark();
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

        PsiBuilder.Marker mark = mark();

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
        PsiBuilder.Marker mark = mark();
        if (tokenType() == IDENTIFIER) {
            do {
                parseDeclarator();
            } while (tryMatch(COMMA));
        }
        mark.done(DECLARATOR_LIST);
    }

    private void parseDeclarator() {
        // declarator: IDENTIFIER [ '[' [ constant_expression ] ']' ] [ '=' initializer ]
        final PsiBuilder.Marker mark = mark();
        parseIdentifier();
        if (tokenType() == LEFT_BRACKET) {
            parseArrayDeclarator();
        }
        if (tryMatch(EQUAL)) {
            parseInitializer();
        }
        mark.done(DECLARATOR);
    }

    private void parseArrayDeclarator() {
        do{
            final PsiBuilder.Marker mark = mark();

            match(LEFT_BRACKET, "Expected '['.");
            if (tokenType() != RIGHT_BRACKET) {
                parseConstantExpression();
            }
            match(RIGHT_BRACKET, "Missing closing ']' after array declarator.");

            mark.done(ARRAY_DECLARATOR);
        }while(tokenType() == LEFT_BRACKET); //Parse all ARRAY_DECLARATOR's if multidimensional array
    }

    private boolean parseInitializer() {
        // initializer: assignment_expression
        if (tokenType() == LEFT_BRACE) {
            parseInitializerList();
        } else {
            PsiBuilder.Marker mark = mark();
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
        PsiBuilder.Marker mark = mark();

        match(LEFT_BRACE, "Expected '{'");

        if (tokenType() != RIGHT_BRACE) parseInitializer();

        while (tokenType() != RIGHT_BRACE && !eof()) {
            match(COMMA, "Expected '}' or ','");
            if (tokenType() == RIGHT_BRACE) break;
            if (!parseInitializer()) { advanceLexer(); }
        }

        match(RIGHT_BRACE, "Expected '}'");

        mark.done(INITIALIZER_LIST);
    }

    private boolean parseAssignmentExpression() {
        // assignment_expression: conditional_expression
        //                      | unary_expression assignment_operator assignment_expression
        // NOTE: both conditional_expression and assignment_expression starts with unary_expression
        // CHANGED TO: (to reduce the need for lookahead. use the annotation passs to verify l-values)
        // assignment_expression: conditional_expression (assignment_operator conditional_expression)*
        PsiBuilder.Marker mark = mark();

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
        PsiBuilder.Marker mark = mark();

        if(isTokenPreprocessorAlias(PreprocessorDropInType.CONDITIONAL_EXPRESSION)){
            mark.done(new PreprocessedExpressionElementType(preprocessorTextOfConsumedTokens));
            return true;
        }

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

        PsiBuilder.Marker mark = mark();

        if(isTokenPreprocessorAlias(PreprocessorDropInType.EXPRESSION)){
            mark.done(new PreprocessedExpressionElementType(preprocessorTextOfConsumedTokens));
            return true;
        }

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

    private boolean parseOperatorExpression() {
        return parseOperatorExpressionLevel(0);
    }

    private boolean parseOperatorExpression(int level) {
        PsiBuilder.Marker mark = mark();
        if (!parseOperatorExpressionLevel(level + 1)) {
            mark.drop();
            return false;
        }

        OperatorLevelTraits operatorLevel = operatorPrecedence[level];
        while (tryMatch(operatorLevel.getOperatorTokens())) {
            if (parseOperatorExpressionLevel(level + 1)) {
                mark.done(operatorLevel.getElementType());
                mark = mark.precede();
            } else {
                PsiBuilder.Marker operatorMark = mark();
                if (tryMatch(OPERATORS)) {
                    do {
                        operatorMark.error("Operator out of place.");
                        if (parseOperatorExpressionLevel(level + 1)) {
                            mark.done(operatorLevel.getElementType());
                            mark = mark.precede();
                            break;
                        } else {
                            operatorMark = mark();
                        }
                    } while (tryMatch(OPERATORS));
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
        PsiBuilder.Marker mark = mark();

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
        PsiBuilder.Marker mark = mark();
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
        PsiBuilder.Marker mark = mark();
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
        PsiBuilder.Marker rollback = mark();
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
        PsiBuilder.Marker mark = mark();

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
        PsiBuilder.Marker mark = mark();
        String name = getTokenText();
        if (tryMatch(FUNCTION_IDENTIFIER_TOKENS)) {
            if (tokenType() == LEFT_BRACKET) {
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
        PsiBuilder.Marker mark = mark();

        if (tokenType() == VOID_TYPE) {
            advanceLexer();
        } else //noinspection StatementWithEmptyBody
            if (tokenType() == RIGHT_PAREN) {
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
        final PsiBuilder.Marker mark = mark();

        if(getTokenPreprocessorAlias() == PreprocessorDropInType.LITERAL){
            mark.done(new PreprocessedLiteralElementType(GLSLLiteral.getLiteralType(tokenType()), getTokenText()));
            consumePreprocessorTokens();
            return true;
        }

        final IElementType type = tokenType();
        if (type == IDENTIFIER) {
            final PsiBuilder.Marker mark2 = mark();
            advanceLexer();
            mark2.done(VARIABLE_NAME);
            mark.done(VARIABLE_NAME_EXPRESSION);
            return true;
        } else if (tryMatch(CONSTANT_TOKENS)) {
            mark.done(CONSTANT_EXPRESSION);
            return true;
        } else if (type == LEFT_PAREN) {
            advanceLexer();
            if (!parseExpression()) {
                if (tokenType() == RIGHT_PAREN) {
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

    private String parseIdentifier() {
        final PsiBuilder.Marker mark = mark();
        boolean success = tokenType() == IDENTIFIER;
        if (success) {
            String name = getTokenText();
            advanceLexer();
            mark.done(VARIABLE_NAME);
            return name;
        } else {
            mark.error("Expected an identifier.");
            return null;
        }
    }

    private String parseFieldIdentifier() {
        final PsiBuilder.Marker mark = mark();
        boolean success = tokenType() == IDENTIFIER;
        if (success) {
            String name = getTokenText();
            advanceLexer();
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

        final PsiBuilder.Marker mark = mark();

        if (!parseTypeSpecifierNoArray()) {
            mark.drop();
            return false;
        }

        if (tokenType() == LEFT_BRACKET) {
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

        final PsiBuilder.Marker mark = mark();

        if (tokenType() == STRUCT) {
            parseStructSpecifier();
            mark.done(TYPE_SPECIFIER_STRUCT);
        } else if (TYPE_SPECIFIER_NONARRAY_TOKENS.contains(tokenType())) {
            advanceLexer();
            mark.done(TYPE_SPECIFIER_PRIMITIVE);
        } else if (tokenType() == IDENTIFIER) {
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

        if (tokenType() == IDENTIFIER) {
            parseIdentifier();
        }

        match(LEFT_BRACE, "'{' expected after 'struct'.");

        parseStructDeclarationList();

        match(RIGHT_BRACE, "Closing '}' for struct expected.");
    }

    private void parseStructDeclarationList() {
        // struct_declaration_list: struct_declaration (',' struct_declaration)*
        // note: we should initially find ',' for a new declarator or '}' at the end of the struct

        final PsiBuilder.Marker mark = mark();

        if (tokenType() == RIGHT_BRACE) {
            error("Empty struct is not allowed.");
        }

        while (GLSLTokenTypes.TYPE_SPECIFIER_NONARRAY_TOKENS.contains(tokenType()) ||
                tokenType() == GLSLTokenTypes.IDENTIFIER) {
            parseStructDeclaration();
        }

        mark.done(STRUCT_DECLARATION_LIST);
    }

    private void parseStructDeclaration() {
        // type_specifier struct_declarator_list ';'

        final PsiBuilder.Marker mark = mark();

        parseQualifiedTypeSpecifier();
        parseStructDeclaratorList();
        match(SEMICOLON, "Expected ';' after declaration.");

        mark.done(STRUCT_DECLARATION);
    }

    private void parseStructDeclaratorList() {
        // struct_declarator_list: struct_declarator (',' struct_declarator)*

        final PsiBuilder.Marker mark = mark();
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

        final PsiBuilder.Marker mark = mark();

        parseIdentifier();

        if (tokenType() == LEFT_BRACKET) {
            advanceLexer();
            parseConstantExpression();
            match(RIGHT_BRACKET, "Expected ']' after constant expression.");
        }

        PsiBuilder.Marker declaratorEnd = mark();
        if (tryMatch(EQUAL)) {
            parseInitializer();
            declaratorEnd.error("Initializer not allowed here.");
        } else {
            declaratorEnd.drop();
        }
        mark.done(type);
    }

    private void parseQualifierList(boolean validPlacement) {

        final PsiBuilder.Marker mark = mark();

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
        //          | qualifier_token

        if(parseLayoutQualifier())return true;

        if (QUALIFIER_TOKENS.contains(tokenType())) {
            final PsiBuilder.Marker mark = mark();
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

        final PsiBuilder.Marker mark = mark();

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

        if(tokenType() == LAYOUT_KEYWORD){
            final PsiBuilder.Marker mark = mark();
            advanceLexer();
            match(LEFT_PAREN, "Expected '('");
            parseLayoutQualifierList();
            match(RIGHT_PAREN, "Expected ')'");
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
        final PsiBuilder.Marker mark = mark();

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
