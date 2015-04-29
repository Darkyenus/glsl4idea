#version 150

/* [config]
 * expect_result: fail
 * glsl_version: 1.50
 * [end config]
 *
 * Page 66 of the OpenGL Shading Language 1.50 spec says:
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
