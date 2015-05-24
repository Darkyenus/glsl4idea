/* From page 21 (page 27 of the PDF) of the GLSL 1.10 spec:
 *
 *    "If an array is indexed with an expression that is not an integral
 *    constant expression or passed as an argument to a function, then its
 *    size must be declared before any such use."
 *
 * [config]
 * expect_result: fail
 * glsl_version: 1.10
 * [end config]
 */
uniform int i;
varying vec4 color;

void main()
{
	float a[];

	// These assignments will implicitly size a.
	a[0] = 0.0;
	a[1] = 1.0;
	a[2] = 2.0;
	a[3] = 3.0;

	color = vec4(a[i]);
}
