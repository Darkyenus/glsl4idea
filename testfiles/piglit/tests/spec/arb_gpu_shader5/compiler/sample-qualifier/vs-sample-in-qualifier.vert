// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

#version 150
#extension GL_ARB_gpu_shader5: require

in vec4 y;
sample in vec4 x;	/* this is not allowed */

void main()
{
	gl_Position = y;
}
