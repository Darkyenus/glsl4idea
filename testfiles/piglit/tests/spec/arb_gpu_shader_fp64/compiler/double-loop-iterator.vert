// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader_fp64
// [end config]
//
// Test that double can be used as a loop iterator

#version 150
#extension GL_ARB_gpu_shader_fp64 : enable

void test() {

	double k;
	vec4 vertex;

	for (k = 0.0lf; k < 1.0lf; k += 0.1lf) {
		vertex.x += k;
	}

	gl_Position = vertex;
}
