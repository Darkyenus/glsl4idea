// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader5
// [end config]

// Test implicit conversions from ARB_gpu_shader5 in function parameter handling.


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


void f_uint(uint x) {}
void f_uvec2(uvec2 x) {}
void f_uvec3(uvec3 x) {}
void f_uvec4(uvec4 x) {}

void f_float(float x) {}
void f_vec2(vec2 x) {}
void f_vec3(vec3 x) {}
void f_vec4(vec4 x) {}


void test() {

	/* int can be converted to uint and to float (and for vectors of same) */
	f_uint(_int);
	f_float(_int);

	f_uvec2(_ivec2);
	f_vec2(_ivec2);

	f_uvec3(_ivec3);
	f_vec3(_ivec3);

	f_uvec4(_ivec4);
	f_vec4(_ivec4);

	/* uint can be converted to float (and for vectors of same) */
	f_float(_uint);

	f_vec2(_uvec2);

	f_vec3(_uvec3);

	f_vec4(_uvec4);
}
