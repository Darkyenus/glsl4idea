// [config]
// expect_result: fail
// glsl_version: 3.30
// [end config]

/* The GLSL 3.30 spec says
 *   "If no profile argument is provided, the default is core."
 */
#version 330

void main() {
    /* gl_ClipVertex is deprecated and not available in GLSL 3.30 core, so
     * it should cause a compilation error.
     */
    gl_Position = gl_ClipVertex;
}
