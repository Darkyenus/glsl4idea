/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 */

#version 130
#extension GL_ARB_shading_language_420pack: enable

void main() {
    // Illegal since there's a constant array access of a[4] but there are only
    // 4 elements
    float[] a = { 0.0, 1.0, 2.0, 3.0 };
    gl_FragColor = vec4(a[4]);
}
