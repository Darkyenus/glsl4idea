// [config]
// expect_result: fail
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

#version 150
#extension GL_ARB_gpu_shader5: require

uniform sampler2D s2D;
uniform ivec2[4] offsets;

void main()
{
	gl_Position = textureGatherOffsets(s2D, vec2(0), offsets);
}
