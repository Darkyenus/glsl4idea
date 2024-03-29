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
import com.intellij.openapi.util.TextRange;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.expressions.GLSLExpression;
import glslplugin.lang.elements.expressions.GLSLFieldSelectionExpression;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLVectorType;
import org.jetbrains.annotations.NotNull;

/**
 * Checks if vector components are correct.
 * - all of the same type (no mixing)
 * - within range (<= number of components)
 */
public class VectorComponentsAnnotation extends Annotator<GLSLFieldSelectionExpression> {
    private static final char[] xyzw = {'x', 'y', 'z', 'w'};
    private static final char[] rgba = {'r', 'g', 'b', 'a'};
    private static final char[] stpq = {'s', 't', 'p', 'q'};

    public void annotate(GLSLFieldSelectionExpression expr, AnnotationHolder holder) {
        GLSLExpression leftHandExpression = expr.getLeftHandExpression();
        if(leftHandExpression == null)return;
        GLSLType leftHandType = leftHandExpression.getType();

        if (leftHandType instanceof GLSLVectorType type) {
            String member = expr.getFieldName();
            if(member == null) return;

            if (!type.hasMember(member) && member.length() > 0) {
                //It has no member like that, why?
                TextRange mitr = expr.getFieldNameRange();

                char[] basePattern = getPattern(member.charAt(0));
                int numComponents = type.getNumComponents();

                if (basePattern == null) {
                    TextRange tr = new TextRange(mitr.getStartOffset(), mitr.getStartOffset() + 1);
                    holder.newAnnotation(HighlightSeverity.ERROR, "'" + member.charAt(0) + "' is not one of: " + getAllAlternatives(numComponents)).range(tr).create();
                } else {

                    for (int i = 0; i < member.length(); i++) {
                        char cm = member.charAt(i);
                        char[] pattern = getPattern(cm);

                        TextRange tr = new TextRange(mitr.getStartOffset() + i, mitr.getStartOffset() + i + 1);

                        if (pattern == null) {
                            holder.newAnnotation(HighlightSeverity.ERROR, "'" + cm + "' is not one of: " + getAlternatives(basePattern, numComponents)).range(tr).create();
                        } else {
                            if (basePattern != pattern) {
                                holder.newAnnotation(HighlightSeverity.ERROR, "Can not mix " + getAlternatives(pattern, numComponents) + " with " + getAlternatives(basePattern, numComponents)).range(tr).create();
                            } else if (!checkRange(pattern, numComponents, cm)) {
                                holder.newAnnotation(HighlightSeverity.ERROR, "'" + cm + "' is out of range of: " + getAlternatives(basePattern, numComponents)).range(tr).create();
                            }
                        }
                    }
                }
            }
        }
    }

    private String getAllAlternatives(int numComponents) {
        return getAlternatives(xyzw, numComponents) + ", " + getAlternatives(rgba, numComponents) + " or " + getAlternatives(stpq, numComponents);
    }

    private String getAlternatives(char[] chars, int numComponents) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < numComponents; i++) {
            result.append(chars[i]);
        }
        return result.toString();
    }

    private boolean checkRange(char[] pattern, int numComponents, char c) {
        for (int i = 0; i < numComponents; i++) {
            if (pattern[i] == c) {
                return true;
            }
        }
        return false;
    }

    private char[] getPattern(char c) {
        for (int i = 0; i < 4; i++) {
            if (xyzw[i] == c) {
                return xyzw;
            } else if (rgba[i] == c) {
                return rgba;
            } else if (stpq[i] == c) {
                return stpq;
            }
        }

        return null;
    }

    @NotNull
    @Override
    public Class<GLSLFieldSelectionExpression> getElementType() {
        return GLSLFieldSelectionExpression.class;
    }
}
