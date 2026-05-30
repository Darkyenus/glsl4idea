package glslplugin.lang;

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import glslplugin.LightGLSLTestCase;
import glslplugin.lang.elements.GLSLTokenTypes;
import glslplugin.lang.elements.declarations.GLSLDeclarator;
import glslplugin.lang.elements.types.GLSLScalarType;
import glslplugin.lang.elements.types.GLSLType;
import glslplugin.lang.elements.types.GLSLTypes;
import glslplugin.lang.elements.types.GLSLVectorType;

import java.util.HashMap;
import java.util.Map;

public class ExtendedTypeSupportTest extends LightGLSLTestCase {

    private static final class TypeCase {
        final String typeName;
        final IElementType tokenType;
        final GLSLType expectedType;
        final String variableName;

        private TypeCase(String typeName, IElementType tokenType, GLSLType expectedType, String variableName) {
            this.typeName = typeName;
            this.tokenType = tokenType;
            this.expectedType = expectedType;
            this.variableName = variableName;
        }
    }

    public void testExtendedBuiltinTypesAreLexedAndResolved() {
        final TypeCase[] cases = new TypeCase[]{
                new TypeCase("int8_t", GLSLTokenTypes.INT8_TYPE, GLSLScalarType.INT8, "s8"),
                new TypeCase("i8vec2", GLSLTokenTypes.I8VEC2_TYPE, GLSLVectorType.getType(GLSLScalarType.INT8, 2), "v8_2"),
                new TypeCase("i8vec3", GLSLTokenTypes.I8VEC3_TYPE, GLSLVectorType.getType(GLSLScalarType.INT8, 3), "v8_3"),
                new TypeCase("i8vec4", GLSLTokenTypes.I8VEC4_TYPE, GLSLVectorType.getType(GLSLScalarType.INT8, 4), "v8_4"),
                new TypeCase("int16_t", GLSLTokenTypes.INT16_TYPE, GLSLScalarType.INT16, "s16"),
                new TypeCase("i16vec2", GLSLTokenTypes.I16VEC2_TYPE, GLSLVectorType.getType(GLSLScalarType.INT16, 2), "v16_2"),
                new TypeCase("i16vec3", GLSLTokenTypes.I16VEC3_TYPE, GLSLVectorType.getType(GLSLScalarType.INT16, 3), "v16_3"),
                new TypeCase("i16vec4", GLSLTokenTypes.I16VEC4_TYPE, GLSLVectorType.getType(GLSLScalarType.INT16, 4), "v16_4"),
                new TypeCase("int64_t", GLSLTokenTypes.INT64_TYPE, GLSLScalarType.INT64, "s64"),
                new TypeCase("i64vec2", GLSLTokenTypes.I64VEC2_TYPE, GLSLVectorType.getType(GLSLScalarType.INT64, 2), "v64_2"),
                new TypeCase("i64vec3", GLSLTokenTypes.I64VEC3_TYPE, GLSLVectorType.getType(GLSLScalarType.INT64, 3), "v64_3"),
                new TypeCase("i64vec4", GLSLTokenTypes.I64VEC4_TYPE, GLSLVectorType.getType(GLSLScalarType.INT64, 4), "v64_4"),
                new TypeCase("uint8_t", GLSLTokenTypes.UINT8_TYPE, GLSLScalarType.UINT8, "us8"),
                new TypeCase("u8vec2", GLSLTokenTypes.U8VEC2_TYPE, GLSLVectorType.getType(GLSLScalarType.UINT8, 2), "uv8_2"),
                new TypeCase("u8vec3", GLSLTokenTypes.U8VEC3_TYPE, GLSLVectorType.getType(GLSLScalarType.UINT8, 3), "uv8_3"),
                new TypeCase("u8vec4", GLSLTokenTypes.U8VEC4_TYPE, GLSLVectorType.getType(GLSLScalarType.UINT8, 4), "uv8_4"),
                new TypeCase("uint16_t", GLSLTokenTypes.UINT16_TYPE, GLSLScalarType.UINT16, "us16"),
                new TypeCase("u16vec2", GLSLTokenTypes.U16VEC2_TYPE, GLSLVectorType.getType(GLSLScalarType.UINT16, 2), "uv16_2"),
                new TypeCase("u16vec3", GLSLTokenTypes.U16VEC3_TYPE, GLSLVectorType.getType(GLSLScalarType.UINT16, 3), "uv16_3"),
                new TypeCase("u16vec4", GLSLTokenTypes.U16VEC4_TYPE, GLSLVectorType.getType(GLSLScalarType.UINT16, 4), "uv16_4"),
                new TypeCase("uint64_t", GLSLTokenTypes.UINT64_TYPE, GLSLScalarType.UINT64, "us64"),
                new TypeCase("u64vec2", GLSLTokenTypes.U64VEC2_TYPE, GLSLVectorType.getType(GLSLScalarType.UINT64, 2), "uv64_2"),
                new TypeCase("u64vec3", GLSLTokenTypes.U64VEC3_TYPE, GLSLVectorType.getType(GLSLScalarType.UINT64, 3), "uv64_3"),
                new TypeCase("u64vec4", GLSLTokenTypes.U64VEC4_TYPE, GLSLVectorType.getType(GLSLScalarType.UINT64, 4), "uv64_4"),
                new TypeCase("float16_t", GLSLTokenTypes.FLOAT16_TYPE, GLSLScalarType.FLOAT16, "h"),
                new TypeCase("f16vec2", GLSLTokenTypes.F16VEC2_TYPE, GLSLVectorType.getType(GLSLScalarType.FLOAT16, 2), "hv2"),
                new TypeCase("f16vec3", GLSLTokenTypes.F16VEC3_TYPE, GLSLVectorType.getType(GLSLScalarType.FLOAT16, 3), "hv3"),
                new TypeCase("f16vec4", GLSLTokenTypes.F16VEC4_TYPE, GLSLVectorType.getType(GLSLScalarType.FLOAT16, 4), "hv4"),
        };

        final StringBuilder shader = new StringBuilder();
        for (TypeCase testCase : cases) {
            shader.append(testCase.typeName).append(' ').append(testCase.variableName).append(";\n");
        }

        myFixture.configureByText(GLSLFileType.INSTANCE, shader.toString());

        final String documentText = myFixture.getFile().getText();
        final Map<String, GLSLDeclarator> declaratorsByName = new HashMap<>();
        for (GLSLDeclarator declarator : PsiTreeUtil.findChildrenOfType(myFixture.getFile(), GLSLDeclarator.class)) {
            declaratorsByName.put(declarator.getVariableName(), declarator);
        }

        for (TypeCase testCase : cases) {
            assertSame(testCase.expectedType, GLSLTypes.getTypeFromName(testCase.typeName));

            final int tokenOffset = documentText.indexOf(testCase.typeName);
            assertTrue("Missing token text for " + testCase.typeName, tokenOffset >= 0);
            final PsiElement tokenElement = myFixture.getFile().findElementAt(tokenOffset);
            assertNotNull(tokenElement);
            assertEquals(testCase.tokenType, tokenElement.getNode().getElementType());

            final GLSLDeclarator declarator = declaratorsByName.get(testCase.variableName);
            assertNotNull("Missing declarator for " + testCase.variableName, declarator);
            assertSame(testCase.expectedType, declarator.getType());
        }
    }
}
