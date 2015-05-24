/* [config]
 * expect_result: pass
 * glsl_version: 1.20
 * require_extensions: GL_ARB_arrays_of_arrays
 * [end config]
 */
#version 120
#extension GL_ARB_arrays_of_arrays: enable

attribute vec4 vert;

void foo(vec4[3][2] x)
{
  gl_Position = vert;
}

void main()
{
  vec4 y[3][2];
  foo(y);
  gl_Position = vert;
}
