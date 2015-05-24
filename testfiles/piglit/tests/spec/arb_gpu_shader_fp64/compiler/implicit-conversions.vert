// [config]
// expect_result: pass
// glsl_version: 1.50
// require_extensions: GL_ARB_gpu_shader_fp64
// [end config]
//
// Test that implicit conversions are allowed as specified in GL_ARB_gpu_shader_fp64
//
// GL_ARB_gpu_shader_fp64 spec presents following conversion table:
//
//                             Can be implicitly
//     Type of expression        converted to
//     ---------------------   -------------------
//     int                     uint(*), float, double
//     ivec2                   uvec2(*), vec2, dvec2
//     ivec3                   uvec3(*), vec3, dvec3
//     ivec4                   uvec4(*), vec4, dvec4
//
//     uint                    float, double
//     uvec2                   vec2, dvec2
//     uvec3                   vec3, dvec3
//     uvec4                   vec4, dvec4
//
//     float                   double
//     vec2                    dvec2
//     vec3                    dvec3
//     vec4                    dvec4
//
//     mat2                    dmat2
//     mat3                    dmat3
//     mat4                    dmat4
//     mat2x3                  dmat2x3
//     mat2x4                  dmat2x4
//     mat3x2                  dmat3x2
//     mat3x4                  dmat3x4
//     mat4x2                  dmat4x2
//     mat4x3                  dmat4x3
//
//     (*) if ARB_gpu_shader5 or NV_gpu_shader5 is supported
//

#version 150
#extension GL_ARB_gpu_shader_fp64 : enable

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

double _double;
dvec2 _dvec2;
dvec3 _dvec3;
dvec4 _dvec4;

mat2 _mat2 = mat2(1.0);
mat3 _mat3 = mat3(1.0);
mat4 _mat4 = mat4(1.0);
mat2x3 _mat2x3 = mat2x3(1.0);
mat2x4 _mat2x4 = mat2x4(1.0);
mat3x2 _mat3x2 = mat3x2(1.0);
mat3x4 _mat3x4 = mat3x4(1.0);
mat4x2 _mat4x2 = mat4x2(1.0);
mat4x3 _mat4x3 = mat4x3(1.0);

dmat2 _dmat2;
dmat3 _dmat3;
dmat4 _dmat4;
dmat2x3 _dmat2x3;
dmat2x4 _dmat2x4;
dmat3x2 _dmat3x2;
dmat3x4 _dmat3x4;
dmat4x2 _dmat4x2;
dmat4x3 _dmat4x3;

void test() {

	/* int can be converted to double (and for vectors of same) */
	_double = _int;
	_dvec2 = _ivec2;
	_dvec3 = _ivec3;
	_dvec4 = _ivec4;

	/* uint can be converted to double (and for vectors of same) */
	_double = _uint;
	_dvec2 = _uvec2;
	_dvec3 = _uvec3;
	_dvec4 = _uvec4;

	/* float can be converted to double (and for vectors of same) */
	_double = _float;
	_dvec2 = _vec2;
	_dvec3 = _vec3;
	_dvec4 = _vec4;

	/* mat -> dmat conversions */
	_dmat2 = _mat2;
	_dmat3 = _mat3;
	_dmat4 = _mat4;
	_dmat2x3 = _mat2x3;
	_dmat2x4 = _mat2x4;
	_dmat3x2 = _mat3x2;
	_dmat3x4 = _mat3x4;
	_dmat4x2 = _mat4x2;
	_dmat4x3 = _mat4x3;
}
