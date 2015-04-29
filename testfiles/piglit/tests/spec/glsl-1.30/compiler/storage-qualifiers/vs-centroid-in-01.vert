// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that vertex inputs cannot be declared as 'centroid in'.
//
// From page 31 (37 of PDF) of the GLSL 1.30 spec:
//
//    It is an error to use centroid in in a vertex shader.


#version 130

centroid in float x;

float f() {
	return x;
}
