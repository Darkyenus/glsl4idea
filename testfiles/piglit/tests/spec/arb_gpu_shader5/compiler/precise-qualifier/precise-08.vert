// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

// test that `precise inout` is allowed on a function parameter

#version 150
#extension GL_ARB_gpu_shader5: require

void foo(precise inout float x) {
	x += 1;
}
