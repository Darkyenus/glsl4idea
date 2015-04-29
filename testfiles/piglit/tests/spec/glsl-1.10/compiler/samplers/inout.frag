/* [config]
 * expect_result: fail
 * glsl_version: 1.10
 * [end config]
 *
 * From page 17 (page 23 of the PDF) of the GLSL 1.20 spec:
 *
 *     "Samplers cannot be treated as l-values; hence cannot be used
 *     as out or inout function parameters..."
 *
 * The GLSL 1.10 spec does not state this rule specifically, but it is
 * clear from context that it is intended.
 */
void f(inout sampler2D p)
{
}

void main()
{
	gl_FragColor = vec4(1.0);
}
