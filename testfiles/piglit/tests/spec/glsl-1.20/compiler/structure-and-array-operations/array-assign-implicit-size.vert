/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * [end config]
 *
 * From page 20 (page 26 of the PDF) of the GLSL 1.20 spec:
 *
 *     "However, implicitly sized arrays cannot be assigned to.  Note, this is
 *     a rare case that initializers and assignments appear to have different
 *     semantics."
 */
#version 120

uniform float a[5];

void main()
{
	float b[];
	b = a;

	gl_Position = vec4(0);
}
