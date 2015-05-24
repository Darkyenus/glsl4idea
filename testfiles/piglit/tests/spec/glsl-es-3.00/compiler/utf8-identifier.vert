#version 300 es

/* [config]
 * expect_result: fail
 * glsl_version: 3.00
 * [end config]
 *
 * While UTF-8 is allowed inside comments, sections 3.1 and 3.8 of the OpenGL
 * ES Shading Language 3.00 spec say that only ASCII characters are allowed
 * elsewhere.
 */

uniform float föö;

void main()
{
  gl_Position = vec4(föö);
}
