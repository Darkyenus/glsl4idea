/* [config]
 * expect_result: fail
 * glsl_version: 1.30
 * [end config]
 *
 * From page 68 of the GLSL 1.30 spec:
 *
 *     "Similarly, if user declared output variables are in use (statically
 *     assigned to), then the built-in variables gl_FragColor and gl_FragData
 *     may not be assigned to. These incorrect usages all generate compile
 *     time errors."
 *
 * Since these are compile time errors and are based on static assignments,
 * the check must happen before any dead-code removal or other optimizations.
 */
#version 130

out vec4 frag_color;

void main()
{
	gl_FragData[0] = vec4(1.0);
	if (false)
		frag_color = vec4(1.0);
}
