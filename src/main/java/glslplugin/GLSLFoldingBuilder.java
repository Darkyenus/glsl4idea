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

package glslplugin;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilder;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import glslplugin.lang.elements.GLSLElementTypes;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.util.TreeIterator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static java.util.Collections.emptySet;

public class GLSLFoldingBuilder implements FoldingBuilder {

    @NotNull
    public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull ASTNode node, @NotNull Document document) {
        List<FoldingDescriptor> descriptors = new ArrayList<>();
        appendDescriptors(node, descriptors);
        appendPreprocessorFolding(node, descriptors);
        return descriptors.toArray(FoldingDescriptor.EMPTY);
    }

    private void appendPreprocessorFolding(final ASTNode node, final List<FoldingDescriptor> descriptors) {
        Stack<ASTNode> nestingStack = new Stack<>();
        ASTNode n = node;
        while (n != null) {
            final IElementType elementType = n.getElementType();
            if (PREPROCESSOR_IF_END.contains(elementType) && !nestingStack.isEmpty()) {
                final ASTNode owner = nestingStack.pop();
                ASTNode preprocessorEnd = owner;
                do {
                    preprocessorEnd = preprocessorEnd.getTreeNext();
                } while (preprocessorEnd != null && preprocessorEnd.getElementType() != GLSLTokenTypes.PREPROCESSOR_END);

                ASTNode preprocessorBegin = n;
                do {
                    preprocessorBegin = preprocessorBegin.getTreePrev();
                } while (preprocessorBegin != null && preprocessorBegin.getElementType() != GLSLTokenTypes.PREPROCESSOR_BEGIN);

                if (preprocessorEnd != null && preprocessorBegin != null) {
                    final int startOffset = preprocessorEnd.getStartOffset();
                    final int endOffset = preprocessorBegin.getStartOffset();

                    // Empty folding regions are pointless and trigger assert
                    if (endOffset - startOffset > 0) {
                        descriptors.add(new FoldingDescriptor(owner, new TextRange(startOffset, endOffset), null, "...", false, emptySet()));
                    }
                }
            }

            if (PREPROCESSOR_IF_BEGIN.contains(elementType)) {
                nestingStack.push(n);
            }

            // Walk only leafs, no composite nodes
            n = TreeIterator.nextLeaf(n);
        }
    }

    private void appendDescriptors(final ASTNode node, final List<FoldingDescriptor> descriptors) {
        IElementType type = node.getElementType();

        final TextRange textRange = node.getTextRange();
        //Don't add folding to 0-length nodes, crashes in new FoldingDescriptor
        if(textRange.getLength() <= 0)return;

        if (type == GLSLTokenTypes.COMMENT_BLOCK || type == GLSLElementTypes.COMPOUND_STATEMENT) {
            descriptors.add(new FoldingDescriptor(node, textRange));
        }

        ASTNode child = node.getFirstChildNode();
        while (child != null) {
            appendDescriptors(child, descriptors);
            child = child.getTreeNext();
        }
    }

    public String getPlaceholderText(@NotNull ASTNode node) {
        if (node.getElementType() == GLSLTokenTypes.COMMENT_BLOCK) {
            return "/*...*/";
        }
        if (node.getElementType() == GLSLElementTypes.COMPOUND_STATEMENT) {
            return "{...}";
        }
        return null;
    }

    public boolean isCollapsedByDefault(@NotNull ASTNode astNode) {
        return false;
    }

    /** Starts preprocessor IF block */
    public static final TokenSet PREPROCESSOR_IF_BEGIN = TokenSet.create(
            GLSLTokenTypes.PREPROCESSOR_IF,
            GLSLTokenTypes.PREPROCESSOR_IFDEF,
            GLSLTokenTypes.PREPROCESSOR_IFNDEF,
            GLSLTokenTypes.PREPROCESSOR_ELIF,
            GLSLTokenTypes.PREPROCESSOR_ELSE
    );

    /** Ends preprocessor IF block */
    public static final TokenSet PREPROCESSOR_IF_END = TokenSet.create(
            GLSLTokenTypes.PREPROCESSOR_ELSE,
            GLSLTokenTypes.PREPROCESSOR_ELIF,
            GLSLTokenTypes.PREPROCESSOR_ENDIF
    );

}
