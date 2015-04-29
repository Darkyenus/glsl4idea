// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

// test that a redeclaration of a built-in variable at global scope is allowed.

#version 150
#extension GL_ARB_gpu_shader5: require

precise gl_Position;
