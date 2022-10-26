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

package glslplugin.formatter;

import com.intellij.formatting.Alignment;
import com.intellij.formatting.Block;
import com.intellij.formatting.Indent;
import com.intellij.formatting.Spacing;
import com.intellij.formatting.SpacingBuilder;
import com.intellij.formatting.Wrap;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.declarations.GLSLStructDefinition;
import glslplugin.lang.elements.declarations.GLSLStructMemberDeclaration;
import glslplugin.lang.elements.statements.GLSLCompoundStatement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wyozi
 */
public class GLSLFormattingBlock extends AbstractBlock {
    private final SpacingBuilder spacingBuilder;
    private final Indent indent;

    protected GLSLFormattingBlock(@NotNull ASTNode astNode, @Nullable Wrap wrap, @Nullable Alignment alignment, SpacingBuilder spacingBuilder) {
        super(astNode, wrap, alignment);
        this.spacingBuilder = spacingBuilder;
        this.indent = getNodeIndent(astNode);
    }

    private static boolean isIndentableBlock(PsiElement psiElement) {
        return psiElement instanceof GLSLCompoundStatement || psiElement instanceof GLSLStructMemberDeclaration;
    }

    private static Indent getNodeIndent(ASTNode node) {
        IElementType nodeType = node.getElementType();
        ASTNode parent = node.getTreeParent();
        PsiElement parentPsi = parent != null ? parent.getPsi() : null;

        if (isIndentableBlock(parentPsi)) {
            if (nodeType == GLSLTokenTypes.LEFT_BRACE) {
                return Indent.getContinuationWithoutFirstIndent();
            }
            if (nodeType == GLSLTokenTypes.RIGHT_BRACE) {
                return Indent.getNoneIndent();
            }
            return Indent.getNormalIndent();
        }

        return Indent.getNoneIndent();
    }

    @Override
    protected List<Block> buildChildren() {
        List<Block> blocks = new ArrayList<>();
        ASTNode child = myNode.getFirstChildNode();

        while (child != null) {
            if (canBeCorrectBlock(child)) {
                blocks.add(new GLSLFormattingBlock(child, null, null, spacingBuilder));
            }
            child = child.getTreeNext();
        }
        return blocks;
    }

    @Nullable
    @Override
    protected Indent getChildIndent() {
        PsiElement psiElement = myNode.getPsi();
        // Note: this cannot use isIndentableBlock because when pressing enter after a right brace, the psiElement in question is
        //       a GLSLTypeDefinition instead of a GLSLStructDeclaration
        return (psiElement instanceof GLSLCompoundStatement || psiElement instanceof GLSLStructDefinition) ? Indent.getNormalIndent() : Indent.getNoneIndent();
    }

    private boolean canBeCorrectBlock(ASTNode child) {
        return child.getText().trim().length() > 0;
    }

    @Override
    public Indent getIndent() {
        return indent;
    }

    @Nullable
    @Override
    public Spacing getSpacing(@Nullable Block block, @NotNull Block block1) {
        return spacingBuilder.getSpacing(this, block, block1);
    }

    @Override
    public boolean isLeaf() {
        return myNode.getFirstChildNode() == null;
    }
}
