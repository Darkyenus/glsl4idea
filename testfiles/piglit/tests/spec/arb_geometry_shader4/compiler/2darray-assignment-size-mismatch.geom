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
	/* Assignment operations of arrays should fail if the arrays don't have
	 * the same size.
	 */
	float a[3] = gs_input[0];

	for (int i = 0; i < gl_VerticesIn; i++) {
		gs_out = a[0];

		gl_Position = gl_PositionIn[i];
		EmitVertex();
	}
}

