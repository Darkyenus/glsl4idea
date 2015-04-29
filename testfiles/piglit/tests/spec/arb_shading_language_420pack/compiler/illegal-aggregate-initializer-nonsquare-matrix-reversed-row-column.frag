/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 */

#version 130
#extension GL_ARB_shading_language_420pack: enable

void main() {
    // Illegal since mat4x2 consists of 4x vec2 columns, but 2x vec4 columns
    // given as initializer
    mat4x2 c = { vec4(0.0), vec4(1.0) };
    gl_FragColor = vec4(1.0);
}
