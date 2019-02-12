package glslplugin.lang.elements.types;

import org.jetbrains.annotations.NotNull;

import static glslplugin.lang.elements.types.GLSLTypes.*;

/**
 * Opaque type is a common class for types which have no members, functions, etc.
 * That is VOID and samplers.
 *
 * @author Jan Polák
 */
public class GLSLOpaqueType extends GLSLType {

    // void
    public static final GLSLOpaqueType VOID = new GLSLOpaqueType("void");

    // atomics
    public static final GLSLOpaqueType atomic_uint = new GLSLOpaqueType("atomic_uint");

    @SuppressWarnings("unused")
    public static class Sampler extends GLSLOpaqueType {
        public final GLSLType baseType;

        private Sampler(String typename, GLSLType baseType) {
            super(typename);
            this.baseType = baseType;
        }

        public static final Sampler sampler1D = new Sampler("sampler1D", FLOAT);
        public static final Sampler sampler2D = new Sampler("sampler2D", FLOAT);
        public static final Sampler sampler3D = new Sampler("sampler3D", FLOAT);
        public static final Sampler samplerCube = new Sampler("samplerCube", FLOAT);
        public static final Sampler sampler2DRect = new Sampler("sampler2DRect", FLOAT);
        public static final Sampler sampler1DArray = new Sampler("sampler1DArray", FLOAT);
        public static final Sampler sampler2DArray = new Sampler("sampler2DArray", FLOAT);
        public static final Sampler samplerBuffer = new Sampler("samplerBuffer", FLOAT);
        public static final Sampler sampler2DMS = new Sampler("sampler2DMS", FLOAT);
        public static final Sampler sampler2DMSArray = new Sampler("sampler2DMSArray", FLOAT);
        public static final Sampler samplerCubeArray = new Sampler("samplerCubeArray", FLOAT);

        public static final Sampler isampler1D = new Sampler("isampler1D", INT);
        public static final Sampler isampler2D = new Sampler("isampler2D", INT);
        public static final Sampler isampler3D = new Sampler("isampler3D", INT);
        public static final Sampler isamplerCube = new Sampler("isamplerCube", INT);
        public static final Sampler isampler2DRect = new Sampler("isampler2DRect", INT);
        public static final Sampler isampler1DArray = new Sampler("isampler1DArray", INT);
        public static final Sampler isampler2DArray = new Sampler("isampler2DArray", INT);
        public static final Sampler isamplerBuffer = new Sampler("isamplerBuffer", INT);
        public static final Sampler isampler2DMS = new Sampler("isampler2DMS", INT);
        public static final Sampler isampler2DMSArray = new Sampler("isampler2DMSArray", INT);
        public static final Sampler isamplerCubeArray = new Sampler("isamplerCubeArray", INT);

        public static final Sampler usampler1D = new Sampler("usampler1D", UINT);
        public static final Sampler usampler2D = new Sampler("usampler2D", UINT);
        public static final Sampler usampler3D = new Sampler("usampler3D", UINT);
        public static final Sampler usamplerCube = new Sampler("usamplerCube", UINT);
        public static final Sampler usampler2DRect = new Sampler("usampler2DRect", UINT);
        public static final Sampler usampler1DArray = new Sampler("usampler1DArray", UINT);
        public static final Sampler usampler2DArray = new Sampler("usampler2DArray", UINT);
        public static final Sampler usamplerBuffer = new Sampler("usamplerBuffer", UINT);
        public static final Sampler usampler2DMS = new Sampler("usampler2DMS", UINT);
        public static final Sampler usampler2DMSArray = new Sampler("usampler2DMSArray", UINT);
        public static final Sampler usamplerCubeArray = new Sampler("usamplerCubeArray", UINT);
    }
    static {
        Sampler.class.getName(); // Force load and registration
    }

    @SuppressWarnings("unused")
    public static class ShadowSampler extends Sampler {
        private ShadowSampler(String typename) {
            super(typename, GLSLTypes.FLOAT); // shadow samplers only exist for floats
        }

        public static final ShadowSampler sampler1DShadow = new ShadowSampler("sampler1DShadow");
        public static final ShadowSampler sampler2DShadow = new ShadowSampler("sampler2DShadow");
        public static final ShadowSampler sampler2DRectShadow = new ShadowSampler("sampler2DRectShadow");
        public static final ShadowSampler sampler1DArrayShadow = new ShadowSampler("sampler1DArrayShadow");
        public static final ShadowSampler sampler2DArrayShadow = new ShadowSampler("sampler2DArrayShadow");
        public static final ShadowSampler samplerCubeShadow = new ShadowSampler("samplerCubeShadow");
        public static final ShadowSampler samplerCubeArrayShadow = new ShadowSampler("samplerCubeArrayShadow");
    }
    static {
        ShadowSampler.class.getName(); // Force load and registration
    }

    @SuppressWarnings("unused")
    public static class Image extends GLSLOpaqueType {
        public final GLSLType baseType;

        private Image(String typename, GLSLType baseType) {
            super(typename);
            this.baseType = baseType;
        }

        public static final Image image1D = new Image("image1D", FLOAT);
        public static final Image image2D = new Image("image2D", FLOAT);
        public static final Image image3D = new Image("image3D", FLOAT);
        public static final Image imageCube = new Image("imageCube", FLOAT);
        public static final Image image2DRect = new Image("image2DRect", FLOAT);
        public static final Image image1DArray = new Image("image1DArray", FLOAT);
        public static final Image image2DArray = new Image("image2DArray", FLOAT);
        public static final Image imageBuffer = new Image("imageBuffer", FLOAT);
        public static final Image image2DMS = new Image("image2DMS", FLOAT);
        public static final Image image2DMSArray = new Image("image2DMSArray", FLOAT);
        public static final Image imageCubeArray = new Image("imageCubeArray", FLOAT);

        public static final Image iimage1D = new Image("iimage1D", INT);
        public static final Image iimage2D = new Image("iimage2D", INT);
        public static final Image iimage3D = new Image("iimage3D", INT);
        public static final Image iimageCube = new Image("iimageCube", INT);
        public static final Image iimage2DRect = new Image("iimage2DRect", INT);
        public static final Image iimage1DArray = new Image("iimage1DArray", INT);
        public static final Image iimage2DArray = new Image("iimage2DArray", INT);
        public static final Image iimageBuffer = new Image("iimageBuffer", INT);
        public static final Image iimage2DMS = new Image("iimage2DMS", INT);
        public static final Image iimage2DMSArray = new Image("iimage2DMSArray", INT);
        public static final Image iimageCubeArray = new Image("iimageCubeArray", INT);

        public static final Image uimage1D = new Image("uimage1D", UINT);
        public static final Image uimage2D = new Image("uimage2D", UINT);
        public static final Image uimage3D = new Image("uimage3D", UINT);
        public static final Image uimageCube = new Image("uimageCube", UINT);
        public static final Image uimage2DRect = new Image("uimage2DRect", UINT);
        public static final Image uimage1DArray = new Image("uimage1DArray", UINT);
        public static final Image uimage2DArray = new Image("uimage2DArray", UINT);
        public static final Image uimageBuffer = new Image("uimageBuffer", UINT);
        public static final Image uimage2DMS = new Image("uimage2DMS", UINT);
        public static final Image uimage2DMSArray = new Image("uimage2DMSArray", UINT);
        public static final Image uimageCubeArray = new Image("uimageCubeArray", UINT);
    }
    static {
        Image.class.getName(); // Force load and registration
    }

    private final String typename;

    private GLSLOpaqueType(String typename) {
        super(null);
        this.typename = typename;
        GLSLTypes.register(this);
    }

    @NotNull
    public String getTypename() {
        return typename;
    }

    public boolean typeEquals(GLSLType otherType) {
        return this == otherType;
    }

    public boolean isConvertibleTo(GLSLType otherType) {
        return typeEquals(otherType);
    }
}
