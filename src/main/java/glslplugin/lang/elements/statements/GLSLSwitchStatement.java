package glslplugin.lang.elements.statements;

import com.intellij.lang.ASTNode;
import glslplugin.lang.elements.expressions.GLSLExpression;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a switch statement, with control expression and compound expression body.
 * <p/>
 * Created by abigail on 08/07/15.
 */
public class GLSLSwitchStatement extends GLSLStatement {

    public GLSLSwitchStatement(@NotNull ASTNode astNode) {
        super(astNode);
    }

    @Nullable
    public GLSLExpression getSwitchCondition() {
        return findChildByClass(GLSLExpression.class);
    }

    @NotNull
    public GLSLStatement[] getSwitchBodyStatements() {
        final GLSLCompoundStatement body = findChildByClass(GLSLCompoundStatement.class);
        if (body != null) return body.getStatements();
        else return GLSLStatement.NO_STATEMENTS;
    }

    @NotNull
    public List<GLSLLabelStatement> getLabelStatements() {
        List<GLSLLabelStatement> result = new ArrayList<>();
        for (GLSLStatement statement : getSwitchBodyStatements()) {
            if (statement instanceof GLSLLabelStatement) {
                result.add((GLSLLabelStatement) statement);
            }
        }
        return result;
    }

    @Nullable
    private static TerminatorScope min(@Nullable TerminatorScope a, @Nullable TerminatorScope b) {
        if (a == null || b == null) {
            return a == null ? b : a;
        }
        return a.ordinal() < b.ordinal() ? a : b;
    }

    @NotNull
    @Override
    public TerminatorScope getTerminatorScope() {
        // Terminator scope of a switch is the minimum terminator scope of all of its cases, including default.
        // Missing default renders the whole switch as having no terminating power
        // (we do not account for somebody enumerating all possible values of int/uint, because that would be crazy)

        TerminatorScope minimalTerminatorScope = null;
        boolean hasDefaultCase = false;
        boolean inScope = false;

        for (GLSLStatement statement : getSwitchBodyStatements()) {
            if (statement instanceof GLSLLabelStatement) {
                // Another label, not very interesting
                inScope = true;
                if (statement instanceof GLSLDefaultStatement) {
                    // Now we know, that we don't have to account for default case,
                    // so the switch can have some breaking power!
                    hasDefaultCase = true;
                }
            }

            if (!inScope) {
                // Bad code
                continue;
            }

            // Looking for the first non-NONE scope breaker
            final TerminatorScope statementTerminatorScope = statement.getTerminatorScope();
            if (statementTerminatorScope == TerminatorScope.LOOP_OR_SWITCH) {
                // This fall-through group is completed with a break so it has no external breaking power.
                return TerminatorScope.NONE;
            }

            if (statementTerminatorScope != TerminatorScope.NONE) {
                minimalTerminatorScope = min(minimalTerminatorScope, statementTerminatorScope);
                inScope = false;
            }
        }

        if (/* Since we don't expect this switch to cover every possible value of int/uint, there are ways for it to
            fall through. Therefore, it does not terminate anything. */
                !hasDefaultCase
                        /* Last scope ended without any breaking statement, so the whole switch has no breaking power. */
                        || inScope
                        /* Switch statement with no real statements inside (bad code) */
                        || minimalTerminatorScope == null) {

            return TerminatorScope.NONE;
        }

        return minimalTerminatorScope;
    }
}
