// [config]
// expect_result: pass
// glsl_version: 1.10
// require_extensions: GL_ARB_geometry_shader4
// [end config]

#version 110
#extension GL_ARB_geometry_shader4: enable

varying in float scalar[];
varying in float scalar_array[][2];
varying in vec4 vector[];
varying in vec4 vector_array[][2];
varying in mat4 matrix[];
varying in mat4 matrix_array[][2];

varying out float var_out;

void main()
{
	float x = 0.0;

	x += scalar[0];
	x += scalar_array[0][1];
	x += vector[0][3];
	x += vector_array[0][1][3];
	x += matrix[0][3][3];
	x += matrix_array[0][1][3][3];

	for (int i = 0; i < 3; i++) {
		var_out = x;
		gl_Position = vec4(0.0);
		EmitVertex();
	}
}
