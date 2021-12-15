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

import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.tree.IElementType;
import glslplugin.lang.GLSLFileType;
import glslplugin.lang.elements.declarations.*;
import glslplugin.lang.elements.expressions.*;
import glslplugin.lang.elements.preprocessor.*;
import glslplugin.lang.elements.statements.*;
import org.jetbrains.annotations.NotNull;

/**
 * GLSLPsiElementFactory defines the interface for the GLSLElement factory.
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 28, 2009
 *         Time: 11:12:19 AM
 */
public class GLSLPsiElementFactory {
    /**
     * Creates a GLSLElement from the given ast node.
     *
     * @param node the source node.
     * @return the resulting GLSLElement, may be null.
     */
    public GLSLElement create(ASTNode node) {
        if (node == null) {
            return null;
        }
        IElementType type = node.getElementType();

        if (type instanceof GLSLElementTypes.RedefinedTokenElementType /*== GLSLElementTypes.REDEFINED_TOKEN*/) return new GLSLRedefinedToken(node);
        if (type == GLSLTokenTypes.PREPROCESSOR_DEFINE) return new GLSLDefineDirective(node);
        if (type == GLSLTokenTypes.PREPROCESSOR_VERSION) return new GLSLVersionDirective(node);
        if (GLSLTokenTypes.PREPROCESSOR_DIRECTIVES.contains(type)) return new GLSLPreprocessorDirective(node);

        // primary expressions
        if (type == GLSLElementTypes.VARIABLE_NAME_EXPRESSION) return new GLSLIdentifierExpression(node);
        if (type == GLSLElementTypes.CONSTANT_EXPRESSION) return new GLSLLiteral(node);
        if (type == GLSLElementTypes.GROUPED_EXPRESSION) return new GLSLGroupedExpression(node);

        // postfix expressions

        // unary operators
        if (type == GLSLElementTypes.POSTFIX_OPERATOR_EXPRESSION) return new GLSLUnaryOperatorExpression(node, false);
        if (type == GLSLElementTypes.PREFIX_OPERATOR_EXPRESSION) return new GLSLUnaryOperatorExpression(node, true);
        if (type == GLSLElementTypes.SUBSCRIPT_EXPRESSION) return new GLSLSubscriptExpression(node);

        // binary operators
        if (type == GLSLElementTypes.ADDITIVE_EXPRESSION ||
                type == GLSLElementTypes.MULTIPLICATIVE_EXPRESSION ||
                type == GLSLElementTypes.RELATIONAL_EXPRESSION ||
                type == GLSLElementTypes.EQUALITY_EXPRESSION ||
                type == GLSLElementTypes.LOGICAL_AND_EXPRESSION ||
                type == GLSLElementTypes.LOGICAL_OR_EXPRESSION ||
                type == GLSLElementTypes.LOGICAL_XOR_EXPRESSION ||
                type == GLSLElementTypes.BIT_SHIFT_EXPRESSION ||
                type == GLSLElementTypes.BINARY_AND_EXPRESSION ||
                type == GLSLElementTypes.BINARY_XOR_EXPRESSION ||
                type == GLSLElementTypes.BINARY_OR_EXPRESSION
                ) return new GLSLBinaryOperatorExpression(node);
        if (type == GLSLElementTypes.ASSIGNMENT_EXPRESSION) return new GLSLAssignmentExpression(node);
        if (type == GLSLElementTypes.CONDITION) return new GLSLCondition(node);

        if (type == GLSLElementTypes.FIELD_SELECTION_EXPRESSION) return new GLSLFieldSelectionExpression(node);
        if (type == GLSLElementTypes.FUNCTION_CALL_EXPRESSION) return new GLSLFunctionCallExpression(node);
        if (type == GLSLElementTypes.METHOD_CALL_EXPRESSION) return new GLSLMethodCallExpression(node);
        if (type == GLSLElementTypes.FUNCTION_NAME) return new GLSLIdentifier(node);
        if (type == GLSLElementTypes.PARAMETER_LIST) return new GLSLParameterList(node);
        if (type == GLSLElementTypes.METHOD_NAME) return new GLSLIdentifier(node);
        if (type == GLSLElementTypes.FIELD_NAME) return new GLSLIdentifier(node);
        if (type == GLSLElementTypes.VARIABLE_NAME) return new GLSLIdentifier(node);

        if (type == GLSLElementTypes.CONDITIONAL_EXPRESSION) return new GLSLConditionalExpression(node);

        if (type == GLSLElementTypes.FUNCTION_DEFINITION) return new GLSLFunctionDefinitionImpl(node);
        if (type == GLSLElementTypes.FUNCTION_DECLARATION) return new GLSLFunctionDeclarationImpl(node);
        if (type == GLSLElementTypes.PARAMETER_DECLARATION_LIST) return new GLSLDeclarationList(node);
        if (type == GLSLElementTypes.PARAMETER_DECLARATION) return new GLSLParameterDeclaration(node);
        if (type == GLSLElementTypes.PARAMETER_DECLARATOR) return new GLSLDeclarator(node);

        // statements:
        if (type == GLSLElementTypes.EXPRESSION_STATEMENT) return new GLSLExpressionStatement(node);
        if (type == GLSLElementTypes.COMPOUND_STATEMENT) return new GLSLCompoundStatement(node);
        if (type == GLSLElementTypes.IF_STATEMENT) return new GLSLIfStatement(node);
        if (type == GLSLElementTypes.FOR_STATEMENT) return new GLSLForStatement(node);
        if (type == GLSLElementTypes.WHILE_STATEMENT) return new GLSLWhileStatement(node);
        if (type == GLSLElementTypes.DO_STATEMENT) return new GLSLDoStatement(node);
        if (type == GLSLElementTypes.SWITCH_STATEMENT) return new GLSLSwitchStatement(node);

        if (type == GLSLElementTypes.DECLARATION_STATEMENT) return new GLSLDeclarationStatement(node);
        if (type == GLSLElementTypes.VARIABLE_DECLARATION) return new GLSLVariableDeclaration(node);
        if (type == GLSLElementTypes.DECLARATOR_LIST) return new GLSLDeclaratorList(node);
        if (type == GLSLElementTypes.DECLARATOR) return new GLSLDeclarator(node);
        if (type == GLSLElementTypes.INITIALIZER) return new GLSLInitializerExpression(node);
        if (type == GLSLElementTypes.INITIALIZER_LIST) return new GLSLInitializerList(node);
        if (type == GLSLElementTypes.ARRAY_DECLARATOR) return new GLSLArraySpecifier(node);

        if (type == GLSLElementTypes.BREAK_STATEMENT) return new GLSLBreakStatement(node);
        if (type == GLSLElementTypes.CONTINUE_STATEMENT) return new GLSLContinueStatement(node);
        if (type == GLSLElementTypes.RETURN_STATEMENT) return new GLSLReturnStatement(node);
        if (type == GLSLElementTypes.DISCARD_STATEMENT) return new GLSLDiscardStatement(node);
        if (type == GLSLElementTypes.CASE_STATEMENT) return new GLSLCaseStatement(node);
        if (type == GLSLElementTypes.DEFAULT_STATEMENT) return new GLSLDefaultStatement(node);

        if (type == GLSLElementTypes.PRECISION_STATEMENT) return new GLSLPrecisionStatement(node);

        // types and structs
        if (type == GLSLElementTypes.QUALIFIER) return new GLSLQualifier(node);
        if (type == GLSLElementTypes.QUALIFIER_LIST) return new GLSLQualifierList(node);
        if (type == GLSLElementTypes.TYPE_SPECIFIER) return new GLSLTypeSpecifier(node);
        if (type == GLSLElementTypes.TYPE_SPECIFIER_PRIMITIVE) return new GLSLTypename(node);
        if (type == GLSLElementTypes.TYPE_SPECIFIER_STRUCT_REFERENCE) return new GLSLTypename(node);
        if (type == GLSLElementTypes.TYPE_SPECIFIER_STRUCT) return new GLSLStructDefinition(node);

        if (type == GLSLElementTypes.STRUCT_DECLARATION_LIST) return new GLSLDeclarationList(node);
        if (type == GLSLElementTypes.STRUCT_MEMBER_DECLARATION) return new GLSLStructMemberDeclaration(node);
        if (type == GLSLElementTypes.STRUCT_DECLARATOR_LIST) return new GLSLDeclaratorList(node);
        if (type == GLSLElementTypes.STRUCT_DECLARATOR) return new GLSLDeclarator(node);

        return null;
    }

    @NotNull
    public static PsiElement createLeafElement(Project project, String name) {
        PsiElement element = PsiFileFactory.getInstance(project).
                createFileFromText("dummy.glsl", GLSLFileType.INSTANCE, name);
        while (element.getFirstChild() != null) element = element.getFirstChild();
        return element;
    }
}
