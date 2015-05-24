// [config]
// expect_result: fail
// glsl_version: 1.30
// require_extensions: GL_AMD_conservative_depth
// [end config]
//
// From the AMD_conservative_depth spec:
//     Within any shader, the first redeclarations of gl_FragDepth must appear
//     before any use of gl_FragDepth.

#version 130
#extension GL_AMD_conservative_depth: require

float f() {
    gl_FragDepth = 0.0;
    return 0.0;
}

layout (depth_any) out float gl_FragDepth;
