// [config]
// expect_result: fail
// glsl_version: 1.10
//
// [end config]

/* FAIL - type mismatch in assignment
 */
uniform float alpha;

void main()
{
	gl_Position = gl_Vertex;

	/* This reproduces the assertion failure reported in bugzilla #30623.
	 */
	vec3 c = vec4(vec3(0.0, 1.0, 0.0), alpha);

	gl_FrontColor = vec4(c, 1.0);
}
