/* [config]
 * expect_result: pass
 * glsl_version: 1.30
 * [end config]
 */
#version 130

void main()
{
	int x = 5;
	uint y = uint(x);

	gl_Position = vec4(x, y, x, y);
}
