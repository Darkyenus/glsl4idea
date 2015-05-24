// [config]
// expect_result: pass
// glsl_version: 1.20
// require_extensions: GL_ARB_geometry_shader4
// [end config]

#version 120
#extension GL_ARB_geometry_shader4: enable

uniform int zero;

varying in float gs_input1[][6];
varying in float gs_input2[gl_VerticesIn][6];

varying out float scalar;
varying out float array[6];

float fs(float input_)
{
	return input_;
}

float fa(float input_[6])
{
	return input_[zero];
}

void main()
{
	/* check implicitly sized input */
	{
	/* check assignments to local variables */
	float a1[] = gs_input1[zero];
	float a2[6] = gs_input1[zero];
	float s = gs_input1[zero][zero];

	/* check passing as function parameters */
	fa(gs_input1[zero]);
	fs(gs_input1[zero][zero]);

	/* check assignments to output variables */
	array = gs_input1[zero];
	scalar = gs_input1[zero][zero];
	}


	/* check explicitly sized input */
	{
	/* check assignments to local variables */
	float a1[] = gs_input2[zero];
	float a2[6] = gs_input2[zero];
	float s = gs_input2[zero][zero];

	/* check passing as function parameters */
	fa(gs_input2[zero]);
	fs(gs_input2[zero][zero]);

	/* check assignments to output variables */
	array = gs_input2[zero];
	scalar = gs_input2[zero][zero];
	}
}

