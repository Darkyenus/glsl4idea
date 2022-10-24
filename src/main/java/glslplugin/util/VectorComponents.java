package glslplugin.util;

//TODO Combine with whatever is defined in GLSLVectorType
public enum VectorComponents {
    XYZW("xyzw"),
    RGBA("rgba"),
    STPQ("stpq");


    @SuppressWarnings("SpellCheckingInspection")
    public static final String[] SETS = new String[]{"xyzw","rgba","stpq"};

    public static final String ALL_COMPONENTS = "xyzwrgbastpq";

    private final String components;

    VectorComponents(String components) {
        this.components = components;
    }

    /**
     * Returns list of components from 0 to end (exclusive). If end > components length, clamps to components length.
     */
    public String getComponentRange(int end) {
        return components.substring(0, Math.min(end, components.length()));
    }
}