/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * [end config]
 *
 * From page 19 (page 25 of the PDF) of the GLSL 1.20 spec:
 *
 *     "Arrays declared as formal parameters in a function declaration
 *     must specify a size."
 */
#version 120

vec4 a_function(vec4 [] p);

uniform vec4 [6] an_array;

void main()
{
  gl_Position = a_function(an_array);
}
