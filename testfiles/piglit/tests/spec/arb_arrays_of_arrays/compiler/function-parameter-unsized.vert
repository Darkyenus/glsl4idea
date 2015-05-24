/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * require_extensions: GL_ARB_arrays_of_arrays
 * [end config]
 *
 * From page 19 (page 25 of the PDF) of the GLSL 1.20 spec:
 *
 *     "Arrays declared as formal parameters in a function declaration
 *     must specify a size."
 */
#version 120
#extension GL_ARB_arrays_of_arrays: enable

vec4 a_function(vec4 [][6] p);

uniform vec4 [9][6] an_array;

void main()
{
  gl_Position = a_function(an_array);
}
