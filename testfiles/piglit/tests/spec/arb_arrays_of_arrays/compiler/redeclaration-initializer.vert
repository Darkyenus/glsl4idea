/* [config]
 * expect_result: pass
 * glsl_version: 1.20
 * require_extensions: GL_ARB_arrays_of_arrays
 * [end config]
 *
 * From page 19 (page 25 of the PDF) of the GLSL 1.20 spec:
 *
 *     "It is legal to declare an array without a size and then later
 *     re-declare the same name as an array of the same type and specify a
 *     size."
 */
#version 120
#extension GL_ARB_arrays_of_arrays: enable

float a_function(float[3][2]);

void main()
{
  float [][2] an_array;

  an_array[0][1] = 0.0;
  an_array[1][1] = 1.0;
  an_array[2][0] = 2.0;

  float [][2] an_array = float[][2](float[2](0.0, 1.0),
                                    float[2](0.0, 1.0),
                                    float[2](0.0, 1.0));

  gl_Position = vec4(a_function(an_array));
}
