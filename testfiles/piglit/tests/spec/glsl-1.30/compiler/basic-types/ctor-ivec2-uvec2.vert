/* [config]
 * expect_result: pass
 * glsl_version: 1.30
 * [end config]
 */
#version 130

void main()
{
	uvec2 x = uvec2(5);
	ivec2 y = ivec2(x);

	gl_Position = vec4(x.xy, y.xy);
}
