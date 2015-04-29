// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

// test that `precise` is allowed on globals.

#version 150
#extension GL_ARB_gpu_shader5: require

precise vec4 x;
