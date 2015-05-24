/* [config]
 * expect_result: fail
 * glsl_version: 1.50
 * [end config]
 *
 * Section 4.3.7(Interface Blocks) of the GLSL 1.50 spec says:
 *
 *     "All indexes used to index a uniform block array must be constant
 *     integral expressions."
 */
#version 150

uniform transform_data {
  mat4 mvp;
} camera[2];

uniform int idx;

in vec4 position;

void main()
{
  gl_Position = camera[idx].mvp * position;
}
