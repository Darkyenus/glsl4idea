// [config]
// expect_result: pass
// glsl_version: 1.20
//
// [end config]

/* PASS */
#version 120

uniform mat2 u_mat2;
uniform vec2 u_vec2;
uniform mat2x2 u_mat2x2;
uniform mat2x3 u_mat2x3;
uniform mat2x4 u_mat2x4;
uniform mat3 u_mat3;
uniform vec3 u_vec3;
uniform mat3x2 u_mat3x2;
uniform mat3x3 u_mat3x3;
uniform mat3x4 u_mat3x4;
uniform mat4 u_mat4;
uniform vec4 u_vec4;
uniform mat4x2 u_mat4x2;
uniform mat4x3 u_mat4x3;
uniform mat4x4 u_mat4x4;

void main()
{
  gl_Position = gl_Vertex;

  mat2   t_mat2   = mat2  (0.0);
  mat2x2 t_mat2x2 = mat2x2(0.0);
  mat2x3 t_mat2x3 = mat2x3(0.0);
  mat2x4 t_mat2x4 = mat2x4(0.0);
  mat3   t_mat3   = mat3  (0.0);
  mat3x2 t_mat3x2 = mat3x2(0.0);
  mat3x3 t_mat3x3 = mat3x3(0.0);
  mat3x4 t_mat3x4 = mat3x4(0.0);
  mat4   t_mat4   = mat4  (0.0);
  mat4x2 t_mat4x2 = mat4x2(0.0);
  mat4x3 t_mat4x3 = mat4x3(0.0);
  mat4x4 t_mat4x4 = mat4x4(0.0);

  t_mat2   = transpose(u_mat2);
  t_mat2x2 = transpose(u_mat2x2);
  t_mat2x3 = transpose(u_mat3x2);
  t_mat2x4 = transpose(u_mat4x2);
  t_mat3   = transpose(u_mat3);
  t_mat3x2 = transpose(u_mat2x3);
  t_mat3x3 = transpose(u_mat3x3);
  t_mat3x4 = transpose(u_mat4x3);
  t_mat4   = transpose(u_mat4);
  t_mat4x2 = transpose(u_mat2x4);
  t_mat4x3 = transpose(u_mat3x4);
  t_mat4x4 = transpose(u_mat4x4);

  t_mat2   = outerProduct(u_vec2, u_vec2);
  t_mat2x2 = outerProduct(u_vec2, u_vec2);
  t_mat2x3 = outerProduct(u_vec3, u_vec2);
  t_mat2x4 = outerProduct(u_vec4, u_vec2);
  t_mat3   = outerProduct(u_vec3, u_vec3);
  t_mat3x2 = outerProduct(u_vec2, u_vec3);
  t_mat3x3 = outerProduct(u_vec3, u_vec3);
  t_mat3x4 = outerProduct(u_vec4, u_vec3);
  t_mat4   = outerProduct(u_vec4, u_vec4);
  t_mat4x2 = outerProduct(u_vec2, u_vec4);
  t_mat4x3 = outerProduct(u_vec3, u_vec4);
  t_mat4x4 = outerProduct(u_vec4, u_vec4);

  t_mat2x2 = matrixCompMult(u_mat2x2, u_mat2x2);
  t_mat2x3 = matrixCompMult(u_mat2x3, u_mat2x3);
  t_mat2x4 = matrixCompMult(u_mat2x4, u_mat2x4);
  t_mat3x2 = matrixCompMult(u_mat3x2, u_mat3x2);
  t_mat3x3 = matrixCompMult(u_mat3x3, u_mat3x3);
  t_mat3x4 = matrixCompMult(u_mat3x4, u_mat3x4);
  t_mat4x2 = matrixCompMult(u_mat4x2, u_mat4x2);
  t_mat4x3 = matrixCompMult(u_mat4x3, u_mat4x3);
  t_mat4x4 = matrixCompMult(u_mat4x4, u_mat4x4);
}
