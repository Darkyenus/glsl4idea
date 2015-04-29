/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * [end config]
 */
#version 120

void main()
{
  ivec2 i = vec2(1.2);
  gl_Position = vec4(0.0, 0.0, 0.0, 1.0);
}
