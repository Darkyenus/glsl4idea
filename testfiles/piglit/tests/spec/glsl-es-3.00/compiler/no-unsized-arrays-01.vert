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
  /* This is valid in desktop GL.  The initial declaration lacks a
   * size, but a later re-declaration provides one.  GLSL ES does not
   * allow this.
   */
  float a[];

  float a[5];

  gl_Position = vec4(0.);
}
