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
import com.intellij.psi.tree.IElementType;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

/**
 * GLSLLiteral is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 28, 2009
 *         Time: 1:50:35 PM
 */
public class GLSLLiteral extends GLSLPrimaryExpression {
    public enum Type {
        BOOL("Bool", GLSLTypes.BOOL),
        FLOAT("Float", GLSLTypes.FLOAT),
        DOUBLE("Double", GLSLTypes.DOUBLE),
        INTEGER("Integer", GLSLTypes.INT),
        UINT("Unsigned integer", GLSLTypes.UINT);


        Type(String name, GLSLType type) {
            this.textRepresentation = name;
            this.type = type;
        }

        final String textRepresentation;
        final GLSLType type;
    }

    public GLSLLiteral(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    public Type getLiteralType() {
        IElementType type = getNode().getFirstChildNode().getElementType();

        Type result = getLiteralType(type);
        if(result != null)return result;

        Logger.getLogger("GLSLLiteral").warning("Unsupported literal type. ("+type+")");
        return null;
    }

    @Nullable
    public static Type getLiteralType(IElementType type){
        if (type == GLSLTokenTypes.BOOL_CONSTANT) return Type.BOOL;
        if (type == GLSLTokenTypes.INTEGER_CONSTANT) return Type.INTEGER;
        if (type == GLSLTokenTypes.UINT_CONSTANT) return Type.UINT;
        if (type == GLSLTokenTypes.FLOAT_CONSTANT) return Type.FLOAT;
        if (type == GLSLTokenTypes.DOUBLE_CONSTANT) return Type.DOUBLE;
        return null;
    }

    @NotNull
    @Override
    public GLSLType getType() {
        Type literalType = getLiteralType();
        if(literalType != null){
            return literalType.type;
        }else{
            return GLSLTypes.UNKNOWN_TYPE;
        }
    }

    public String toString() {
        Type literalType = getLiteralType();
        return (literalType == null ? "(unknown)" : getLiteralType().textRepresentation) + " Literal: '" + getText() + "'";
    }
}
