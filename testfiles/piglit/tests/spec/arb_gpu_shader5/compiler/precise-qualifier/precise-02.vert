// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

// test that `precise` is allowed on shader outputs.

#version 150
#extension GL_ARB_gpu_shader5: require

precise out vec4 x;
