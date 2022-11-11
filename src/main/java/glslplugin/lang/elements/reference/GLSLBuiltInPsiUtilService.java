package glslplugin.lang.elements.reference;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.LookupItem;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import glslplugin.lang.GLSLFileType;
import glslplugin.lang.elements.declarations.GLSLFunctionDeclaration;
import glslplugin.lang.elements.declarations.GLSLStructDefinition;
import glslplugin.lang.elements.types.GLSLMatrixType;
import glslplugin.lang.elements.types.GLSLOpaqueType;
import glslplugin.lang.elements.types.GLSLScalarType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import glslplugin.lang.elements.types.GLSLVectorType;
import glslplugin.lang.parser.GLSLFile;
import glslplugin.util.VectorComponents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public final class GLSLBuiltInPsiUtilService {

    private final Project project;

    public GLSLBuiltInPsiUtilService(Project project) {
        this.project = project;
    }

    private GLSLFile createImmutableDummyFile(String content) {
        GLSLFile file = (GLSLFile) PsiFileFactory.getInstance(project).createFileFromText("dummy.glsl", GLSLFileType.INSTANCE, content).getContainingFile();
        try {
            file.getViewProvider().getVirtualFile().setWritable(false);
        } catch (IOException ignored) {}
        return file;
    }

    /**
     * Contains lazily-created dummy structs for scalars and vectors that contain swizzling fields,
     * and for other fields that are opaque.
     */
    private final HashMap<GLSLType, GLSLStructDefinition> DEFINITIONS = new HashMap<>();

    public @NotNull GLSLStructDefinition getScalarDefinition(GLSLScalarType type) {
        GLSLStructDefinition definition = DEFINITIONS.get(type);
        if (definition == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("struct __").append(type.getTypename()).append("__ {\n");

            final String baseTypeName = type.getBaseType().getTypename();
            for (String swizzleSet : VectorComponents.SETS) {
                sb.append(baseTypeName).append(' ').append(swizzleSet.charAt(0)).append(";\n");
            }
            sb.append("};\n");

            final GLSLFile file = createImmutableDummyFile(sb.toString());
            definition = PsiTreeUtil.findChildOfType(file, GLSLStructDefinition.class);
            assert definition != null;
            DEFINITIONS.put(type, definition);
        }
        return definition;
    }
    public @NotNull GLSLStructDefinition getVecDefinition(GLSLVectorType type) {
        GLSLStructDefinition definition = DEFINITIONS.get(type);
        if (definition == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("struct __").append(type.getTypename()).append("__ {\n");

            final String baseTypeName = type.getBaseType().getTypename();
            for (String swizzleSet : VectorComponents.SETS) {
                for (int i = 0; i < type.getNumComponents(); i++) {
                    sb.append(baseTypeName).append(' ').append(swizzleSet.charAt(i)).append(";\n");
                }
            }
            sb.append("};\n");

            final GLSLFile file = createImmutableDummyFile(sb.toString());
            definition = PsiTreeUtil.findChildOfType(file, GLSLStructDefinition.class);
            assert definition != null;
            DEFINITIONS.put(type, definition);
        }
        return definition;
    }

    public @NotNull GLSLStructDefinition getMatrixDefinition(GLSLMatrixType type) {
        GLSLStructDefinition definition = DEFINITIONS.get(type);
        if (definition == null) {
            //TODO This may be problematic, because matrices don't have any fields, which is technically illegal
            final GLSLFile file = createImmutableDummyFile("struct __"+type.getTypename()+"__ { void __opaque__; };");
            definition = PsiTreeUtil.findChildOfType(file, GLSLStructDefinition.class);
            assert definition != null;
            DEFINITIONS.put(type, definition);
        }
        return definition;
    }

    public @NotNull GLSLStructDefinition getOpaqueDefinition(GLSLOpaqueType type) {
        GLSLStructDefinition definition = DEFINITIONS.get(type);
        if (definition == null) {
            //TODO This may be problematic, because opaque types don't have any fields, which is technically illegal
            final GLSLFile file = createImmutableDummyFile("struct __"+type.getTypename()+"__ { void __opaque__; };");
            definition = PsiTreeUtil.findChildOfType(file, GLSLStructDefinition.class);
            assert definition != null;
            DEFINITIONS.put(type, definition);
        }
        return definition;
    }

    private GLSLFunctionDeclaration lengthMethod;

    public @NotNull GLSLFunctionDeclaration getLengthMethodDeclaration() {
        GLSLFunctionDeclaration lengthMethod = this.lengthMethod;
        if (lengthMethod == null) {
            final GLSLFile file = createImmutableDummyFile("int length();");
            final PsiElement functionDefinition = file.getFirstChild();
            if (functionDefinition instanceof GLSLFunctionDeclaration decl) {
                this.lengthMethod = lengthMethod = decl;
            } else {
                throw new AssertionError("length() dummy did not parse");
            }
        }
        return lengthMethod;
    }

    private GLSLFile builtinsFile;
    private boolean builtinsFileResolved;

    public @Nullable GLSLFile getBuiltinsFile() {
        // We have a generated file with builtins in res/glsl/builtin.glsl
        // and also granular files in res/environment. The granular files are much better
        // quality and contain documentation, but also contain non-resolved types (genType)
        // which we don't know how to parse yet.
        if (builtinsFileResolved) {
            return builtinsFile;
        }

        builtinsFileResolved = true;
        URL builtinUrl = getClass().getClassLoader().getResource("glsl/builtin.glsl");
        if (builtinUrl == null) return null;
        final VirtualFile file = VfsUtil.findFileByURL(builtinUrl);
        if (file == null) return null;
        final PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
        if (psiFile instanceof GLSLFile glslPsiFile) {
            glslPsiFile.isBuiltinFile = true;
            return builtinsFile = glslPsiFile;
        }
        return null;
    }


    public final ArrayList<LookupElement> builtinTypeLookup;

    {
        final List<GLSLType> types = GLSLTypes.builtinTypes();
        builtinTypeLookup = new ArrayList<>(types.size());
        for (GLSLType type : types) {
            LookupElementBuilder lookup = LookupElementBuilder.create(type, type.getTypename());

            if (type instanceof GLSLMatrixType mat && !mat.shortName.equals(mat.fullName)) {
                lookup = lookup.withLookupString(mat.fullName);
            }

            builtinTypeLookup.add(lookup);
        }
    }
}
