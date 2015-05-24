// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5 GL_ARB_texture_cube_map_array
// [end config]

#version 150
#extension GL_ARB_gpu_shader5: require
#extension GL_ARB_texture_cube_map_array: require

uniform isampler2D s2D;
uniform isampler2DArray s2DArray;
uniform isamplerCube sCube;
uniform isamplerCubeArray sCubeArray;
uniform isampler2DRect s2DRect;

void main()
{
	ivec4 res = ivec4(0);

	res += textureGather(s2D,		vec2(0), 0);
	res += textureGather(s2DArray,		vec3(0), 1);
	res += textureGather(sCube,		vec3(0), 2);
	res += textureGather(sCubeArray,	vec4(0), 3);
	res += textureGather(s2DRect,		vec2(0), 0);

	gl_FragColor = vec4(res);
}
