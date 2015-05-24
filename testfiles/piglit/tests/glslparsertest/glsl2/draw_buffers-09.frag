// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - out of bounds access of gl_FragData */
#version 110

uniform vec4 a;

void main()
{
  gl_FragData[0] = a;
  gl_FragData[gl_MaxDrawBuffers] = a;
}
