// [config]
// expect_result: pass
// glsl_version: 1.20
// [end config]
//
// test that a straightforward redeclaration of a user-defined varying
// as invariant works.
#version 120

varying vec4 x;
invariant x;

void main()
{
  gl_Position = vec4(0);
}
