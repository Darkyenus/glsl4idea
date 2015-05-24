/* [config]
 * expect_result: pass
 * glsl_version: 1.30
 * [end config]
 */
#version 130

void main()
{
	uint x = 5u;
	int y = int(x);

	gl_Position = vec4(x, y, x, y);
}
