/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * [end config]
 *
 * From page 19 (page 25 of the PDF) of the GLSL 1.20 spec:
 *
 *     "It is also illegal to index an array with a negative constant
 *     expression."
 */
#version 120

uniform vec4 [6] an_array;

void main()
{
  gl_Position = an_array[-1];
}
