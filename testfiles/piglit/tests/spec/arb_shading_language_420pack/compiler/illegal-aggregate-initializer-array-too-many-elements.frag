/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 */

#version 130
#extension GL_ARB_shading_language_420pack: enable

void main() {
    // Illegal since a has only 2 elements, but 3 elements given as initializer
    float a[2] = { 3.4, 4.2, 5.0 };
    gl_FragColor = vec4(1.0);
}
