/* [config]
 * expect_result: pass
 * glsl_version: 1.20
 * require_extensions: GL_ARB_arrays_of_arrays
 * [end config]
 */
#version 120
#extension GL_ARB_arrays_of_arrays: enable

uniform vec4[4] an_array[2];

void main()
{
  gl_Position = an_array[1][3];
}
