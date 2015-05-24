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
 * This test tries to trick the compiler by writing to gl_FragColor and an
 * 'out' parameter of a function.  This is valid.
 */
#version 130

void function(out vec4 f)
{
	f = vec4(1.0);
}

void main()
{
	vec4 v;

	function(v);
	gl_FragData[0] = v;
}
