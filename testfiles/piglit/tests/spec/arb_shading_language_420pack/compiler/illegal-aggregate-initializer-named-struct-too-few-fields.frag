/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * require_extensions: GL_ARB_shading_language_420pack
 * [end config]
 */

#version 130
#extension GL_ARB_shading_language_420pack: enable

void main() {
    // Illegal since e has 2 fields, but 1 given as initializer
    struct S {
        float a;
        int b;
    };
    S e = { 1.2 };
    gl_FragColor = vec4(1.0);
}
