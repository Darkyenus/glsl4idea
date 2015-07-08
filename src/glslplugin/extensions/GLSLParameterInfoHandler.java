package glslplugin.extensions;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.parameterInfo.*;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReference;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.declarations.GLSLFunctionDeclaration;
import glslplugin.lang.elements.declarations.GLSLFunctionDefinitionImpl;
import glslplugin.lang.elements.declarations.GLSLParameterDeclaration;
import glslplugin.lang.elements.expressions.GLSLExpression;
import glslplugin.lang.elements.expressions.GLSLFunctionCallExpression;
import glslplugin.lang.elements.statements.GLSLCompoundStatement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by abigail on 08/07/15.
 */
public class GLSLParameterInfoHandler implements ParameterInfoHandler<GLSLFunctionCallExpression, Object> {

    private static final Object[] EMPTY_ARRAY = new Object[0];

    @Override
    public boolean couldShowInLookup() {
        return true;
    }

    @Nullable
    @Override
    public Object[] getParametersForLookup(LookupElement item, ParameterInfoContext context) {
        return EMPTY_ARRAY;
    }

    @Nullable
    @Override
    public Object[] getParametersForDocumentation(Object p, ParameterInfoContext context) {
        return EMPTY_ARRAY;
    }

    @Nullable
    @Override
    public GLSLFunctionCallExpression findElementForParameterInfo(@NotNull CreateParameterInfoContext context) {
        GLSLFunctionCallExpression call =
                ParameterInfoUtils.findParentOfTypeWithStopElements(context.getFile(), context.getOffset(),
                        GLSLFunctionCallExpression.class, GLSLCompoundStatement.class);
        if (call == null) return null;

        PsiReference reference = call.getReferenceProxy();
        if (reference == null) return null;

        if (reference instanceof PsiPolyVariantReference) {
            context.setItemsToShow(((PsiPolyVariantReference) reference).multiResolve(true));
        } else {
            context.setItemsToShow(new Object[] { reference.resolve() });
        }

        return call;
    }

    @Override
    public void showParameterInfo(@NotNull GLSLFunctionCallExpression call, @NotNull CreateParameterInfoContext context) {
        context.showHint(call, call.getTextRange().getStartOffset(), this);
    }

    @Nullable
    @Override
    public GLSLFunctionCallExpression findElementForUpdatingParameterInfo(@NotNull UpdateParameterInfoContext context) {
        GLSLFunctionCallExpression call =
                ParameterInfoUtils.findParentOfTypeWithStopElements(context.getFile(), context.getOffset(),
                        GLSLFunctionCallExpression.class, GLSLCompoundStatement.class);
        context.setParameterOwner(call);
        return call;
    }

    @Override
    public void updateParameterInfo(@NotNull GLSLFunctionCallExpression call, @NotNull UpdateParameterInfoContext context) {
        if (context.getParameterOwner() != call) {
            context.removeHint();
            return;
        }

        int index = ParameterInfoUtils.getCurrentParameterIndex(call.getParameterList().getNode(), context.getOffset(), GLSLTokenTypes.COMMA);
        context.setCurrentParameter(index);
    }

    @Nullable
    @Override
    public String getParameterCloseChars() {
        return ParameterInfoUtils.DEFAULT_PARAMETER_CLOSE_CHARS;
    }

    @Override
    public boolean tracksParameterIndex() {
        return false;
    }

    @Override
    public void updateUI(Object p, @NotNull ParameterInfoUIContext context) {
        if (p instanceof PsiElementResolveResult) p = ((PsiElementResolveResult) p).getElement();
        if (!(p instanceof GLSLFunctionDeclaration)) return;
        GLSLFunctionDeclaration declaration = (GLSLFunctionDeclaration) p;
        GLSLParameterDeclaration[] parameters = declaration.getParameters();

        StringBuilder buffer = new StringBuilder();
        buffer.append(declaration.getType().getReturnType().getTypename())
                .append(' ').append(declaration.getName()).append('(');

        final int currentParameter = context.getCurrentParameterIndex();
        int highlightStartOffset = -1, highlightEndOffset = -1;

        for (int i = 0; i < parameters.length; i++) {
            if (i == currentParameter) highlightStartOffset = buffer.length();
            buffer.append(parameters[i].getText());
            if (i == currentParameter) highlightEndOffset = buffer.length();
            if (i < parameters.length - 1) buffer.append(", ");
        }
        buffer.append(')');

        context.setupUIComponentPresentation(buffer.toString(), highlightStartOffset, highlightEndOffset, false, false, false, context.getDefaultParameterColor());
    }
}
