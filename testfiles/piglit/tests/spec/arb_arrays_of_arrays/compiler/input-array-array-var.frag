/* [config]
 * expect_result: pass
 * glsl_version: 1.50
 * require_extensions: GL_ARB_arrays_of_arrays
 * [end config]
 */
#version 150
#extension GL_ARB_arrays_of_arrays: enable

in vec4[3][1] an_array;

void main()
{
  gl_FragColor = an_array[0][0];
}
