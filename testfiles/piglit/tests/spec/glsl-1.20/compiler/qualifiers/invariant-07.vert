// [config]
// expect_result: fail
// glsl_version: 1.20
// [end config]
//
// test that `invariant` redeclaration after the first use of the variable is
// not allowed.

#version 120

varying vec4 x;
x = vec4(0);
invariant x;		/* redeclaration after use! */

void main()
{
  gl_Position = vec4(0);
}
