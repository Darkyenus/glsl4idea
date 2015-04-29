// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

// test that a precise redeclaration of a parameter after use is not allowed.

#version 150
#extension GL_ARB_gpu_shader5: require

void foo(out x) {
	x = 1;
	precise x;	/* redeclaration after use */
}
