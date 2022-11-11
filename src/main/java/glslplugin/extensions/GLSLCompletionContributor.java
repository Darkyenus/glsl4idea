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

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.completion.DefaultCompletionContributor;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;
import com.intellij.patterns.ElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.declarations.GLSLFunctionDeclaration;
import glslplugin.lang.elements.declarations.GLSLParameterDeclaration;
import glslplugin.lang.elements.declarations.GLSLStructDefinition;
import glslplugin.lang.elements.expressions.GLSLExpression;
import glslplugin.lang.elements.expressions.GLSLFieldSelectionExpression;
import glslplugin.lang.elements.preprocessor.GLSLDefineDirective;
import glslplugin.lang.elements.reference.GLSLBuiltInPsiUtilService;
import glslplugin.lang.elements.statements.GLSLCompoundStatement;
import glslplugin.lang.elements.types.GLSLArrayType;
import glslplugin.lang.elements.types.GLSLMatrixType;
import glslplugin.lang.elements.types.GLSLStructType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLVectorType;
import glslplugin.util.TreeIterator;
import glslplugin.util.VectorComponents;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * Provides more advanced contextual autocompletion
 *
 * @author Wyozi
 * @author Jan Polák
 */
public class GLSLCompletionContributor extends DefaultCompletionContributor {
    private static final ElementPattern<PsiElement> FIELD_SELECTION = psiElement(GLSLTokenTypes.IDENTIFIER).withParent(GLSLFieldSelectionExpression.class);

    private static final ElementPattern<PsiElement> GENERIC_REFERENCE = psiElement(GLSLTokenTypes.IDENTIFIER);

    public GLSLCompletionContributor() {
        // Add field selection completion
        extend(CompletionType.BASIC, FIELD_SELECTION, new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters completionParameters, @NotNull ProcessingContext processingContext, @NotNull CompletionResultSet completionResultSet) {
                final PsiElement position = completionParameters.getPosition();
                GLSLFieldSelectionExpression fieldSelection = (GLSLFieldSelectionExpression) position.getParent();

                GLSLExpression leftHandExpression = fieldSelection.getLeftHandExpression();
                if (leftHandExpression == null) return;

                // Struct member completion
                GLSLType type = leftHandExpression.getType();

                if (type instanceof GLSLVectorType vec) {
                    completionResultSet.addElement(LookupElementBuilder.create("length()").withTypeText("int"));

                    final int textStart = position.getTextOffset();
                    final int textEnd = completionParameters.getOffset();
                    if (textStart < textEnd) {
                        String prefix = position.getText().substring(0, textEnd - textStart);
                        if (prefix.length() > 0 && prefix.length() < 4) {
                            final String newType = GLSLVectorType.getType(vec.getBaseType(), prefix.length() + 1).getTypename();

                            final char firstComponent = prefix.charAt(0);
                            for (String set : VectorComponents.SETS) {
                                if (set.indexOf(firstComponent) == -1) {
                                    continue;
                                }
                                for (int i = 0; i < Math.min(vec.getNumComponents(), set.length()); i++) {
                                    completionResultSet.addElement(LookupElementBuilder.create(prefix + set.charAt(i)).withTypeText(newType));
                                }
                                break;
                            }
                        }

                    } else {
                        String baseType = vec.getBaseType().getTypename();
                        for (String set : VectorComponents.SETS) {
                            for (int i = 0; i < Math.min(vec.getNumComponents(), set.length()); i++) {
                                completionResultSet.addElement(LookupElementBuilder.create(set.charAt(i)).withTypeText(baseType));
                            }
                        }
                    }
                } else if (type instanceof GLSLMatrixType || type instanceof GLSLArrayType) {
                    completionResultSet.addElement(LookupElementBuilder.create("length()").withTypeText("int"));
                } else if (type instanceof GLSLStructType struct) {
                    for (Map.Entry<String, GLSLType> entry : struct.getMembers().entrySet()) {
                        completionResultSet.addElement(LookupElementBuilder.create(entry.getKey()).withTypeText(entry.getValue().getTypename()));
                    }
                }
            }
        });

        // This can be anything, types, tokens...
        extend(CompletionType.BASIC, GENERIC_REFERENCE, new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
                final PsiElement element = parameters.getOriginalPosition();
                if (element == null) return;

                final PsiElement prevLeaf = PsiTreeUtil.prevLeaf(parameters.getPosition());
                if (prevLeaf != null) {
                    final IElementType prevToken = prevLeaf.getNode().getElementType();
                    if (prevToken == GLSLTokenTypes.DOT) {
                        // Field selection handles this
                        return;
                    }
                    if (GLSLTokenTypes.CONSTANT_TOKENS.contains(prevToken)) {
                        // No complete right after a number, it is confusing and annoying
                        return;
                    }
                }

                // Walk declarations
                boolean includeFunctions = PsiTreeUtil.getParentOfType(element, GLSLCompoundStatement.class) != null;
                final DeclarationWalk walk = new DeclarationWalk(result, includeFunctions);
                PsiTreeUtil.treeWalkUp(walk, element, null, ResolveState.initial());

                // Walk preprocessor tokens
                {
                    GLSLDefineDirective prev = TreeIterator.previous(element, GLSLDefineDirective.class);
                    while (prev != null) {
                        System.out.println("Got one: "+prev);
                        result.addElement(LookupElementBuilder.create(prev));
                        prev = TreeIterator.previous(prev, GLSLDefineDirective.class);
                    }
                }

                // Add all built-ins
                final GLSLBuiltInPsiUtilService bipus = element.getProject().getService(GLSLBuiltInPsiUtilService.class);
                result.addAllElements(bipus.builtinTypeLookup);
            }
        });
    }

    /** Combination of all reference walks into one, for efficiency. */
    private static final class DeclarationWalk implements PsiScopeProcessor {

        public final @NotNull CompletionResultSet result;
        public final boolean includeFunctions;

        private DeclarationWalk(@NotNull CompletionResultSet result, boolean includeFunctions) {
            this.result = result;
            this.includeFunctions = includeFunctions;
        }

        private final HashMap<String, ArrayList<GLSLFunctionDeclaration>> encounteredFunctions = new HashMap<>();

        @Override
        public boolean execute(@NotNull PsiElement element, @NotNull ResolveState state) {
            if (element instanceof final GLSLDeclarator declarator) {
                result.addElement(LookupElementBuilder.create(declarator).withTypeText(declarator.getType().getTypename()));
            } else if (element instanceof GLSLStructDefinition def) {
                result.addElement(LookupElementBuilder.create(def).withTypeText("struct"));
            } else if (includeFunctions && element instanceof GLSLFunctionDeclaration dec) {
                final String funcName = dec.getFunctionName();
                ArrayList<GLSLFunctionDeclaration> all = encounteredFunctions.get(funcName);
                if (all == null) {
                    all = new ArrayList<>();
                    result.addElement(LookupElementBuilder.create(dec).withExpensiveRenderer(new LookupElementRenderer<>() {
                        @Override
                        public void renderElement(LookupElement element, LookupElementPresentation presentation) {
                            presentation.setItemText(dec.getFunctionName());
                            presentation.appendTailText("(", true);
                            boolean first = true;
                            for (GLSLParameterDeclaration parameter : dec.getParameters()) {
                                if (!first) {
                                    presentation.appendTailText(", ", true);
                                } else first = false;

                                presentation.appendTailText(parameter.getTypeSpecifierNodeTypeName(), false);
                            }
                            presentation.appendTailText(")", true);
                            presentation.setTypeText(dec.getReturnType().getTypename());
                        }
                    }));
                    all.add(dec);
                    encounteredFunctions.put(funcName, all);
                } else {
                    all.add(dec);
                }
            }

            return true;
        }
    }
}
