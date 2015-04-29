// [config]
// expect_result: fail
// glsl_version: 1.30
// require_extensions: GL_AMD_conservative_depth
// [end config]
//
// Cannot redeclare gl_FragDepth in vertex shader.
//
// From the AMD_conservative_depth spec:
//     The built-in gl_FragDepth is only predeclared in fragment shaders, so
//     redeclaring it in any other shader language will be illegal.

#version 130
#extension GL_AMD_conservative_depth: require

layout(depth_any) out float gl_FragDepth;

float f() {
    return 0.0;
}
