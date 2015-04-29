// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

// Test that implicit conversions are allowed as specified in ARB_gpu_shader5.


#version 150
#extension GL_ARB_gpu_shader5 : enable

int _int = 0;
ivec2 _ivec2 = ivec2(0);
ivec3 _ivec3 = ivec3(0);
ivec4 _ivec4 = ivec4(0);

uint _uint = 0u;
uvec2 _uvec2 = uvec2(0u);
uvec3 _uvec3 = uvec3(0u);
uvec4 _uvec4 = uvec4(0u);

float _float = 0.0f;
vec2 _vec2 = vec2(0.0f);
vec3 _vec3 = vec3(0.0f);
vec4 _vec4 = vec4(0.0f);

void test() {

	/* int can be converted to uint and to float (and for vectors of same) */
	_uint = _int;
	_float = _int;

	_uvec2 = _ivec2;
	_vec2 = _ivec2;

	_uvec3 = _ivec3;
	_vec3 = _ivec3;

	_uvec4 = _ivec4;
	_vec4 = _ivec4;

	/* uint can be converted to float (and for vectors of same) */
	_float = _uint;

	_vec2 = _uvec2;

	_vec3 = _uvec3;

	_vec4 = _uvec4;
}
