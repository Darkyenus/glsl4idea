/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * [end config]
 *
 * Section 6.1 (Function Definitions) of the GLSL 1.20 spec says:
 *
 *     "Arrays are allowed as arguments and as the return type. In
 *     both cases, the array must be explicitly sized."
 */
#version 120

vec4[] a_function(vec4 [6] p);

uniform vec4 [6] an_array;

void main()
{
  gl_Position = a_function(an_array)[0];
}
