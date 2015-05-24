/* [config]
 * expect_result: fail
 * glsl_version: 1.10
 * [end config]
 *
 * From page 49 of the GLSL 1.10 spec:
 *
 *     "If a shader statically assigns a value to gl_FragColor, it may not
 *     assign a value to any element of gl_FragData. If a shader statically
 *     writes a value to any element of gl_FragData, it may not assign a value
 *     to gl_FragColor. That is, a shader may assign values to either
 *     gl_FragColor or gl_FragData, but not both."
 *
 * Since these are compile time errors and are based on static assignments,
 * the check must happen before any dead-code removal or other optimizations.
 */
void main()
{
	if (false)
		gl_FragColor = vec4(1.0);
	gl_FragData[0] = vec4(1.0);
}
