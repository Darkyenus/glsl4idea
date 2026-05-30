package glslplugin.annotation.impl;

import glslplugin.GLSLHighlighter;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.declarations.GLSLInterfaceBlockDefinition;
import glslplugin.lang.elements.declarations.GLSLInterfaceBlockMemberDeclaration;
import glslplugin.lang.elements.declarations.GLSLQualifiedDeclaration;
import glslplugin.lang.elements.declarations.GLSLParameterDeclaration;
import glslplugin.lang.elements.declarations.GLSLQualifier;
import glslplugin.lang.elements.declarations.GLSLStructDefinition;
import glslplugin.lang.elements.declarations.GLSLVariableDeclaration;
import glslplugin.lang.elements.expressions.GLSLFieldSelectionExpression;
import glslplugin.lang.elements.types.GLSLInterfaceBlockType;
import glslplugin.lang.elements.types.GLSLQualifiedType;
import org.jetbrains.annotations.Nullable;

final class GLSLHighlightClassifier {
    private GLSLHighlightClassifier() {}

    enum HighlightKind {
        NONE,
        GLOBAL_IN,
        GLOBAL_OUT,
        UNIFORM,
        VARYING,
        ATTRIBUTE,
        STRUCT_FIELD,
        INTERFACE_BLOCK_MEMBER
    }

    static @Nullable com.intellij.openapi.editor.colors.TextAttributesKey keyFor(HighlightKind kind) {
        return switch (kind) {
            case GLOBAL_IN -> GLSLHighlighter.GLSL_IDENTIFIER_IN[0];
            case GLOBAL_OUT -> GLSLHighlighter.GLSL_IDENTIFIER_OUT[0];
            case UNIFORM -> GLSLHighlighter.GLSL_IDENTIFIER_UNIFORM[0];
            case VARYING -> GLSLHighlighter.GLSL_IDENTIFIER_VARYING[0];
            case ATTRIBUTE -> GLSLHighlighter.GLSL_IDENTIFIER_ATTRIBUTE[0];
            case STRUCT_FIELD -> GLSLHighlighter.GLSL_IDENTIFIER_STRUCT_FIELD[0];
            case INTERFACE_BLOCK_MEMBER -> GLSLHighlighter.GLSL_IDENTIFIER_INTERFACE_BLOCK[0];
            default -> null;
        };
    }

    static HighlightKind classify(GLSLDeclarator declarator) {
        final GLSLQualifiedType qualifiedType = declarator.getQualifiedType();
        final GLSLQualifiedDeclaration parentDeclaration = declarator.getParentDeclaration();

        if (parentDeclaration instanceof GLSLParameterDeclaration) {
            return HighlightKind.NONE;
        }

        if (declarator.findParentByClass(GLSLInterfaceBlockDefinition.class) != null
                || declarator.findParentByClass(GLSLInterfaceBlockMemberDeclaration.class) != null) {
            return HighlightKind.INTERFACE_BLOCK_MEMBER;
        }

        if (declarator.findParentByClass(GLSLStructDefinition.class) != null) {
            return HighlightKind.STRUCT_FIELD;
        }

        if (parentDeclaration != null) {
            if (qualifiedType.hasQualifier(GLSLQualifier.Qualifier.UNIFORM)) {
                return HighlightKind.UNIFORM;
            }
            if (qualifiedType.hasQualifier(GLSLQualifier.Qualifier.VARYING)) {
                return HighlightKind.VARYING;
            }
            if (qualifiedType.hasQualifier(GLSLQualifier.Qualifier.ATTRIBUTE)) {
                return HighlightKind.ATTRIBUTE;
            }
            if (qualifiedType.hasQualifier(GLSLQualifier.Qualifier.IN)) {
                return HighlightKind.GLOBAL_IN;
            }
            if (qualifiedType.hasQualifier(GLSLQualifier.Qualifier.OUT)) {
                return HighlightKind.GLOBAL_OUT;
            }
            if (parentDeclaration instanceof GLSLVariableDeclaration variableDeclaration && isInterfaceBlockInstance(variableDeclaration)) {
                return interfaceBlockKind(parentDeclaration);
            }
        }

        return HighlightKind.NONE;
    }

    static HighlightKind classify(GLSLFieldSelectionExpression.FieldReference reference) {
        if (reference.getElement().isSwizzle()) {
            return HighlightKind.NONE;
        }
        final GLSLDeclarator resolved = reference.resolve();
        if (resolved == null) {
            return HighlightKind.NONE;
        }
        if (resolved.findParentByClass(GLSLInterfaceBlockDefinition.class) != null
                || resolved.findParentByClass(GLSLInterfaceBlockMemberDeclaration.class) != null) {
            return HighlightKind.INTERFACE_BLOCK_MEMBER;
        }
        if (resolved.findParentByClass(GLSLStructDefinition.class) != null) {
            return HighlightKind.STRUCT_FIELD;
        }
        return HighlightKind.NONE;
    }

    private static boolean isInterfaceBlockInstance(GLSLVariableDeclaration declaration) {
        return declaration.getTypeSpecifierNode() != null
                && declaration.getTypeSpecifierNode().getType() instanceof GLSLInterfaceBlockType;
    }

    private static HighlightKind interfaceBlockKind(GLSLQualifiedDeclaration declaration) {
        final GLSLQualifiedType qualifiedType = new GLSLQualifiedType(declaration.getTypeSpecifierNode().getType(), declaration.getQualifiers());
        if (qualifiedType.hasQualifier(GLSLQualifier.Qualifier.UNIFORM)) {
            return HighlightKind.UNIFORM;
        }
        if (qualifiedType.hasQualifier(GLSLQualifier.Qualifier.BUFFER)) {
            return HighlightKind.INTERFACE_BLOCK_MEMBER;
        }
        if (qualifiedType.hasQualifier(GLSLQualifier.Qualifier.IN)) {
            return HighlightKind.GLOBAL_IN;
        }
        if (qualifiedType.hasQualifier(GLSLQualifier.Qualifier.OUT)) {
            return HighlightKind.GLOBAL_OUT;
        }
        return HighlightKind.NONE;
    }
}
