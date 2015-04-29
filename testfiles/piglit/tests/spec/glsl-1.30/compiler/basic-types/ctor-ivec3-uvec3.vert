/* [config]
 * expect_result: pass
 * glsl_version: 1.30
 * [end config]
 */
#version 130

void main()
{
	uvec3 x = uvec3(5);
	ivec3 y = ivec3(x);

	gl_Position = vec4(x.xy, y.xy);
}
