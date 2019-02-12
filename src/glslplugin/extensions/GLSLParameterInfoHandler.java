package glslplugin.extensions;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.parameterInfo.*;
import com.intellij.psi.PsiElementResolveResult;
import glslplugin.lang.elements.GLSLElement;
import glslplugin.lang.elements.GLSLIdentifier;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.declarations.GLSLFunctionDeclaration;
import glslplugin.lang.elements.declarations.GLSLParameterDeclaration;
import glslplugin.lang.elements.expressions.GLSLFunctionCallExpression;
import glslplugin.lang.elements.expressions.GLSLParameterList;
import glslplugin.lang.elements.reference.GLSLFunctionReference;
import glslplugin.lang.elements.reference.GLSLReferenceBase;
import glslplugin.lang.elements.statements.GLSLCompoundStatement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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

    @SuppressWarnings("deprecation") // Kept for compatibility
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

        final GLSLReferenceBase<GLSLIdentifier, ? extends GLSLElement> rawReference = call.getReferenceProxy();
        if (!(rawReference instanceof GLSLFunctionReference))return null;

       GLSLFunctionReference reference = ((GLSLFunctionReference) rawReference);

        Object[] items = reference.multiResolve(false);
        if (items.length == 0) {
            List<GLSLFunctionDeclaration> declarations = new ArrayList<>();
            for (Object variant : reference.getVariants()) {
                if (!(variant instanceof GLSLDeclarator)) continue;
                GLSLDeclarator declarator = (GLSLDeclarator) variant;
                if (declarator.getParentDeclaration() instanceof GLSLFunctionDeclaration) {
                    String name = declarator.getName();
                    if (name.equals(call.getFunctionName())) {
                        declarations.add((GLSLFunctionDeclaration) declarator.getParentDeclaration());
                    }

                }
            }
            items = declarations.toArray();
        }

        context.setItemsToShow(items);
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

        final GLSLParameterList parameterList = call.getParameterList();
        if (parameterList == null) {
            return;
        }
        int index = ParameterInfoUtils.getCurrentParameterIndex(parameterList.getNode(), context.getOffset(), GLSLTokenTypes.COMMA);
        context.setCurrentParameter(index);
    }

    @SuppressWarnings("deprecation") // Kept for compatibility
    @Nullable
    @Override
    public String getParameterCloseChars() {
        return ParameterInfoUtils.DEFAULT_PARAMETER_CLOSE_CHARS;
    }

    @SuppressWarnings("deprecation") // Kept for compatibility
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
