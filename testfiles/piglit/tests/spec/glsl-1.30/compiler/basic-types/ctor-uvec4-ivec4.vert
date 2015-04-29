/* [config]
 * expect_result: pass
 * glsl_version: 1.30
 * [end config]
 */
#version 130

void main()
{
	ivec4 x = ivec4(5);
	uvec4 y = uvec4(x);

	gl_Position = vec4(y);
}
