/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 */

#version 130
#extension GL_ARB_shading_language_420pack: enable

void main() {
    // Illegal since b is a 3-element vector, but 4 elements given as initializer
    vec3 b = { 1.0, 2.0, 3.0, 4.0 };
    gl_FragColor = vec4(1.0);
}
