// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Vertex inputs cannot be declared 'smooth'.
//
// From section 4.3 of the GLSL 1.30 spec:
//     [Interpolation qualifiers] also do not apply to inputs into a vertex
//     shader or outputs from a fragment shader.


#version 130

smooth in float x;

float f() {
	return 0.0;
}
