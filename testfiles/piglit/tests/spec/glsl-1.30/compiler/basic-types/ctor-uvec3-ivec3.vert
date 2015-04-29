/* [config]
 * expect_result: pass
 * glsl_version: 1.30
 * [end config]
 */
#version 130

void main()
{
	ivec3 x = ivec3(5);
	uvec3 y = uvec3(x);

	gl_Position = vec4(x.xy, y.xy);
}
