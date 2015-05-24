// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS */
#version 110

uniform vec4 a;

void main()
{
  gl_FragData[0] = a;
  gl_FragData[gl_MaxDrawBuffers - 1] = a;
}
