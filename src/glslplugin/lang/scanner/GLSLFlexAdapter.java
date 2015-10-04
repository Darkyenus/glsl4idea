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

package glslplugin.lang.scanner;

import com.intellij.lexer.LexerBase;
import com.intellij.lexer.LexerPosition;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * based on com.intellij.lexer.FlexAdapter
 */
public final class GLSLFlexAdapter extends LexerBase {

    public GLSLFlexAdapter() {
        myFlex = new GLSLFlexLexer(null);
    }

    private GLSLFlexLexer myFlex = null;
    private IElementType myTokenType = null;
    private CharSequence myText;

    private int myEnd;
    private int myState;

    @Override
    public synchronized void start(@NotNull final CharSequence buffer, int startOffset, int endOffset, final int initialState) {
        myText = buffer;
        myEnd = endOffset;
        myFlex.reset(myText, startOffset, endOffset, initialState);
        myTokenType = null;
    }

    @Override
    public synchronized int getState() {
        if (myTokenType == null) locateToken();
        return myState;
    }

    @Override
    public synchronized IElementType getTokenType() {
        if (myTokenType == null) locateToken();
        return myTokenType;
    }

    @Override
    public synchronized int getTokenStart() {
        if (myTokenType == null) locateToken();
        return myFlex.getTokenStart();
    }

    @Override
    public synchronized int getTokenEnd() {
        if (myTokenType == null) locateToken();
        return myFlex.getTokenEnd();
    }

    @Override
    public synchronized void advance() {
        if (myTokenType == null) locateToken();
        myTokenType = null;
    }

    @NotNull
    @Override
    public synchronized CharSequence getBufferSequence() {
        return myText;
    }

    @Override
    public synchronized int getBufferEnd() {
        return myEnd;
    }

    private void locateToken() {
        if (myTokenType != null) return;
        try {
            myState = myFlex.yystate();
            myTokenType = myFlex.advance();
        }
        catch (IOException e) { /*Can't happen*/ }
        catch (Error e) {
            // add lexer class name to the error
            final Error error = new Error(myFlex.getClass().getName() + ": " + e.getMessage());
            error.setStackTrace(e.getStackTrace());
            throw error;
        }
    }

    @NotNull
    @Override
    public synchronized LexerPosition getCurrentPosition() {
        return super.getCurrentPosition();
    }

    @Override
    public synchronized void restore(@NotNull LexerPosition position) {
        super.restore(position);
    }
}
