package glslplugin.lang.elements.statements;

import org.jetbrains.annotations.Nullable;

public interface LoopStatement extends ConditionStatement {

    @Nullable
    GLSLStatement getBody();

}
