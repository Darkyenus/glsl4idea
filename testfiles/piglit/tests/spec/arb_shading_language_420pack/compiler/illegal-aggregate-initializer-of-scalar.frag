/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 */

#version 130
#extension GL_ARB_shading_language_420pack: enable

void main() {
    // Illegal since i is not an aggregate
    int i = { 1 };
    gl_FragColor = vec4(1.0);
}
