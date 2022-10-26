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

package glslplugin.annotation.impl;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.TextRange;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.declarations.GLSLFunctionDefinition;
import glslplugin.lang.elements.expressions.GLSLCondition;
import glslplugin.lang.elements.expressions.GLSLExpression;
import glslplugin.lang.elements.statements.GLSLBreakStatement;
import glslplugin.lang.elements.statements.GLSLCompoundStatement;
import glslplugin.lang.elements.statements.GLSLContinueStatement;
import glslplugin.lang.elements.statements.GLSLDefaultStatement;
import glslplugin.lang.elements.statements.GLSLDiscardStatement;
import glslplugin.lang.elements.statements.GLSLIfStatement;
import glslplugin.lang.elements.statements.GLSLLabelStatement;
import glslplugin.lang.elements.statements.GLSLReturnStatement;
import glslplugin.lang.elements.statements.GLSLStatement;
import glslplugin.lang.elements.statements.GLSLSwitchStatement;
import glslplugin.lang.elements.statements.LoopStatement;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;

/**
 * Checks that all statements are reachable and verifies that the function always returns a value.
 */
public class ReachabilityAnnotation extends Annotator<GLSLFunctionDefinition> {
    private final TextAttributesKey unreachableAttributes;

    public ReachabilityAnnotation() {
        unreachableAttributes = TextAttributesKey.createTextAttributesKey("GLSL.UNREACHABLE", CodeInsightColors.NOT_USED_ELEMENT_ATTRIBUTES);
    }

    public void annotate(GLSLFunctionDefinition function, AnnotationHolder holder) {
        final GLSLCompoundStatement body = function.getBody();
        if (body == null) {
            return;
        }

        final Terminate bodyTerminate = annotateStatement(body, holder);

        if (function.getReturnType() == GLSLTypes.VOID
                || bodyTerminate == Terminate.RETURN
                || bodyTerminate == Terminate.DISCARD) return;

        var rightBrace = body.findLastChildByType(GLSLTokenTypes.RIGHT_BRACE);
        if (rightBrace != null) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Missing return statement")
                    .range(rightBrace).create();
        }
    }

    private void markUnreachable(@NotNull GLSLStatement s, @NotNull AnnotationHolder holder) {
        final TextRange textRange = s.getTextRange();
        if (!textRange.isEmpty()) {
            holder.newAnnotation(HighlightSeverity.WARNING, "Unreachable statement").range(textRange).textAttributes(unreachableAttributes).create();
        }
    }

    private Terminate annotateStatement(@NotNull GLSLStatement statement, @NotNull AnnotationHolder holder) {
        if (statement instanceof GLSLCompoundStatement compound) {
            Terminate terminator = Terminate.NOTHING;
            for (GLSLStatement cs : compound.getStatements()) {
                if (terminator != Terminate.NOTHING) {
                    markUnreachable(cs, holder);
                } else {
                    terminator = annotateStatement(cs, holder);
                }
            }
            return terminator;
        } else if (statement instanceof LoopStatement loop) {
            final GLSLStatement body = loop.getBody();
            if (body == null) return Terminate.NOTHING;
            final Terminate bodyTerminate = annotateStatement(body, holder);
            switch (bodyTerminate) {
                case DISCARD, RETURN -> {
                    return bodyTerminate;
                }
                case BREAK -> {
                    // Just breaks the loop
                    return Terminate.NOTHING;
                }
                case CONTINUE, NOTHING -> {
                    // If the condition is constant true, this will block forever
                    final GLSLCondition condition = loop.getCondition();
                    final GLSLExpression expr = condition == null ? null : condition.getConditionExpression();
                    if (expr != null && expr.isConstantValue() && Boolean.TRUE.equals(expr.getConstantValue())) {
                        return Terminate.DISCARD;
                    }
                    return Terminate.NOTHING;
                }
            }
            return Terminate.NOTHING;
        } else if (statement instanceof GLSLIfStatement ifStat) {
            final GLSLStatement trueBranch = ifStat.getTrueBranch();
            final GLSLStatement falseBranch = ifStat.getFalseBranch();

            final GLSLCondition condition = ifStat.getCondition();
            final GLSLExpression expr = condition == null ? null : condition.getConditionExpression();
            if (expr != null && expr.isConstantValue()) {
                final Object constantValue = expr.getConstantValue();
                if (constantValue instanceof Boolean bool) {
                    holder.newAnnotation(HighlightSeverity.WEAK_WARNING, "Condition is always "+bool).range(condition).create();
                }
            }

            final Terminate trueTerminate = trueBranch == null ? null : annotateStatement(trueBranch, holder);
            final Terminate falseTerminate = falseBranch == null ? null : annotateStatement(falseBranch, holder);
            if (trueTerminate == null || falseTerminate == null) {
                return Terminate.NOTHING;
            }
            return max(trueTerminate, falseTerminate);
        } else if (statement instanceof GLSLSwitchStatement switchStat) {
            Terminate minimalTerminatorScope = Terminate.DISCARD;
            boolean hasDefaultCase = false;
            Terminate prongTerminate = null;

            for (GLSLStatement ss : switchStat.getSwitchBodyStatements()) {
                if (ss instanceof GLSLLabelStatement) {
                    // Another label, not very interesting
                    if (prongTerminate != null) {
                        minimalTerminatorScope = max(minimalTerminatorScope, prongTerminate);
                    }
                    prongTerminate = Terminate.NOTHING;
                    if (ss instanceof GLSLDefaultStatement) {
                        // Now we know, that we don't have to account for default case,
                        // so the switch can have some breaking power!
                        hasDefaultCase = true;
                    }
                    continue;
                }

                if (prongTerminate == null) {
                    // Bad code before first label
                    continue;
                }

                if (prongTerminate != Terminate.NOTHING) {
                    markUnreachable(ss, holder);
                } else {
                    prongTerminate = annotateStatement(ss, holder);
                }
            }
            if (prongTerminate != null) {
                minimalTerminatorScope = max(minimalTerminatorScope, prongTerminate);
            }

            if (hasDefaultCase) {
                // Since all cases are covered, the switch has terminating power (if all paths terminate)
                if (minimalTerminatorScope == Terminate.BREAK) {
                    // Break just breaks the switch, nothing outside
                    minimalTerminatorScope = Terminate.NOTHING;
                }
                return minimalTerminatorScope;
            } else {
                // Since we don't expect the switch to cover every possible value of anything (no enum in GLSL),
                // there are ways for it to fall through. Therefore, it does not terminate anything.
                return Terminate.NOTHING;
            }
        } else if (statement instanceof GLSLBreakStatement) {
            return Terminate.BREAK;
        } else if (statement instanceof GLSLContinueStatement) {
            return Terminate.CONTINUE;
        } else if (statement instanceof GLSLDiscardStatement) {
            return Terminate.DISCARD;
        } else if (statement instanceof GLSLReturnStatement) {
            return Terminate.RETURN;
        } else {
            return Terminate.NOTHING;
        }
    }

    private enum Terminate {
        DISCARD,
        RETURN,
        CONTINUE,
        BREAK,// Must be below continue for switch to work
        NOTHING
    }

    private static Terminate max(Terminate a, Terminate b) {
        return a.ordinal() > b.ordinal() ? a : b;
    }

    @NotNull
    @Override
    public Class<GLSLFunctionDefinition> getElementType() {
        return GLSLFunctionDefinition.class;
    }
}
