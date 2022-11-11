package glslplugin.lang.elements.types;

import org.jetbrains.annotations.NotNull;

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
    public static final GLSLOpaqueType ATOMIC_UINT = new GLSLOpaqueType("atomic_uint");

    /** All non-specialized opaque types */
    public static final GLSLOpaqueType[] ALL = new GLSLOpaqueType[]{
            VOID,
            ATOMIC_UINT,
    };

    public static class Sampler extends GLSLOpaqueType {
        public final GLSLType baseType;

        private Sampler(String typename, GLSLType baseType) {
            super(typename);
            this.baseType = baseType;
        }

        public static final Sampler sampler1D = new Sampler("sampler1D", GLSLScalarType.FLOAT);
        public static final Sampler sampler2D = new Sampler("sampler2D", GLSLScalarType.FLOAT);
        public static final Sampler sampler3D = new Sampler("sampler3D", GLSLScalarType.FLOAT);
        public static final Sampler samplerCube = new Sampler("samplerCube", GLSLScalarType.FLOAT);
        public static final Sampler sampler2DRect = new Sampler("sampler2DRect", GLSLScalarType.FLOAT);
        public static final Sampler sampler1DArray = new Sampler("sampler1DArray", GLSLScalarType.FLOAT);
        public static final Sampler sampler2DArray = new Sampler("sampler2DArray", GLSLScalarType.FLOAT);
        public static final Sampler samplerBuffer = new Sampler("samplerBuffer", GLSLScalarType.FLOAT);
        public static final Sampler sampler2DMS = new Sampler("sampler2DMS", GLSLScalarType.FLOAT);
        public static final Sampler sampler2DMSArray = new Sampler("sampler2DMSArray", GLSLScalarType.FLOAT);
        public static final Sampler samplerCubeArray = new Sampler("samplerCubeArray", GLSLScalarType.FLOAT);

        public static final Sampler isampler1D = new Sampler("isampler1D", GLSLScalarType.INT);
        public static final Sampler isampler2D = new Sampler("isampler2D", GLSLScalarType.INT);
        public static final Sampler isampler3D = new Sampler("isampler3D", GLSLScalarType.INT);
        public static final Sampler isamplerCube = new Sampler("isamplerCube", GLSLScalarType.INT);
        public static final Sampler isampler2DRect = new Sampler("isampler2DRect", GLSLScalarType.INT);
        public static final Sampler isampler1DArray = new Sampler("isampler1DArray", GLSLScalarType.INT);
        public static final Sampler isampler2DArray = new Sampler("isampler2DArray", GLSLScalarType.INT);
        public static final Sampler isamplerBuffer = new Sampler("isamplerBuffer", GLSLScalarType.INT);
        public static final Sampler isampler2DMS = new Sampler("isampler2DMS", GLSLScalarType.INT);
        public static final Sampler isampler2DMSArray = new Sampler("isampler2DMSArray", GLSLScalarType.INT);
        public static final Sampler isamplerCubeArray = new Sampler("isamplerCubeArray", GLSLScalarType.INT);

        public static final Sampler usampler1D = new Sampler("usampler1D", GLSLScalarType.UINT);
        public static final Sampler usampler2D = new Sampler("usampler2D", GLSLScalarType.UINT);
        public static final Sampler usampler3D = new Sampler("usampler3D", GLSLScalarType.UINT);
        public static final Sampler usamplerCube = new Sampler("usamplerCube", GLSLScalarType.UINT);
        public static final Sampler usampler2DRect = new Sampler("usampler2DRect", GLSLScalarType.UINT);
        public static final Sampler usampler1DArray = new Sampler("usampler1DArray", GLSLScalarType.UINT);
        public static final Sampler usampler2DArray = new Sampler("usampler2DArray", GLSLScalarType.UINT);
        public static final Sampler usamplerBuffer = new Sampler("usamplerBuffer", GLSLScalarType.UINT);
        public static final Sampler usampler2DMS = new Sampler("usampler2DMS", GLSLScalarType.UINT);
        public static final Sampler usampler2DMSArray = new Sampler("usampler2DMSArray", GLSLScalarType.UINT);
        public static final Sampler usamplerCubeArray = new Sampler("usamplerCubeArray", GLSLScalarType.UINT);

        public static final Sampler[] ALL = new Sampler[]{
                sampler1D,
                sampler2D,
                sampler3D,
                samplerCube,
                sampler2DRect,
                sampler1DArray,
                sampler2DArray,
                samplerBuffer,
                sampler2DMS,
                sampler2DMSArray,
                samplerCubeArray,
                isampler1D,
                isampler2D,
                isampler3D,
                isamplerCube,
                isampler2DRect,
                isampler1DArray,
                isampler2DArray,
                isamplerBuffer,
                isampler2DMS,
                isampler2DMSArray,
                isamplerCubeArray,
                usampler1D,
                usampler2D,
                usampler3D,
                usamplerCube,
                usampler2DRect,
                usampler1DArray,
                usampler2DArray,
                usamplerBuffer,
                usampler2DMS,
                usampler2DMSArray,
                usamplerCubeArray,
        };
    }

    public static class ShadowSampler extends Sampler {
        private ShadowSampler(String typename) {
            super(typename, GLSLScalarType.FLOAT); // shadow samplers only exist for floats
        }

        public static final ShadowSampler sampler1DShadow = new ShadowSampler("sampler1DShadow");
        public static final ShadowSampler sampler2DShadow = new ShadowSampler("sampler2DShadow");
        public static final ShadowSampler sampler2DRectShadow = new ShadowSampler("sampler2DRectShadow");
        public static final ShadowSampler sampler1DArrayShadow = new ShadowSampler("sampler1DArrayShadow");
        public static final ShadowSampler sampler2DArrayShadow = new ShadowSampler("sampler2DArrayShadow");
        public static final ShadowSampler samplerCubeShadow = new ShadowSampler("samplerCubeShadow");
        public static final ShadowSampler samplerCubeArrayShadow = new ShadowSampler("samplerCubeArrayShadow");

        public static final ShadowSampler[] ALL = new ShadowSampler[]{
                sampler1DShadow,
                sampler2DShadow,
                sampler2DRectShadow,
                sampler1DArrayShadow,
                sampler2DArrayShadow,
                samplerCubeShadow,
                samplerCubeArrayShadow,
        };
    }

    public static class Image extends GLSLOpaqueType {
        public final GLSLType baseType;

        private Image(String typename, GLSLType baseType) {
            super(typename);
            this.baseType = baseType;
        }

        public static final Image image1D = new Image("image1D", GLSLScalarType.FLOAT);
        public static final Image image2D = new Image("image2D", GLSLScalarType.FLOAT);
        public static final Image image3D = new Image("image3D", GLSLScalarType.FLOAT);
        public static final Image imageCube = new Image("imageCube", GLSLScalarType.FLOAT);
        public static final Image image2DRect = new Image("image2DRect", GLSLScalarType.FLOAT);
        public static final Image image1DArray = new Image("image1DArray", GLSLScalarType.FLOAT);
        public static final Image image2DArray = new Image("image2DArray", GLSLScalarType.FLOAT);
        public static final Image imageBuffer = new Image("imageBuffer", GLSLScalarType.FLOAT);
        public static final Image image2DMS = new Image("image2DMS", GLSLScalarType.FLOAT);
        public static final Image image2DMSArray = new Image("image2DMSArray", GLSLScalarType.FLOAT);
        public static final Image imageCubeArray = new Image("imageCubeArray", GLSLScalarType.FLOAT);

        public static final Image iimage1D = new Image("iimage1D", GLSLScalarType.INT);
        public static final Image iimage2D = new Image("iimage2D", GLSLScalarType.INT);
        public static final Image iimage3D = new Image("iimage3D", GLSLScalarType.INT);
        public static final Image iimageCube = new Image("iimageCube", GLSLScalarType.INT);
        public static final Image iimage2DRect = new Image("iimage2DRect", GLSLScalarType.INT);
        public static final Image iimage1DArray = new Image("iimage1DArray", GLSLScalarType.INT);
        public static final Image iimage2DArray = new Image("iimage2DArray", GLSLScalarType.INT);
        public static final Image iimageBuffer = new Image("iimageBuffer", GLSLScalarType.INT);
        public static final Image iimage2DMS = new Image("iimage2DMS", GLSLScalarType.INT);
        public static final Image iimage2DMSArray = new Image("iimage2DMSArray", GLSLScalarType.INT);
        public static final Image iimageCubeArray = new Image("iimageCubeArray", GLSLScalarType.INT);

        public static final Image uimage1D = new Image("uimage1D", GLSLScalarType.UINT);
        public static final Image uimage2D = new Image("uimage2D", GLSLScalarType.UINT);
        public static final Image uimage3D = new Image("uimage3D", GLSLScalarType.UINT);
        public static final Image uimageCube = new Image("uimageCube", GLSLScalarType.UINT);
        public static final Image uimage2DRect = new Image("uimage2DRect", GLSLScalarType.UINT);
        public static final Image uimage1DArray = new Image("uimage1DArray", GLSLScalarType.UINT);
        public static final Image uimage2DArray = new Image("uimage2DArray", GLSLScalarType.UINT);
        public static final Image uimageBuffer = new Image("uimageBuffer", GLSLScalarType.UINT);
        public static final Image uimage2DMS = new Image("uimage2DMS", GLSLScalarType.UINT);
        public static final Image uimage2DMSArray = new Image("uimage2DMSArray", GLSLScalarType.UINT);
        public static final Image uimageCubeArray = new Image("uimageCubeArray", GLSLScalarType.UINT);

        public static final Image[] ALL = new Image[]{
                image1D,
                image2D,
                image3D,
                imageCube,
                image2DRect,
                image1DArray,
                image2DArray,
                imageBuffer,
                image2DMS,
                image2DMSArray,
                imageCubeArray,
                iimage1D,
                iimage2D,
                iimage3D,
                iimageCube,
                iimage2DRect,
                iimage1DArray,
                iimage2DArray,
                iimageBuffer,
                iimage2DMS,
                iimage2DMSArray,
                iimageCubeArray,
                uimage1D,
                uimage2D,
                uimage3D,
                uimageCube,
                uimage2DRect,
                uimage1DArray,
                uimage2DArray,
                uimageBuffer,
                uimage2DMS,
                uimage2DMSArray,
                uimageCubeArray,
        };
    }

    private final String typename;

    private GLSLOpaqueType(String typename) {
        super(null);
        this.typename = typename;
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
