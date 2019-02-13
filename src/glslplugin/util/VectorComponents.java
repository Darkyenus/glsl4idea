package glslplugin.util;

public enum VectorComponents {
    XYZW("xyzw"),
    RGBA("rgba"),
    STPQ("stpq");

    private String components;

    VectorComponents(String components) {
        this.components = components;
    }

    public String getComponent(int i) {
        return String.valueOf(components.charAt(i));
    }

    /**
     * Returns list of components from 0 to end (exclusive). If end > components length, clamps to components length.
     *
     * @param end
     * @return
     */
    public String getComponentRange(int end) {
        return components.substring(0, Math.min(end, components.length()));
    }
}