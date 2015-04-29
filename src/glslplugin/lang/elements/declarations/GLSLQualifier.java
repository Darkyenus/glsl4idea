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

package glslplugin.lang.elements.declarations;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import glslplugin.lang.elements.GLSLElementImpl;
import glslplugin.lang.elements.GLSLElementTypes;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.types.GLSLTypeQualifier;
import org.jetbrains.annotations.NotNull;

/**
 * GLSLQualifier is all kinds of qualifiers combined into a single class.
 * <p/>
 * The qualifiers are associated with a qualifier type which are ordered by an integer value.
 * Qualifiers needs to appear in increasing order. Some storage qualifiers may be combined,
 * <code>varying centroid</code> in particular.
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 2, 2009
 *         Time: 9:34:12 AM
 */
public class GLSLQualifier extends GLSLElementImpl {

    @SuppressWarnings("unused") //It is used, as .values()
    public enum Qualifier {
        // Storage Qualifiers
        CONST(GLSLTypeQualifier.STORAGE_QUALIFIER, "const", GLSLTokenTypes.CONST_KEYWORD),
        ATTRIBUTE(GLSLTypeQualifier.STORAGE_QUALIFIER, "attribute", GLSLTokenTypes.ATTRIBUTE_KEYWORD),
        UNIFORM(GLSLTypeQualifier.STORAGE_QUALIFIER, "uniform", GLSLTokenTypes.UNIFORM_KEYWORD),
        VARYING(GLSLTypeQualifier.STORAGE_QUALIFIER, "varying", GLSLTokenTypes.VARYING_KEYWORD),
        CENTROID(GLSLTypeQualifier.STORAGE_QUALIFIER, "centroid", GLSLTokenTypes.CENTROID_KEYWORD),
        PATCH(GLSLTypeQualifier.STORAGE_QUALIFIER, "patch", GLSLTokenTypes.PATCH_KEYWORD),
        SAMPLE(GLSLTypeQualifier.STORAGE_QUALIFIER, "sample", GLSLTokenTypes.SAMPLE_KEYWORD),
        BUFFER(GLSLTypeQualifier.STORAGE_QUALIFIER, "buffer", GLSLTokenTypes.BUFFER_KEYWORD),
        SHARED(GLSLTypeQualifier.STORAGE_QUALIFIER, "shared", GLSLTokenTypes.SHARED_KEYWORD),

        // Memory Qualifiers
        COHERENT(GLSLTypeQualifier.MEMORY_QUALIFIER,"coherent",GLSLTokenTypes.COHERENT_KEYWORD),
        VOLATILE(GLSLTypeQualifier.MEMORY_QUALIFIER,"volatile",GLSLTokenTypes.VOLATILE_KEYWORD),
        RESTRICT(GLSLTypeQualifier.MEMORY_QUALIFIER,"restrict",GLSLTokenTypes.RESTRICT_KEYWORD),
        READONLY(GLSLTypeQualifier.MEMORY_QUALIFIER,"readonly",GLSLTokenTypes.READONLY_KEYWORD),
        WRITEONLY(GLSLTypeQualifier.MEMORY_QUALIFIER,"writeonly",GLSLTokenTypes.WRITEONLY_KEYWORD),

        // Invariant Qualifier
        INVARIANT(GLSLTypeQualifier.INVARIANT_QUALIFIER, "invariant", GLSLTokenTypes.INVARIANT_KEYWORD),

        // Subroutine Qualifier
        SUBROUTINE(GLSLTypeQualifier.SUBROUTINE_QUALIFIER, "subroutine", GLSLTokenTypes.SUBROUTINE_KEYWORD),

        // Precise Qualifier
        PRECISE(GLSLTypeQualifier.PRECISE_QUALIFIER, "precise", GLSLTokenTypes.PRECISE_KEYWORD),

        // ES Storage Qualifiers
        PRECISION(GLSLTypeQualifier.ES_STORAGE_QUALIFIER,"lowp|mediump|highp",GLSLTokenTypes.PRECISION_KEYWORD),

        // Parameter Modifiers
        IN(GLSLTypeQualifier.PARAMETER_MODIFIER , "in", GLSLTokenTypes.IN_KEYWORD),
        OUT(GLSLTypeQualifier.PARAMETER_MODIFIER, "out", GLSLTokenTypes.OUT_KEYWORD),
        INOUT(GLSLTypeQualifier.PARAMETER_MODIFIER, "inout", GLSLTokenTypes.INOUT_KEYWORD),

        // Interpolation modifiers
        SMOOTH(GLSLTypeQualifier.INTERPOLATION_MODIFIER, "smooth", GLSLTokenTypes.SMOOTH_KEYWORD),
        FLAT(GLSLTypeQualifier.INTERPOLATION_MODIFIER, "flat", GLSLTokenTypes.FLAT_KEYWORD),
        NOPERSPECTIVE(GLSLTypeQualifier.INTERPOLATION_MODIFIER, "noperspective", GLSLTokenTypes.NOPERSPECTIVE_KEYWORD),
        LAYOUT(GLSLTypeQualifier.LAYOUT_QUALIFIER, "layout", GLSLTokenTypes.LAYOUT_KEYWORD),
        ;

        private final GLSLTypeQualifier type;
        private final String textRepresentation;
        private final IElementType correspondingElement;

        Qualifier(GLSLTypeQualifier type, String textRepresentation, IElementType correspondingElement) {
            this.type = type;
            this.textRepresentation = textRepresentation;
            this.correspondingElement = correspondingElement;
        }

        GLSLTypeQualifier getType() {
            return type;
        }

        IElementType getCorrespondingElement() {
            return correspondingElement;
        }

        @Override
        public String toString() {
            return textRepresentation;
        }
    }


    public Qualifier getQualifier() {
        PsiElement qualifier = getFirstChild();
        assert qualifier != null;
        ASTNode qualifierNode = qualifier.getNode();
        assert qualifierNode != null;
        return getQualifierFromType(qualifierNode.getElementType());
    }

    public GLSLTypeQualifier getQualifierType() {
        return getQualifier().getType();
    }

    private Qualifier getQualifierFromType(IElementType elt) {
        for (Qualifier qualifier : Qualifier.values()) {
            if (qualifier.getCorrespondingElement() == elt) {
                return qualifier;
            }
        }
        throw new RuntimeException("Unsupported element type: " + elt);
    }

    public GLSLQualifier(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Override
    public String toString() {
        return "Qualifier: " + getQualifier();
    }
}
