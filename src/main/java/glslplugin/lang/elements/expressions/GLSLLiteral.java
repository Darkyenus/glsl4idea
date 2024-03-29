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
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.types.GLSLScalarType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
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
        BOOL("Bool", GLSLScalarType.BOOL),
        FLOAT("Float", GLSLScalarType.FLOAT),
        DOUBLE("Double", GLSLScalarType.DOUBLE),
        INT("Integer", GLSLScalarType.INT),
        UINT("Unsigned integer", GLSLScalarType.UINT),
        INT64("64-bit Integer", GLSLScalarType.INT),
        UINT64("Unsigned 64-bit integer", GLSLScalarType.UINT),
        // https://github.com/KhronosGroup/GLSL/blob/master/extensions/ext/GLSL_EXT_debug_printf.txt
        // does not define any GLSL type, just the literal.
        STRING("String", null),
        ;


        Type(String name, @Nullable GLSLType type) {
            this.textRepresentation = name;
            this.type = type;
        }

        final String textRepresentation;
        @Nullable
        final GLSLType type;
    }

    public GLSLLiteral(@NotNull ASTNode astNode) {
        super(astNode);
    }

    private @Nullable ASTNode valueNode() {
        return getNode().getFirstChildNode();
    }

    @Nullable
    public Type getLiteralType() {
        final ASTNode node = valueNode();
        if (node == null) return null;

        IElementType type = node.getElementType();

        Type result = getLiteralType(type);
        if(result != null) return result;

        Logger.getLogger("GLSLLiteral").warning("Unsupported literal type. ("+type+")");
        return null;
    }

    @Nullable
    public static Type getLiteralType(IElementType type){
        if (type == GLSLTokenTypes.BOOL_CONSTANT) return Type.BOOL;
        if (type == GLSLTokenTypes.INTEGER_CONSTANT) return Type.INT;
        if (type == GLSLTokenTypes.UINT_CONSTANT) return Type.UINT;
        if (type == GLSLTokenTypes.INT64_CONSTANT) return Type.INT64;
        if (type == GLSLTokenTypes.UINT64_CONSTANT) return Type.UINT64;
        if (type == GLSLTokenTypes.FLOAT_CONSTANT) return Type.FLOAT;
        if (type == GLSLTokenTypes.DOUBLE_CONSTANT) return Type.DOUBLE;
        if (type == GLSLTokenTypes.STRING_CONSTANT) return Type.STRING;
        return null;
    }

    @NotNull
    @Override
    public GLSLType getType() {
        Type literalType = getLiteralType();
        if (literalType != null && literalType.type != null) {
            return literalType.type;
        } else {
            return GLSLTypes.UNKNOWN_TYPE;
        }
    }

    @NotNull
    public String getLiteralValue(){
        return Objects.requireNonNullElse(GLSLElement.nodeText(valueNode()), "");
    }

    @Override
    public boolean isConstantValue() {
        return getConstantValue() != null;
    }

    @Nullable
    @Override
    public Object getConstantValue() {
        final Type literalType = getLiteralType();
        final String text = getLiteralValue();
        if(literalType == null)return null;
        switch (literalType){
            case BOOL:
                if("true".equals(text))return true;
                else if("false".equals(text))return false;
                else return null;
            case INT:
            case UINT:
            case INT64:
            case UINT64:
                try{
                    return Long.parseLong(text);
                }catch (NumberFormatException nfe){
                    return null;
                }
            case FLOAT:
            case DOUBLE:
                try{
                    return Double.parseDouble(text);
                }catch (NumberFormatException nfe){
                    return null;
                }
            default:
                return null;
        }
    }

    public String toString() {
        Type literalType = getLiteralType();
        return (literalType == null ? "(unknown)" : getLiteralType().textRepresentation) + " Literal: '" + getLiteralValue() + "'";
    }
}
