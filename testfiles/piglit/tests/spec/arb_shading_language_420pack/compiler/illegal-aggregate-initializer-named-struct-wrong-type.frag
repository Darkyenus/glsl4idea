/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 */

#version 130
#extension GL_ARB_shading_language_420pack: enable

void main() {
    // Illegal since e.b is int, but bool given as initializer
    struct S {
        float a;
        int b;
    };
    S e = { 1.2, true };
    gl_FragColor = vec4(1.0);
}
