// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

// From the ARB_gpu_shader5 spec:
// "Variables declared as ..., or sample in may
// not be written to during shader execution."

#version 150
#extension GL_ARB_gpu_shader5: require

sample in vec4 x;
out vec4 out_color;

void main()
{
	x = vec4(0);	/* not allowed */
	out_color = vec4(1);
}

