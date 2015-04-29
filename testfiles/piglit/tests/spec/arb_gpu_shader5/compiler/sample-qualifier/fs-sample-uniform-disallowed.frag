// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

#version 150
#extension GL_ARB_gpu_shader5: require

sample uniform vec4 x;
out vec4 out_color;

void main()
{
	out_color = x;
}

