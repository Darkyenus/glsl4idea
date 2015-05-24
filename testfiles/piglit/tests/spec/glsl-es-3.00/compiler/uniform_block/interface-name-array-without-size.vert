/* [config]
 * expect_result: fail
 * glsl_version: 3.00 es
 * [end config]
 *
 * The GLSL ES 3.00 spec says:
 *
 *     "As the array size indicates the number of buffer objects needed,
 *     uniform block array declarations must specify an array size."
 */
#version 300 es

uniform transform_data {
  mat4 mvp;
} camera[];

in vec4 position;

void main()
{
  gl_Position = camera[0].mvp * position;
}
