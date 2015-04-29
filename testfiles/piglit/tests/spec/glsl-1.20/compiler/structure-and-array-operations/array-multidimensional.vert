/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * [end config]
 *
 * From page 19 (page 25 of the PDF) of the GLSL 1.20 spec:
 *
 *     "Only one-dimensional arrays may be declared."
 */
#version 120

uniform vec4 an_array[1][1];

void main()
{
  gl_Position = an_array[0][0];
}
