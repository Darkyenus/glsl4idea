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

package glslplugin.structureview;

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.util.PlatformIcons;
import glslplugin.lang.GLSLFileType;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.declarations.GLSLQualifier;
import glslplugin.lang.elements.types.GLSLQualifiedType;

import javax.swing.*;

class GLSLPresentation implements ItemPresentation {
    public static final Icon FIELD = PlatformIcons.FIELD_ICON;
    public static final Icon PROTOTYPE = PlatformIcons.ABSTRACT_METHOD_ICON;
    public static final Icon FUNCTION = PlatformIcons.METHOD_ICON;
    public static final Icon STRUCT = PlatformIcons.ANNOTATION_TYPE_ICON;

    private final String name;
    private Icon icon = null;

    public GLSLPresentation(String name) {
        this.name = name;
    }

    public String getPresentableText() {
        return name;
    }

    public String getLocationString() {
        return null;
    }

    public Icon getIcon(boolean open) {
        return icon;
    }

    private void setIcon(Icon icon) {
        this.icon = icon;
    }

    public static GLSLPresentation createMethodPresentation(String name, String... parameters) {
        GLSLPresentation presentation = new GLSLPresentation(name + " " + prettyPrint(parameters));
        presentation.setIcon(FUNCTION);
        return presentation;
    }

    public static GLSLPresentation createFilePresentation(String name) {
        GLSLPresentation presentation = new GLSLPresentation(name);
        presentation.setIcon(((LanguageFileType) GLSLFileType.INSTANCE).getIcon());
        return presentation;
    }

    public static GLSLPresentation createStructPresentation(String structName) {
        GLSLPresentation presentation = new GLSLPresentation(structName);
        presentation.setIcon(STRUCT);
        return presentation;
    }

    private static String prettyPrint(String[] names) {
        StringBuilder result = new StringBuilder();
        if (names.length > 0) {
            for (int i = 0; i < names.length - 1; i++) {
                result.append(names[i]).append(", ");
            }
            result.append(names[names.length - 1]);
        }

        return result.toString();
    }

    public static GLSLPresentation createPrototypePresentation(String name) {
        GLSLPresentation presentation = new GLSLPresentation(name);
        presentation.setIcon(PROTOTYPE);
        return presentation;
    }

    public static GLSLPresentation createFieldPresentation(GLSLDeclarator declarator) {
        String result = "";

        GLSLQualifiedType type = declarator.getQualifiedType();
        GLSLQualifier[] qualifiers = type.getQualifiers();

        if (qualifiers.length > 0) {
            result += prettyPrint(stringify(qualifiers, glslQualifier -> glslQualifier == null ? "null" : glslQualifier.getText()));
            result += " ";
        }

        result += declarator.getName();

        result += " : ";
        result += type.getType().getTypename();

        GLSLPresentation presentation = new GLSLPresentation(result);
        presentation.setIcon(FIELD);
        return presentation;

    }

    private static <T> String[] stringify(T[] array, Stringifyer<T> stringifyer) {
        String[] result = new String[array.length];

        for (int i = 0; i < array.length; i++) {
            result[i] = stringifyer.stringify(array[i]);
        }

        return result;
    }

    private interface Stringifyer<T> {
        String stringify(T t);
    }
}
