#version 300 es

/* [config]
 * expect_result: fail
 * glsl_version: 3.00
 * [end config]
 *
 * Page 35 of the OpenGL ES Shading Language 3.00 spec says:
 *
 *     "Arrays have a fixed number of elements. This can be obtained
 *     by using the length method:
 *
 *         a.length();
 *         // returns 5 for the above declarations
 *
 *     The return value is a constant signed integral expression. The
 *     precision is determined using the same rules as for literal
 *     integers."
 */

uniform vec4 a[7];
uniform int i;

void main()
{
  gl_Position = vec4(uint(a[i].x) + a.length());
}
