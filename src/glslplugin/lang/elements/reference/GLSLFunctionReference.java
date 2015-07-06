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

package glslplugin.lang.elements.reference;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.ResolveResult;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLIdentifier;
import glslplugin.lang.elements.declarations.GLSLFunctionDeclaration;
import glslplugin.lang.elements.declarations.GLSLFunctionDefinition;
import glslplugin.lang.elements.declarations.GLSLTypeSpecifier;
import glslplugin.lang.elements.declarations.GLSLVariableDeclaration;
import glslplugin.lang.elements.expressions.GLSLFunctionCallExpression;
import glslplugin.lang.elements.expressions.GLSLParameterList;
import glslplugin.lang.elements.types.GLSLFunctionType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * GLSLFunctionReference is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Mar 1, 2009
 *         Time: 10:04:11 PM
 */
public class GLSLFunctionReference extends GLSLReferenceBase<GLSLIdentifier, GLSLElement>
        implements PsiPolyVariantReference {

    GLSLFunctionCallExpression sourceExpression = null;
    GLSLElement[] targets;

    public GLSLFunctionReference(GLSLFunctionCallExpression source) {
        super(source.getFunctionNameIdentifier());
        this.sourceExpression = source;
    }

    @NotNull
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        GLSLElement[] declarations = findFunctionDeclarations();
        ResolveResult[] result = new ResolveResult[declarations.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = new PsiElementResolveResult(declarations[i]);
        }
        return result;
    }

    @Override
    public GLSLElement resolve() {
        GLSLElement[] declarations = findFunctionDeclarations();
        if (declarations.length == 1) return declarations[0];
        return null;
    }

    @NotNull
    private GLSLElement[] findFunctionDeclarations() {
        GLSLFunctionType[] declarations = findFunctionTypes();
        List<GLSLElement> realDeclarations = new ArrayList<GLSLElement>();
        for (GLSLFunctionType declaration : declarations) {
            GLSLElement element = declaration.getDefinition();
            if (element != null) {
                realDeclarations.add(element);
            }
        }
        return realDeclarations.toArray(new GLSLElement[realDeclarations.size()]);
    }

    private final GLSLFunctionType[] NO_FUNCTION_TYPES = new GLSLFunctionType[0];
    @NotNull
    public GLSLFunctionType[] findFunctionTypes() {
        List<GLSLFunctionType> compatibleDeclarations = new ArrayList<GLSLFunctionType>();

        PsiElement current = source.findParentByClass(GLSLFunctionDefinition.class);

        if(current == null) return NO_FUNCTION_TYPES; //For the time being, see below

        //todo: fails on vec3 and such...
        // todo: also fails for function calls in initializers of global variables (can only be vec3 and such there...)
        //assert current != null : "GLSLFunctionDeclaration for '" + getFunctionName() + "' not found.";

        // Is this a constructor for one of the built-in types?
        GLSLType builtInType = GLSLTypes.getTypeFromName(sourceExpression.getFunctionName());
        if (builtInType != null) {
            final GLSLParameterList parameterList = sourceExpression.getParameterList();
            if(parameterList != null){
                return GLSLFunctionType.findApplicableTypes(builtInType.getConstructors(), parameterList.getParameterTypes());
            }else{
                return NO_FUNCTION_TYPES;
            }
        }

        while (current != null) {

            GLSLFunctionType functionType;

            if (current instanceof GLSLFunctionDeclaration) {
                GLSLFunctionDeclaration function = (GLSLFunctionDeclaration) current;
                functionType = function.getType();

                if (sourceExpression.getFunctionName().equals(functionType.getName())) {

                    switch (functionType.getParameterCompatibilityLevel(sourceExpression.getParameterTypes())) {
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
                if(typeSpecifier != null){
                    GLSLType type = typeSpecifier.getType();
                    if (sourceExpression.getFunctionName().equals(type.getTypename())) {
                        for (GLSLFunctionType constructor : type.getConstructors()) {
                            switch (constructor.getParameterCompatibilityLevel(sourceExpression.getParameterTypes())) {
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
            }

            current = current.getPrevSibling();
        }

        return compatibleDeclarations.toArray(new GLSLFunctionType[compatibleDeclarations.size()]);
    }
}
