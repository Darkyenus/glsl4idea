// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Precision qualifiers cannot be applied to structs.
//
// This test declares the struct, but none of its members, with a precision
// qualifier.
//
// See section 4.5.2 of the GLSL 1.30 spec.

#version 130

highp struct s {
	float a;
};

float f() {
	return 0.0;
}
