// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Attempt to declare a vertex global with 'inout'.
//
// From section 4.3.6 of the GLSL 1.30 spec:
//     "There is not an inout storage qualifier at global scope [...]".

#version 130

inout float x;

float f() {
	return 0.0;
}
