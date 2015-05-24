/* [config]
 * expect_result: pass
 * glsl_version: 1.00
 * [end config]
 *
 * From "Appendix A" of the GLSL ES 1.00 spec:
 *
 *     "GLSL ES 1.00 supports both arrays of samplers and arrays of
 *      structures which contain samplers. In both these cases, for
 *      ES 2.0, support for indexing with a constant-index-expression
 *      is mandated"
 *
 */
#version 100
uniform sampler2D array[2];

void main()
{
	highp vec4 color;
	for (int i = 0; i < 2; i++) {
		color += texture2D(array[i], vec2(0.0));
	}
	gl_FragColor = color;
}
