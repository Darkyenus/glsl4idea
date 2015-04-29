// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

#version 150
#extension GL_ARB_gpu_shader5: require

uniform usampler2D s2D;
uniform usampler2DArray s2DArray;
uniform usampler2DRect s2DRect;

const ivec2 offset = ivec2(-8, 7);

void main()
{
	uvec4 res = uvec4(0);

	res += textureGatherOffset(s2D,		vec2(0), offset);
	res += textureGatherOffset(s2DArray,	vec3(0), offset);
	res += textureGatherOffset(s2DRect,	vec2(0), offset);

	gl_FragColor = vec4(res);
}
