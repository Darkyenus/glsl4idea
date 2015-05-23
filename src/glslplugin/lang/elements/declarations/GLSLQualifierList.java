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
import glslplugin.lang.elements.GLSLElementImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * NewQualifierList is simply a list of qualifiers.
 * It implements <code>Iterable&lt;NewQualifier&gt;</code> for easy iteration,
 * as well as <code>getQualifiers() : NewQualifier[]</code> for easy access.
 *
 * @author Yngve Devik Hammersland
 *         Date: Feb 2, 2009
 *         Time: 9:58:52 AM
 */
public class GLSLQualifierList extends GLSLElementImpl implements Iterable<GLSLQualifier> {
    public GLSLQualifierList(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @NotNull
    public GLSLQualifier[] getQualifiers() {
        return findChildrenByClass(GLSLQualifier.class);
    }

    public Iterator<GLSLQualifier> iterator() {
        return java.util.Arrays.asList(getQualifiers()).iterator();
    }

    @Override
    public String toString() {
        return "Qualifier List: (" + getQualifiers().length + " qualifiers)";
    }

    public boolean containsQualifier(GLSLQualifier.Qualifier qualifier) {
        for (GLSLQualifier qualifierElement : getQualifiers()) {
            if (qualifierElement.getQualifier() == qualifier) {
                return true;
            }
        }
        return false;
    }
}
