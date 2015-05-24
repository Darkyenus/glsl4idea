// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

// Test that overloads which differ only by return type are not allowed.

#version 150
#extension GL_ARB_gpu_shader5 : enable

int foo(int x) { return 0; }
float foo(int x) { return 0; }		/* differs only by return type. */
