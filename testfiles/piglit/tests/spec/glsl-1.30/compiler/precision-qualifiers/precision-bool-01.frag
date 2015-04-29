// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Precision qualifiers cannot be applied to bool variables.
//
// From section 4.5.2 of the GLSL 1.30 spec:
//     Literal constants do not have precision qualifiers. Neither do Boolean
//     variables.

#version 130

float f() {
	highp bool b;
	return 0.0;
}
