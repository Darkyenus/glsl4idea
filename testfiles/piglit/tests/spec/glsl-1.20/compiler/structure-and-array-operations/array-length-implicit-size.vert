/* [config]
 * expect_result: fail
 * glsl_version: 1.20
 * [end config]
 *
 * From page 20 (page 26 of the PDF) of the GLSL 1.20 spec:
 *
 *     "The length method cannot be called on an array that has not been
 *     explicitly sized."
 */
#version 120

void main()
{
	float b[];

	b[2] = 1.0;  // Implicitly size array to have at least 3 elements

	gl_Position = vec4(b.length());
}
