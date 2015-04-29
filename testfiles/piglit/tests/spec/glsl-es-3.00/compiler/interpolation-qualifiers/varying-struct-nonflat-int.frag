/* [config]
 * expect_result: fail
 * glsl_version: 3.00
 * check_link: true
 * [end config]
 *
 * From the GLSL ES 3.00 spec, Section 4.3.4 ("Input Variables"):
 *
 *     "Fragment shader inputs that are, or contain, signed or
 *     unsigned integers or integer vectors must be qualified with the
 *     interpolation qualifier flat."
 *
 * This test verifies that a non-flat varying struct containing
 * signed integral data is properly flagged as an error.
 */

#version 300 es

struct S {
	int i;
};

in S foo;
out highp vec4 color;

void main()
{
	color = vec4(foo.i);
}
