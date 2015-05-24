/* From page 44 (page 50 of the PDF) of the GLSL 1.30 spec:
 *
 *    "Array elements are accessed using an expression whose type is int or uint."
 *
 * [config]
 * expect_result: pass
 * glsl_version: 1.30
 * [end config]
 */
#version 130
flat in uint i;
out vec4 color;

const float a[4] = float[4](0.0, 1.0, 2.0, 3.0);

void main()
{
	color = vec4(a[i]);
}
