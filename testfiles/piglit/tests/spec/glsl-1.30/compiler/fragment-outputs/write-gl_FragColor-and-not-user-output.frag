/* [config]
 * expect_result: pass
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
 * However, the first fix for this at compile time caused an unused
 * out variable to be considered as statically written.
 */
#version 130

out vec4 unused_color;

void main()
{
	gl_FragColor = vec4(1.0);
}
