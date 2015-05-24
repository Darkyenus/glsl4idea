/* From page 33 (page 39 of the PDF) of the GLSL 1.10 spec:
 *
 *     "The equality operators equal (==), and not equal (!=) operate on all
 *     types except arrays."
 *
 * [config]
 * expect_result: fail
 * glsl_version: 1.10
 * [end config]
 */
varying vec4 color;
void main()
{
	int a[4];
	int b[4];

	a[0] = 0;
	a[1] = 1;
	a[2] = 2;
	a[3] = 3;
	b[0] = 0;
	b[1] = 1;
	b[2] = 2;
	b[3] = 3;

	color = float(b == a) * vec4(0, 1, 0, 1);
}
