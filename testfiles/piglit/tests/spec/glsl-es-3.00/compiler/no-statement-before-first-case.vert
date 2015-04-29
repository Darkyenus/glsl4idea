#version 300 es

/* [config]
 * expect_result: fail
 * glsl_version: 3.00
 * [end config]
 *
 * Page 80 of the OpenGL ES Shading Language 3.00 spec says:
 *
 *     "No statements are allowed in a switch statement before the first case
 *     statement."
 */

uniform int x;

void main()
{
  switch (x) {
    gl_Position = vec4(0.);
  default:
    gl_Position = vec4(1.);
    break;
  }
}
