// [config]
// expect_result: fail
// glsl_version: 1.20
// require_extensions: GL_ARB_geometry_shader4
// [end config]

#version 120
#extension GL_ARB_geometry_shader4: enable

varying in float gs_input[3][1];

varying out float gs_out;

void main()
{
	/* Constant indexing of arrays with known size should do a bounds check.
	 * The bounds check on this to operation should fail.
	 */
	float s = gs_input[0][1];

	for (int i = 0; i < gl_VerticesIn; i++) {
		gs_out = s;

		gl_Position = gl_PositionIn[i];
		EmitVertex();
	}
}

