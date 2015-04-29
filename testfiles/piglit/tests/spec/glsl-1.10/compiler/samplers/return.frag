/* [config]
 * expect_result: fail
 * glsl_version: 1.10
 * [end config]
 *
 * From page 19 (page 25 of the PDF) of the GLSL 1.10 spec:
 *
 *     "[Samplers] can only be declared as function parameters or uniforms
 *     (see Section 4.3.5 "Uniform")."
 */
uniform sampler2D u;

sampler2D f()
{
	return u;
}

void main()
{
	gl_FragColor = vec4(1.0);
}
