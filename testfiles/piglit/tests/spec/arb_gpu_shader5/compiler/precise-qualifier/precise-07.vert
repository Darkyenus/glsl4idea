// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

// notable departure from the `invariant` qualifier rules: it seems reasonable
// to have local precise redeclarations be allowed.

#version 150
#extension GL_ARB_gpu_shader5: require

void foo() {
	vec4 x;
	precise x;
}
