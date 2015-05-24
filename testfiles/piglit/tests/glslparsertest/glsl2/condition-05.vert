// [config]
// expect_result: pass
// glsl_version: 1.20
//
// [end config]

#version 120
/* PASS */

uniform bool a;
uniform int b;

void main()
{
  float x;

  x = (a) ? 2.0 : b;
  gl_Position = vec4(x);
}
