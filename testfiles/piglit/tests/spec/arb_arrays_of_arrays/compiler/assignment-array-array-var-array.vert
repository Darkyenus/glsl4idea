/* [config]
 * expect_result: pass
 * glsl_version: 1.20
 * require_extensions: GL_ARB_arrays_of_arrays
 * [end config]
 */
#version 120
#extension GL_ARB_arrays_of_arrays: enable

void main()
{
  vec4 a[3][2][4];
  vec4[2][4] b[3] = a;

  gl_Position = a[2][1][1];
}
