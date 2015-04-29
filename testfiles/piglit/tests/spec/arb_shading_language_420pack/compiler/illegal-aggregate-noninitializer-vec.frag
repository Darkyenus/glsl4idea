/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 */

#version 130
#extension GL_ARB_shading_language_420pack: enable

void main() {
    vec3 b;
    // Illegal since C-style syntax can only be used as initializers
    b = { 1.0, 1.0, 1.0 };
    gl_FragColor = vec4(1.0);
}
