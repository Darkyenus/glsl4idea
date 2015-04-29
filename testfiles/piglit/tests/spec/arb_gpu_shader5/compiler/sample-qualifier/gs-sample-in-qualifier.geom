// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

#version 150
#extension GL_ARB_gpu_shader5: require

sample in vec4 x;	/* not allowed */

void main()
{
	gl_Position = x;
	EmitVertex();
}
