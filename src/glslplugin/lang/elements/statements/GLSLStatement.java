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

package glslplugin.lang.elements.statements;

import com.intellij.lang.ASTNode;
import glslplugin.lang.elements.GLSLElementImpl;
import org.jetbrains.annotations.NotNull;

/**
 * GLSLStatement is ...
 *
 * @author Yngve Devik Hammersland
 *         Date: Jan 28, 2009
 *         Time: 5:53:03 PM
 */
public abstract class GLSLStatement extends GLSLElementImpl {

    public static final GLSLStatement[] NO_STATEMENTS = new GLSLStatement[0];

    public GLSLStatement(@NotNull ASTNode astNode) {
        super(astNode);
    }

    public enum TerminatorScope {
        NONE, // This statement does not terminate any scope
        LOOP, // A loop terminator terminates the innermost loop (eg. break, continue)
        FUNCTION, // A function terminator terminates the current function (eg. return)
        SHADER, // A shader terminator terminates the entire shader (eg. discard)
    }

    @NotNull
    public TerminatorScope getTerminatorScope() { return TerminatorScope.NONE; }
}
