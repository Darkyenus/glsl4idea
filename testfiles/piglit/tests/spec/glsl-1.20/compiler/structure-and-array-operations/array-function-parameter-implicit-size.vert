/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * [end config]
 *
 * From page 19 (page 25 of the PDF) of the GLSL 1.20 spec:
 *
 *     "If an array is indexed with an expression that is not an integral
 *     constant expression, or if an array is passed as an argument to a
 *     function, then its size must be declared before any such use."
 */
#version 120

float a_function(vec4[6]);

void main()
{
  vec4 [] an_array;

  an_array[0] = vec4(0);
  an_array[1] = vec4(1);
  an_array[2] = vec4(2);
  an_array[3] = vec4(3);
  an_array[4] = vec4(4);
  an_array[5] = vec4(5);

  gl_Position = vec4(a_function(an_array));
}
