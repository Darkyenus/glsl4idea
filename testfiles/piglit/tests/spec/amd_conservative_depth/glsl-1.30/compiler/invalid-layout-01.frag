// [config]
// expect_result: fail
// glsl_version: 1.30
// require_extensions: GL_AMD_conservative_depth
// [end config]

#version 130
#extension GL_AMD_conservative_depth: require

layout (invalid_layout) out float gl_FragDepth;

float f() {
    return 0.0;
}
