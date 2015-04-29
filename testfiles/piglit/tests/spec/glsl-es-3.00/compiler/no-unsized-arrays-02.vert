#version 300 es

/* [config]
 * expect_result: fail
 * glsl_version: 3.00
 * [end config]
 *
 * Page 35 of the OpenGL ES Shading Language 3.00 spec says:
 *
 *     "An array declaration which leaves the size unspecified is an
 *     error."
 */

void main()
{
  float a[];

  gl_Position = vec4(0.);
}
