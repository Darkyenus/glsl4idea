package glslplugin.extensions;

import com.intellij.lang.parameterInfo.CreateParameterInfoContext;
import com.intellij.lang.parameterInfo.ParameterInfoHandler;
import com.intellij.lang.parameterInfo.ParameterInfoUIContext;
import com.intellij.lang.parameterInfo.ParameterInfoUtils;
import com.intellij.lang.parameterInfo.UpdateParameterInfoContext;
import com.intellij.psi.PsiElementResolveResult;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.declarations.GLSLFunctionDeclaration;
import glslplugin.lang.elements.declarations.GLSLParameterDeclaration;
import glslplugin.lang.elements.expressions.GLSLFunctionOrConstructorCallExpression;
import glslplugin.lang.elements.expressions.GLSLParameterList;
import glslplugin.lang.elements.statements.GLSLCompoundStatement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abigail on 08/07/15.
 */
public class GLSLParameterInfoHandler implements ParameterInfoHandler<GLSLFunctionOrConstructorCallExpression, Object> {

    @Nullable
    @Override
    public GLSLFunctionOrConstructorCallExpression findElementForParameterInfo(@NotNull CreateParameterInfoContext context) {
        GLSLFunctionOrConstructorCallExpression call =
                ParameterInfoUtils.findParentOfTypeWithStopElements(context.getFile(), context.getOffset(),
                        GLSLFunctionOrConstructorCallExpression.class, GLSLCompoundStatement.class);
        if (call == null) return null;

        final GLSLFunctionOrConstructorCallExpression.FunctionCallOrConstructorReference ref = call.getReference();
        if (ref == null) return null;

        Object[] items = ref.multiResolve(false);
        if (items.length == 0) {
            List<GLSLFunctionDeclaration> declarations = new ArrayList<>();
            for (Object variant : ref.getVariants()) {
                if (!(variant instanceof GLSLDeclarator declarator)) continue;
                if (declarator.getParentDeclaration() instanceof GLSLFunctionDeclaration) {
                    String name = declarator.getVariableName();
                    if (name != null && name.equals(call.getFunctionOrConstructedTypeName())) {
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
    public void showParameterInfo(@NotNull GLSLFunctionOrConstructorCallExpression call, @NotNull CreateParameterInfoContext context) {
        context.showHint(call, call.getTextRange().getStartOffset(), this);
    }

    @Nullable
    @Override
    public GLSLFunctionOrConstructorCallExpression findElementForUpdatingParameterInfo(@NotNull UpdateParameterInfoContext context) {
        GLSLFunctionOrConstructorCallExpression call =
                ParameterInfoUtils.findParentOfTypeWithStopElements(context.getFile(), context.getOffset(),
                        GLSLFunctionOrConstructorCallExpression.class, GLSLCompoundStatement.class);
        context.setParameterOwner(call);
        return call;
    }

    @Override
    public void updateParameterInfo(@NotNull GLSLFunctionOrConstructorCallExpression call, @NotNull UpdateParameterInfoContext context) {
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

    @Override
    public void updateUI(Object p, @NotNull ParameterInfoUIContext context) {
        if (p instanceof PsiElementResolveResult) p = ((PsiElementResolveResult) p).getElement();
        if (!(p instanceof GLSLFunctionDeclaration declaration)) return;
        GLSLParameterDeclaration[] parameters = declaration.getParameters();

        StringBuilder buffer = new StringBuilder();
        buffer.append(declaration.getType().getReturnType().getTypename())
                .append(' ').append(declaration.getFunctionName()).append('(');

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
