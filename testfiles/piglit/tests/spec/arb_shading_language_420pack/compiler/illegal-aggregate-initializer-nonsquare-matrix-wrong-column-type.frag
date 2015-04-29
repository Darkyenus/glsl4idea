/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 */

#version 130
#extension GL_ARB_shading_language_420pack: enable

void main() {
    // Illegal since columns of a mat4x2 are vec2 but vec4 are given as initializers
    mat4x2 c = { vec4(0.0), vec4(1.0), vec4(0.0), vec4(1.0) };
    gl_FragColor = vec4(1.0);
}
