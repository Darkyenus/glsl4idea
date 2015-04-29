// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

// test that precise redeclaration after the first use of a variable is not allowed.

#version 150
#extension GL_ARB_gpu_shader5: require

float x;
x = 1;
precise x;	/* redeclaration after use */
