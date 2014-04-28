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

package glslplugin.lang.elements.expressions;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLIdentifier;
import glslplugin.lang.elements.GLSLReferenceElement;
import glslplugin.lang.elements.declarations.GLSLFunctionDeclaration;
import glslplugin.lang.elements.declarations.GLSLFunctionDefinition;
import glslplugin.lang.elements.declarations.GLSLTypeSpecifier;
import glslplugin.lang.elements.declarations.GLSLVariableDeclaration;
import glslplugin.lang.elements.reference.GLSLFunctionReference;
import glslplugin.lang.elements.types.GLSLFunctionType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * GLSLFunctionCallExpression is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 29, 2009
 *         Time: 10:34:04 AM
 */
public class GLSLFunctionCallExpression extends GLSLExpression implements GLSLReferenceElement {
    public GLSLFunctionCallExpression(@NotNull ASTNode astNode) {
        super(astNode);
    }

    public GLSLIdentifier getFunctionNameIdentifier() {
        final PsiElement first = getFirstChild();
        return (GLSLIdentifier) first;
    }

    public String getFunctionName() {
        return getFunctionNameIdentifier().getIdentifierName();
    }

    public GLSLParameterList getParameterList() {
        return findChildByClass(GLSLParameterList.class);
    }

    @NotNull
    @Override
    public GLSLType getType() {
        GLSLFunctionType[] functionTypes = findFunctionTypes();
        if (functionTypes.length == 1) {
            return functionTypes[0].getBaseType();
        } else {
            return GLSLTypes.UNKNOWN_TYPE;
        }
    }

    public GLSLFunctionReference getReferenceProxy() {
        GLSLElement[] declarations = findFunctionDeclarations();
        if (declarations.length > 0) {
            return new GLSLFunctionReference(getFunctionNameIdentifier(), declarations);
        } else {
            return null;
        }
    }

    private GLSLElement[] findFunctionDeclarations() {
        GLSLFunctionType[] declarations = findFunctionTypes();
        List<GLSLElement> realDeclarations = new ArrayList<GLSLElement>();
        for (int i = 0; i < declarations.length; i++) {
            GLSLElement element = declarations[i].getDefinition();
            if (element != null) {
                realDeclarations.add(element);
            }
        }
        return realDeclarations.toArray(new GLSLElement[realDeclarations.size()]);
    }

    public GLSLFunctionType[] findFunctionTypes() {
        List<GLSLFunctionType> compatibleDeclarations = new ArrayList<GLSLFunctionType>();

        PsiElement current = findParentByClass(GLSLFunctionDefinition.class);

        //todo: fails on vec3 and such...
        // todo: also fails for function calls in initializers of global variables (can only be vec3 and such there...)
        assert current != null : "GLSLFunctionDeclaration for '" + getFunctionName() + "' not found.";

        // Is this a constructor for one of the built-in types?
        GLSLType builtInType = GLSLTypes.getTypeFromName(getFunctionName());
        if (builtInType != null) {
            return GLSLFunctionType.findApplicableTypes(builtInType.getConstructors(), getParameterList().getParameterTypes());
        }

        while (current != null) {

            GLSLFunctionType functionType;

            if (current instanceof GLSLFunctionDeclaration) {
                GLSLFunctionDeclaration function = (GLSLFunctionDeclaration) current;
                functionType = function.getType();

                if (getFunctionName().equals(functionType.getName())) {
                    switch (functionType.getParameterCompatibilityLevel(getParameterList().getParameterTypes())) {
                        case COMPATIBLE_WITH_IMPLICIT_CONVERSION:
                            compatibleDeclarations.add(functionType);
                            break;

                        case DIRECTLY_COMPATIBLE:
                            return new GLSLFunctionType[]{functionType};

                        case INCOMPATIBLE:
                            break;

                        default:
                            assert false : "Unsupported compatibility level.";
                    }
                }

            } else if (current instanceof GLSLVariableDeclaration) {
                GLSLVariableDeclaration declaration = (GLSLVariableDeclaration) current;
                GLSLTypeSpecifier typeSpecifier = declaration.getTypeSpecifierNode();
                GLSLType type = typeSpecifier.getType();
                if (getFunctionName().equals(type.getTypename())) {
                    for (GLSLFunctionType constructor : type.getConstructors()) {
                        switch (constructor.getParameterCompatibilityLevel(getParameterList().getParameterTypes())) {
                            case COMPATIBLE_WITH_IMPLICIT_CONVERSION:
                                compatibleDeclarations.add(constructor);
                                break;

                            case DIRECTLY_COMPATIBLE:
                                return new GLSLFunctionType[]{constructor};

                            case INCOMPATIBLE:
                                break;

                            default:
                                assert false : "Unsupported compatibility level.";
                        }
                    }
                }
            }

            current = current.getPrevSibling();
        }

        return compatibleDeclarations.toArray(new GLSLFunctionType[compatibleDeclarations.size()]);
    }

    //todo: need an isConstructor method

    @Override
    public String toString() {
        return "Function Call: " + getFunctionName();
    }
}
