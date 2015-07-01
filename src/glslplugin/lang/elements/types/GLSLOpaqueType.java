package glslplugin.lang.elements.types;

import org.jetbrains.annotations.NotNull;

/**
 * Opaque type is a common class for types which have no members, functions, etc.
 * That is VOID and samplers.
 *
 * @author Jan Polák
 */
public class GLSLOpaqueType  extends GLSLType {
    // void
    public static final GLSLOpaqueType VOID = new GLSLOpaqueType("void");

    // samplers
    public static final GLSLOpaqueType SAMPLER1D = new GLSLOpaqueType("sampler1D");
    public static final GLSLOpaqueType SAMPLER2D = new GLSLOpaqueType("sampler2D");
    public static final GLSLOpaqueType SAMPLER3D = new GLSLOpaqueType("sampler3D");
    public static final GLSLOpaqueType SAMPLER1D_SHADOW = new GLSLOpaqueType("sampler1DShadow");
    public static final GLSLOpaqueType SAMPLER2D_SHADOW = new GLSLOpaqueType("sampler2DShadow");
    public static final GLSLOpaqueType SAMPLER_CUBE = new GLSLOpaqueType("samplerCube");

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

    @Override
    public String toString() {
        return "Type: " + getTypename();
    }
}