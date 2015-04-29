// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - :? condition is not bool scalar */

uniform float a;

void main()
{
  gl_Position = (a) ? vec4(1.0, 0.0, 0.0, 1.0) : vec4(0.0, 1.0, 0.0, 1.0);
}
