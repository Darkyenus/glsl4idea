// [config]
// expect_result: pass
// glsl_version: 1.50
// check_link: false
// [end config]
//
// Tests that arrays (of matrices) can be inputs to the vertex shader
/*
* Section 4.3.4 (Inputs) of the GLSLLangSpec.1.50.09 4.3.4 Inputs says:
*
* Vertex shader inputs can only be float, floating-point
* vectors, matrices, signed and unsigned integers and integer vectors.
* Vertex shader inputs can also form arrays of these types, but not
* structures.
*
*/

#version 150

in mat3 a[2];
in mat4 b[2];

void main()
{
	gl_Position = vec4(
			a[0][0].x + a[0][1].x +
			a[1][0].x + a[1][1].x +
			b[0][0].x + b[0][1].x +
			b[1][0].x + b[1][1].x
			);
}
