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

attribute vec4 a;
attribute vec4 b;

uniform int i;

void main()
{
  vec4 [] an_array;

  an_array[0] = a;
  an_array[1] = vec4(0);
  an_array[2] = b;

  gl_Position = an_array[i];
}
