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

package glslplugin.extensions;

import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.containers.ContainerUtil;
import glslplugin.lang.elements.GLSLIdentifier;
import glslplugin.lang.elements.declarations.GLSLDeclaration;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.declarations.GLSLFunctionDeclaration;
import glslplugin.lang.elements.declarations.GLSLParameterDeclaration;
import glslplugin.lang.elements.declarations.GLSLQualifier;
import glslplugin.lang.elements.declarations.GLSLSingleDeclarationImpl;
import glslplugin.lang.elements.declarations.GLSLStructDefinition;
import glslplugin.lang.elements.declarations.GLSLStructMemberDeclaration;
import glslplugin.lang.elements.declarations.GLSLTypename;
import glslplugin.lang.elements.declarations.GLSLVariableDeclaration;
import glslplugin.lang.elements.expressions.GLSLFieldSelectionExpression;
import glslplugin.lang.elements.expressions.GLSLFunctionOrConstructorCallExpression;
import glslplugin.lang.elements.reference.GLSLReferenceBase;
import glslplugin.lang.elements.reference.GLSLTypeReference;
import glslplugin.lang.elements.types.GLSLBasicFunctionType;
import glslplugin.lang.elements.types.GLSLFunctionType;
import glslplugin.lang.elements.types.GLSLQualifiedType;
import glslplugin.lang.elements.types.GLSLStructType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.constructors.GLSLBasicConstructorType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * @author Wyozi
 */
public class GLSLDocumentationProvider extends AbstractDocumentationProvider {
    private static String getElementDocHeader(@Nullable PsiElement element) {
        if (element == null)
            return "<u>internal</u><br><br>";
        return "<b><u>" + element.getContainingFile().getName() + "</u></b><br><br>";
    }

    private static String getFunctionDocumentation(@NotNull GLSLFunctionType type, @Nullable final List<String> parameterNames) {
        GLSLType[] parameterTypes = null;
        if (type instanceof GLSLBasicFunctionType) {
            parameterTypes = ((GLSLBasicFunctionType) type).getParameterTypes();
        } else if (type instanceof GLSLBasicConstructorType) {
            parameterTypes = ((GLSLBasicConstructorType) type).getParameterTypes();
        }

        String paramsString;
        if (parameterTypes != null) {
            String[] paramStrings = new String[parameterTypes.length];
            for (int i = 0;i < parameterTypes.length; i++) {
                String paramName = parameterNames != null ? parameterNames.get(i) : null;

                String typename = parameterTypes[i].getTypename();
                if (paramName != null) {
                    // TODO link typename to definition
                    paramStrings[i] = typename + " <b>" + paramName + "</b>";
                } else {
                    paramStrings[i] = typename;
                }
            }
            paramsString = StringUtil.join(paramStrings, ", ");
        } else {
            paramsString = "";
        }

        return getElementDocHeader(type.getDefinition()) +
                "<code>" + type.getReturnType().getTypename() + " <b>" + type.getName() + "</b>(" + paramsString + ")</code>";
    }
    private static String getFunctionDocumentation(@NotNull GLSLFunctionDeclaration decl) {
        return getFunctionDocumentation(decl.getType(), ContainerUtil.map(decl.getParameters(), GLSLSingleDeclarationImpl::getName));
    }

    private static String getNamedTypedElementDocumentation(PsiNamedElement element, String typename) {
        return getElementDocHeader(element) +
                "<code>" + typename + " <b>" + element.getName() + "</b></code>";
    }
    private static String getNamedTypedElementDocumentation(PsiNamedElement element, GLSLQualifiedType qualifiedType) {
        StringBuilder typeString = new StringBuilder(qualifiedType.getType().getTypename());
        for (GLSLQualifier glslQualifier : qualifiedType.getQualifiers()) {
            GLSLQualifier.Qualifier qualifier = glslQualifier.getQualifier();
            if (qualifier == null) continue;

            if (typeString.length() > 0) {
                typeString.append(' ');
            }
            typeString.append(qualifier);
        }
        return getNamedTypedElementDocumentation(element, typeString.toString());
    }

    private static String getStructDocumentation(PsiNamedElement element) {
        return getNamedTypedElementDocumentation(element, "struct");
    }
    private static String getStructDocumentation(GLSLStructDefinition typeDefinition) {
        return getStructDocumentation((PsiNamedElement)typeDefinition);
    }

    private static String getVariableDocumentation(GLSLDeclarator variableDeclaration) {
        return getNamedTypedElementDocumentation(variableDeclaration, variableDeclaration.getQualifiedType());
    }

    private static String getDocumentation(PsiElement element, PsiElement originalElement) {
        PsiElement parent = element.getParent();

        // For some reason opening documentation on a struct constructor returns GLSLTypeDefinition while we want
        // to consider it a constructor call.
        if (element instanceof GLSLStructDefinition && originalElement != null) {
            //TODO Check that this case works now
        }

        if (element instanceof GLSLIdentifier) {
            String parentDoc = getDocumentation(parent, null);
            if (parentDoc != null)
                return parentDoc;

            PsiReference reference = element.getReference();
            if (reference == null && parent instanceof GLSLReferenceBase) reference = ((PsiReference) parent);

            if (reference instanceof GLSLTypeReference) {
                GLSLStructDefinition typeDef = ((GLSLTypeReference) reference).resolve();
                if (typeDef == null) return null;

                GLSLStructType type = typeDef.getType();
                return getNamedTypedElementDocumentation(((GLSLIdentifier) element), type.getTypename());
            } else {
                return null;
            }
        } else if (element instanceof GLSLStructDefinition) {
            return getStructDocumentation(((GLSLStructDefinition) element));
        } else if (element instanceof GLSLFunctionDeclaration) {
            return getFunctionDocumentation(((GLSLFunctionDeclaration) element));
        } else if (element instanceof GLSLDeclarator) {
            GLSLDeclaration elementDeclaration = ((GLSLDeclarator) element).getParentDeclaration();
            if (elementDeclaration instanceof GLSLFunctionDeclaration) {
                return getFunctionDocumentation(((GLSLFunctionDeclaration) elementDeclaration));
            } else if (elementDeclaration instanceof GLSLStructDefinition) {
                return getStructDocumentation(((GLSLStructDefinition) elementDeclaration));
            } else if (elementDeclaration instanceof GLSLVariableDeclaration || elementDeclaration instanceof GLSLParameterDeclaration) {
                return getVariableDocumentation(((GLSLDeclarator) element));
            }

            // This happens if we index a struct variable of a struct
            if (elementDeclaration instanceof GLSLStructMemberDeclaration) {
                return getVariableDocumentation(((GLSLDeclarator) element));
            }
        } else if (element instanceof GLSLFunctionOrConstructorCallExpression) {
            GLSLFunctionOrConstructorCallExpression functionCallExpression = (GLSLFunctionOrConstructorCallExpression) element;
            final GLSLFunctionOrConstructorCallExpression.FunctionCallOrConstructorReference reference = functionCallExpression.getReference();
            final PsiElement target = reference != null ? reference.resolve() : null;
            if (target instanceof GLSLFunctionDeclaration) {
                return getFunctionDocumentation(((GLSLFunctionDeclaration) target).getType(), null);
            } else if (target instanceof GLSLStructDefinition) {
                final GLSLStructType structType = ((GLSLStructDefinition) target).getType();
                return getFunctionDocumentation(structType.getConstructor(), Arrays.asList(structType.getMemberNames()));
            } else {
                //TODO What is it?
            }
        } else if (element instanceof GLSLTypename) {
            GLSLStructDefinition typeDefinition = ((GLSLTypename) element).getStructDefinition();
            if (typeDefinition != null) {
                return getStructDocumentation(typeDefinition);
            }
        } else if (element instanceof GLSLFieldSelectionExpression) {
            GLSLFieldSelectionExpression fieldSelectionExpression = (GLSLFieldSelectionExpression) element;
            GLSLType type = fieldSelectionExpression.getType();
            return getNamedTypedElementDocumentation(fieldSelectionExpression.getMemberIdentifier(), type.getTypename());
        }

        return null;
    }

    @Override
    public String generateDoc(PsiElement element, @Nullable PsiElement originalElement) {
        return getDocumentation(element, originalElement);
    }
}
