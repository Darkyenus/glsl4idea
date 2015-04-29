// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

#version 150
#extension GL_ARB_gpu_shader5: require

uniform sampler2D s2D;
uniform ivec2 offset;		/* ARB_gpu_shader5 allows this to be uniform
				   rather than constexpr */

void main()
{
	vec4 res = vec4(0);

	res += textureGatherOffset(s2D,	vec2(0), offset);

	gl_Position = res;
}
