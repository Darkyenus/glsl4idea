/* [config]
 * expect_result: pass
 * glsl_version: 1.30
 * [end config]
 */
#version 130

void main()
{
	uvec4 x = uvec4(5);
	ivec4 y = ivec4(x);

	gl_Position = vec4(y);
}
