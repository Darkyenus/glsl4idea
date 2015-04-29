// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

// test that a straightforward redeclaration of a user-defined variable
// at global scope is allowed.

#version 150
#extension GL_ARB_gpu_shader5: require

vec4 x;
precise x;
