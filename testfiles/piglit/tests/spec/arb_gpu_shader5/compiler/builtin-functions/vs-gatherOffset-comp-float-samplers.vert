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

const ivec2 offset = ivec2(-8, 7);

void main()
{
	vec4 res = vec4(0);

	res += textureGatherOffset(s2D,			vec2(0), offset, 0);
	res += textureGatherOffset(s2DArray,		vec3(0), offset, 1);
	res += textureGatherOffset(s2DRect,		vec2(0), offset, 2);

	gl_Position = res;
}
