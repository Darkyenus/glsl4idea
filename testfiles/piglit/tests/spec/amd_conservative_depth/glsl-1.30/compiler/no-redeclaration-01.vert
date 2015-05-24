// [config]
// expect_result: pass
// glsl_version: 1.30
// require_extensions: GL_AMD_conservative_depth
// [end config]
//
// Require AMD_conservative_depth in the vertex shader, but don't use any of its
// features.
//
// The AMD_conservative_depth spec says nothing of vertex shaders, and does not
// explicitly restrict the extension to the fragment shader, so it shouldn't be
// an error to require the extension here.

#version 130
#extension GL_AMD_conservative_depth: require

float f() {
    return 0.0;
}
