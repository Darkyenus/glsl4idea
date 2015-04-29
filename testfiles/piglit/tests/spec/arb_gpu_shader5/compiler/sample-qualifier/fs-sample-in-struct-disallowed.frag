// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

#version 150
#extension GL_ARB_gpu_shader5: require

struct things {
	/* not allowed in struct declarations */
	sample vec4 x;
};
out vec4 out_color;

void main()
{
	out_color = vec4(1);
}

