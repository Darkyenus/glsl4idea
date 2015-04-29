// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Declare a local varaible with 'in'.
//
// From section 4.3.4 of the GLSL 1.30 spec:
//     Input variables must be declared at global scope.

#version 130

float f() {
	in float x;
	return x;
}
