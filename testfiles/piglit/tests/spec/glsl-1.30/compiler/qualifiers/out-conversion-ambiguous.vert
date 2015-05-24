/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * [end config]
 *
 * Test that the GLSL compiler properly detects an error in the case
 * where the conversion of an out parameter causes a function call to
 * be ambiguous.
 *
 * From the GLSL 1.30 spec, p55 (Function Definitions):
 *
 *   For example,
 *
 *     vec4 f(in vec4 x, out  vec4 y);
 *     vec4 f(in vec4 x, out ivec4 y); // okay, different argument type
 *     ...
 *
 *   Calling the first two functions above with the following argument
 *   types yields
 *
 *     ...
 *     f(ivec4, vec4)  // error, convertible to both
 *
 * This test verifies that the call f(ivec4, vec4) generates an error.
 */

#version 130

vec4 f(in vec4 x, out vec4 y)
{
  y = vec4(0.0);
  return vec4(0.0);
}

vec4 f(in vec4 x, out ivec4 y)
{
  y = ivec4(0);
  return vec4(0.0);
}

void main()
{
  ivec4 x_actual = ivec4(1, 3, 3, 7);
  vec4 y_actual;
  vec4 f_result = f(x_actual, y_actual);
  gl_Position = f_result + y_actual;
}
