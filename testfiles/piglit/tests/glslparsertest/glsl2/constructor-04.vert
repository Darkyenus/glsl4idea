// [config]
// expect_result: fail
// glsl_version: 1.20
//
// [end config]

#version 120
/* FAIL - matrix must be only parameter to matrix constructor */

uniform mat2 a;
uniform float x;

void main()
{
  mat2 b;

  b = mat2(a, x);

  gl_Position = gl_Vertex;
}
