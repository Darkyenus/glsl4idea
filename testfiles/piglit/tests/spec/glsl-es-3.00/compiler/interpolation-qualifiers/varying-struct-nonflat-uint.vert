/* [config]
 * expect_result: fail
 * glsl_version: 3.00
 * check_link: true
 * [end config]
 *
 * From the GLSL ES 3.00 spec, Section 4.3.6 ("Output Variables"):
 *
 *     "Vertex shader outputs that are, or contain, signed or unsigned
 *     integers or integer vectors must be qualified with the
 *     interpolation qualifier flat."
 *
 * This test verifies that a non-flat varying struct containing
 * unsigned integral data is properly flagged as an error.
 */

#version 300 es

struct S {
	uint u;
};

out S foo;

void main()
{
	gl_Position = vec4(0.0);
	foo.u = 1u;
}
