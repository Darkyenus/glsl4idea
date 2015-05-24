/* [config]
 * expect_result: fail
 * glsl_version: 3.00 es
 * [end config]
 *
 * The GLSL ES 3.00 spec says:
 *
 *     "For blocks declared as arrays, the array index must also be included
 *     when accessing members..."
 */
#version 300 es

uniform transform_data {
  mat4 mvp;
} camera[2];

in vec4 position;

void main()
{
  gl_Position = camera.mvp * position;
}
