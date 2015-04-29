// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

#version 150
#extension GL_ARB_gpu_shader5: require

in vec4 y;
sample out vec4 x;

void main()
{
	x = y;
	gl_Position = y;
}
