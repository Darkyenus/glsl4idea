// [config]
// expect_result: fail
// glsl_version: 1.50
// [end config]

/* The GLSL 1.50 spec says
 *   "If no profile argument is provided, the default is core."
 */
#version 150

void main() {
    /* gl_ClipVertex is deprecated and not available in GLSL 1.50 core, so
     * it should cause a compilation error.
     */
    gl_Position = gl_ClipVertex;
}
