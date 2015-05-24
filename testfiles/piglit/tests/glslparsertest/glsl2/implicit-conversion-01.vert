/* [config]
 * expect_result: pass
 * glsl_version: 1.20
 * [end config]
 */
#version 120

void main()
{
  float f = 8;
  vec2 v2 = ivec2(1);
  vec3 v3 = ivec3(1);
  vec4 v4 = ivec4(1);
  gl_Position = vec4(0.0, 0.0, 0.0, 1.0);
}
