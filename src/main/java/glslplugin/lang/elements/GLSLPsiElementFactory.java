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
import com.intellij.util.IncorrectOperationException;
import glslplugin.lang.GLSLFileType;
import glslplugin.lang.elements.declarations.GLSLArraySpecifier;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.declarations.GLSLDeclaratorList;
import glslplugin.lang.elements.declarations.GLSLFunctionDeclarationImpl;
import glslplugin.lang.elements.declarations.GLSLFunctionDefinitionImpl;
import glslplugin.lang.elements.declarations.GLSLInitializerExpression;
import glslplugin.lang.elements.declarations.GLSLInitializerList;
import glslplugin.lang.elements.declarations.GLSLParameterDeclaration;
import glslplugin.lang.elements.declarations.GLSLQualifier;
import glslplugin.lang.elements.declarations.GLSLQualifierList;
import glslplugin.lang.elements.declarations.GLSLStructDefinition;
import glslplugin.lang.elements.declarations.GLSLStructMemberDeclaration;
import glslplugin.lang.elements.declarations.GLSLTypeSpecifier;
import glslplugin.lang.elements.declarations.GLSLTypename;
import glslplugin.lang.elements.declarations.GLSLVariableDeclaration;
import glslplugin.lang.elements.expressions.GLSLAssignmentExpression;
import glslplugin.lang.elements.expressions.GLSLBinaryOperatorExpression;
import glslplugin.lang.elements.expressions.GLSLCondition;
import glslplugin.lang.elements.expressions.GLSLConditionalExpression;
import glslplugin.lang.elements.expressions.GLSLFieldSelectionExpression;
import glslplugin.lang.elements.expressions.GLSLFunctionOrConstructorCallExpression;
import glslplugin.lang.elements.expressions.GLSLGroupedExpression;
import glslplugin.lang.elements.expressions.GLSLLiteral;
import glslplugin.lang.elements.expressions.GLSLMethodCallExpression;
import glslplugin.lang.elements.expressions.GLSLParameterList;
import glslplugin.lang.elements.expressions.GLSLSubscriptExpression;
import glslplugin.lang.elements.expressions.GLSLUnaryOperatorExpression;
import glslplugin.lang.elements.expressions.GLSLVariableExpression;
import glslplugin.lang.elements.preprocessor.GLSLDefineDirective;
import glslplugin.lang.elements.preprocessor.GLSLFlowAttribute;
import glslplugin.lang.elements.preprocessor.GLSLPreprocessorDirective;
import glslplugin.lang.elements.preprocessor.GLSLPreprocessorInclude;
import glslplugin.lang.elements.preprocessor.GLSLRedefinedToken;
import glslplugin.lang.elements.preprocessor.GLSLVersionDirective;
import glslplugin.lang.elements.statements.GLSLBreakStatement;
import glslplugin.lang.elements.statements.GLSLCaseStatement;
import glslplugin.lang.elements.statements.GLSLCompoundStatement;
import glslplugin.lang.elements.statements.GLSLContinueStatement;
import glslplugin.lang.elements.statements.GLSLDeclarationStatement;
import glslplugin.lang.elements.statements.GLSLDefaultStatement;
import glslplugin.lang.elements.statements.GLSLDiscardStatement;
import glslplugin.lang.elements.statements.GLSLDoStatement;
import glslplugin.lang.elements.statements.GLSLExpressionStatement;
import glslplugin.lang.elements.statements.GLSLForStatement;
import glslplugin.lang.elements.statements.GLSLIfStatement;
import glslplugin.lang.elements.statements.GLSLPrecisionStatement;
import glslplugin.lang.elements.statements.GLSLReturnStatement;
import glslplugin.lang.elements.statements.GLSLSwitchStatement;
import glslplugin.lang.elements.statements.GLSLWhileStatement;

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

        if (type == GLSLTokenTypes.PREPROCESSOR_DEFINE) return new GLSLDefineDirective(node);
        if (type == GLSLTokenTypes.PREPROCESSOR_VERSION) return new GLSLVersionDirective(node);
        if (type == GLSLTokenTypes.PREPROCESSOR_INCLUDE) return new GLSLPreprocessorInclude(node);
        if (GLSLTokenTypes.PREPROCESSOR_DIRECTIVES.contains(type)) return new GLSLPreprocessorDirective(node);

        // primary expressions
        if (type == GLSLElementTypes.VARIABLE_NAME_EXPRESSION) return new GLSLVariableExpression(node);
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
        if (type == GLSLElementTypes.FUNCTION_CALL_EXPRESSION) return new GLSLFunctionOrConstructorCallExpression(node);
        if (type == GLSLElementTypes.METHOD_CALL_EXPRESSION) return new GLSLMethodCallExpression(node);
        if (type == GLSLElementTypes.PARAMETER_LIST) return new GLSLParameterList(node);

        if (type == GLSLElementTypes.CONDITIONAL_EXPRESSION) return new GLSLConditionalExpression(node);

        if (type == GLSLElementTypes.FUNCTION_DEFINITION) return new GLSLFunctionDefinitionImpl(node);
        if (type == GLSLElementTypes.FUNCTION_DECLARATION) return new GLSLFunctionDeclarationImpl(node);
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
        if (type == GLSLElementTypes.FLOW_ATTRIBUTE) return new GLSLFlowAttribute(node);

        // types and structs
        if (type == GLSLElementTypes.QUALIFIER) return new GLSLQualifier(node);
        if (type == GLSLElementTypes.QUALIFIER_LIST) return new GLSLQualifierList(node);
        if (type == GLSLElementTypes.TYPE_SPECIFIER) return new GLSLTypeSpecifier(node);
        if (type == GLSLElementTypes.TYPE_SPECIFIER_PRIMITIVE) return new GLSLTypename(node);
        if (type == GLSLElementTypes.TYPE_SPECIFIER_STRUCT_REFERENCE) return new GLSLTypename(node);
        if (type == GLSLElementTypes.TYPE_SPECIFIER_STRUCT) return new GLSLStructDefinition(node);

        if (type == GLSLElementTypes.STRUCT_MEMBER_DECLARATION) return new GLSLStructMemberDeclaration(node);
        if (type == GLSLElementTypes.STRUCT_DECLARATOR_LIST) return new GLSLDeclaratorList(node);
        if (type == GLSLElementTypes.STRUCT_DECLARATOR) return new GLSLDeclarator(node);

        return null;
    }

    public static ASTNode createIdentifier(Project project, String name) throws IncorrectOperationException {
        PsiElement element = PsiFileFactory.getInstance(project).createFileFromText("dummy.glsl", GLSLFileType.INSTANCE, name);
        final ASTNode node = element.getNode().getFirstChildNode();
        if (node != null && node.getElementType() == GLSLTokenTypes.IDENTIFIER) {
            return node;
        }
        throw new IncorrectOperationException("'"+name+"' is not a valid identifier");
    }

    public static ASTNode createPreprocessorString(Project project, String name) throws IncorrectOperationException {
        PsiElement element = PsiFileFactory.getInstance(project).createFileFromText("dummy.glsl", GLSLFileType.INSTANCE, "# "+name);
        final ASTNode node = element.getNode().getLastChildNode();
        if (node != null && node.getElementType() == GLSLTokenTypes.PREPROCESSOR_STRING) {
            return node;
        }
        throw new IncorrectOperationException("'"+name+"' is not a valid preprocessor string");
    }
}
