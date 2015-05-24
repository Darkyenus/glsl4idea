/* [config]
 * expect_result: fail
 * glsl_version: 3.00 es
 * [end config]
 *
 * The GLSL ES 3.00 spec says:
 *
 *     "All indexes used to index a uniform block array must be constant
 *     integral expressions."
 */
#version 300 es

uniform transform_data {
  mat4 mvp;
} camera[2];

uniform int idx;

in vec4 position;

void main()
{
  gl_Position = camera[idx].mvp * position;
}
