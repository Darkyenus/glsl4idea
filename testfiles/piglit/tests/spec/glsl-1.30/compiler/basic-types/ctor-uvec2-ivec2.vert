/* [config]
 * expect_result: pass
 * glsl_version: 1.30
 * [end config]
 */
#version 130

void main()
{
	ivec2 x = ivec2(5);
	uvec2 y = uvec2(x);

	gl_Position = vec4(x.xy, y.xy);
}
