/* [config]
 * expect_result: pass
 * glsl_version: 1.50
 * require_extensions: GL_ARB_arrays_of_arrays
 * [end config]
 */
#version 150
#extension GL_ARB_arrays_of_arrays: enable

uniform ArraysOfArraysBlock
{
  vec4 a;
} i[4][5];

void main()
{
  gl_Position = i[3][4].a;
}
