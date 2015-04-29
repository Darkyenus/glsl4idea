// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

#version 150
#extension GL_ARB_gpu_shader5: require

uniform sampler2D s2D;
uniform sampler2DArray s2DArray;
uniform sampler2DRect s2DRect;

uniform isampler2D is2D;
uniform isampler2DArray is2DArray;
uniform isampler2DRect is2DRect;

uniform usampler2D us2D;
uniform usampler2DArray us2DArray;
uniform usampler2DRect us2DRect;

uniform sampler2DShadow ss2D;
uniform sampler2DArrayShadow ss2DArray;
uniform sampler2DRectShadow ss2DRect;

const ivec2[] offsets = ivec2[](
	ivec2(-8, 7),
	ivec2(-8, -8),
	ivec2(7, -8),
	ivec2(7, 7)
);

const float refz = 0.5;

void main()
{
	vec4 res = vec4(0);

	/* float samplers, no component select */
	res += textureGatherOffsets(s2D,		vec2(0), offsets);
	res += textureGatherOffsets(s2DArray,		vec3(0), offsets);
	res += textureGatherOffsets(s2DRect,		vec2(0), offsets);

	/* float samplers, component select */
	res += textureGatherOffsets(s2D,		vec2(0), offsets, 0);
	res += textureGatherOffsets(s2DArray,		vec3(0), offsets, 1);
	res += textureGatherOffsets(s2DRect,		vec2(0), offsets, 2);

	/* int samplers, no component select */
	res += textureGatherOffsets(is2D,		vec2(0), offsets);
	res += textureGatherOffsets(is2DArray,		vec3(0), offsets);
	res += textureGatherOffsets(is2DRect,		vec2(0), offsets);

	/* int samplers, component select */
	res += textureGatherOffsets(is2D,		vec2(0), offsets, 0);
	res += textureGatherOffsets(is2DArray,		vec3(0), offsets, 1);
	res += textureGatherOffsets(is2DRect,		vec2(0), offsets, 2);

	/* uint samplers, no component select */
	res += textureGatherOffsets(us2D,		vec2(0), offsets);
	res += textureGatherOffsets(us2DArray,		vec3(0), offsets);
	res += textureGatherOffsets(us2DRect,		vec2(0), offsets);

	/* uint samplers, component select */
	res += textureGatherOffsets(us2D,		vec2(0), offsets, 0);
	res += textureGatherOffsets(us2DArray,		vec3(0), offsets, 1);
	res += textureGatherOffsets(us2DRect,		vec2(0), offsets, 2);

	/* shadow samplers, no component select (none available) */
	res += textureGatherOffsets(ss2D,		vec2(0), refz, offsets);
	res += textureGatherOffsets(ss2DArray,		vec3(0), refz, offsets);
	res += textureGatherOffsets(ss2DRect,		vec2(0), refz, offsets);

	gl_Position = res;
}
