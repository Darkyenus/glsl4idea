// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

// test that precise redeclarations of function parameters are allowed.

#version 150
#extension GL_ARB_gpu_shader5: require

void foo(out float x) {
	precise x;
	x = 1;
}
