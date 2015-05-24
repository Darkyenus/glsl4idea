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
  vec4 a[2][4];
  vec4[2][4] b[3];

  b[0] = a;

  gl_Position = b[0][1][1];
}
