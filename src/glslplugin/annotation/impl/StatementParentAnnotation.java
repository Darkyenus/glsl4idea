package glslplugin.annotation.impl;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiElement;
import glslplugin.annotation.Annotator;
import glslplugin.lang.elements.declarations.GLSLFunctionDefinition;
import glslplugin.lang.elements.statements.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by abigail on 08/07/15.
 */
public class StatementParentAnnotation extends Annotator<GLSLStatement> {

    static class AcceptableParents {
        AcceptableParents(String message, Class<? extends PsiElement>... parents) {
            this.parents = new HashSet<>(Arrays.asList(parents));
            this.message = message;
        }
        Set<Class<? extends PsiElement>> parents;
        String message;
    }

    static Map<Class<? extends PsiElement>, AcceptableParents> parentsForClass;

    static {
        parentsForClass = new HashMap<>();
        parentsForClass.put(GLSLBreakStatement.class,
                new AcceptableParents("Break statement must be inside a loop or switch statement",
                        GLSLWhileStatement.class, GLSLForStatement.class, GLSLDoStatement.class, GLSLSwitchStatement.class));
        parentsForClass.put(GLSLContinueStatement.class,
                new AcceptableParents("Continue statement must be inside a loop statement",
                        GLSLWhileStatement.class, GLSLForStatement.class, GLSLDoStatement.class));
        parentsForClass.put(GLSLDefaultStatement.class,
                new AcceptableParents("Default statement must be inside a switch statement",
                        GLSLSwitchStatement.class));
        parentsForClass.put(GLSLReturnStatement.class,
                new AcceptableParents("Return statement must be inside a function definition",
                        GLSLFunctionDefinition.class));
    };

    @Override
    public void annotate(GLSLStatement expr, AnnotationHolder holder) {
        AcceptableParents acceptableParents = parentsForClass.get(expr.getClass());
        if (acceptableParents == null) return;
        PsiElement parent = expr.findParentByClasses(acceptableParents.parents);
        if (parent == null) { // we needed a parent and we couldn't find one - this is a compile-time error
            Annotation annotation = holder.createErrorAnnotation(expr, acceptableParents.message);
        }
    }

    @NotNull
    @Override
    public Class<GLSLStatement> getElementType() {
        return GLSLStatement.class;
    }
}
