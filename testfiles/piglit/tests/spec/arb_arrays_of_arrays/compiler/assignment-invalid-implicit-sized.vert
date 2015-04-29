/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * require_extensions: GL_ARB_arrays_of_arrays
 * [end config]
 *
 * From page 20 (page 26 of the PDF) of the GLSL 1.20 spec:
 *
 *     "However, implicitly sized arrays cannot be assigned to.  Note, this is
 *     a rare case that initializers and assignments appear to have different
 *     semantics."
 */
#version 120
#extension GL_ARB_arrays_of_arrays: enable

void main()
{
  float [][3] an_array;
  float [3][3] an_array2;

  an_array[0][2] = 0.0;
  an_array[1][2] = 1.0;
  an_array[2][2] = 2.0;

  an_array = an_array2;

  gl_Position = vec4(an_array[2][2]);
}
