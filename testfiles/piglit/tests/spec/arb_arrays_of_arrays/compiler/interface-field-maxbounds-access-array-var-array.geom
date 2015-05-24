/* [config]
 * expect_result: pass
 * glsl_version: 1.50
 * require_extensions: GL_ARB_arrays_of_arrays
 * [end config]
 */
#version 150
#extension GL_ARB_arrays_of_arrays: enable

in ArraysOfArraysBlock
{
  vec4[2] a[3];
} i[];

void main()
{
  gl_Position = i[0].a[2][1];
}
