// [config]
// expect_result: pass
// glsl_version: 1.30
// require_extensions: GL_AMD_conservative_depth
// [end config]
//
// Require AMD_conservative_depth in the fragment shader, but don't use any of its
// features.

#version 130
#extension GL_AMD_conservative_depth: require

float f() {
    return 0.0;
}
