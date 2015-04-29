// [config]
// expect_result: pass
// glsl_version: 1.10
//
// [end config]

/* PASS - type mismatch in assignment
 */
uniform float alpha;

void main()
{
	gl_Position = gl_Vertex;

	/* This reproduces the assertion failure reported in bugzilla #30623.
	 */
	gl_FrontColor = vec4(vec3(0.0, 1.0, 0.0), alpha);
}
