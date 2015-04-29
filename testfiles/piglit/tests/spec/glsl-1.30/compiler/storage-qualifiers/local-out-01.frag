// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Declare a local variable with 'out'.
//
// From section 4.3.6 of the GLSL 1.30 spec:
//     "Output variables must be declared at global scope."

#version 130

float f() {
	out float x;
	return x;
}
