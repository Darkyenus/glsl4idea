/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 */

#version 130
#extension GL_ARB_shading_language_420pack: enable

void main() {
    // Illegal since b is of type vec3, but initialized with bool
    vec3 b = { true, false, true };
    gl_FragColor = vec4(1.0);
}
