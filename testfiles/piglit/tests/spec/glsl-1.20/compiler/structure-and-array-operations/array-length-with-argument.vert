/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * [end config]
 */
#version 120

uniform vec4 a[2];

void main()
{
  gl_Position = vec4(a.length(5));
}
