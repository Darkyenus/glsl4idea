// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

#version 150
#extension GL_ARB_gpu_shader5: require

uniform sampler2DShadow s2D;
uniform sampler2DArrayShadow s2DArray;
uniform sampler2DRectShadow s2DRect;

void main()
{
	vec4 res = vec4(0);

	float refz = 0.5;
	ivec2 offset = ivec2(-8, 7);

	res += textureGatherOffset(s2D,		vec2(0), refz, offset);
	res += textureGatherOffset(s2DArray,	vec3(0), refz, offset);
	res += textureGatherOffset(s2DRect,	vec2(0), refz, offset);

	gl_Position = res;
}
