// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Precision qualifiers cannot be applied to float literals.
//
// From section 4.5.2 of the GLSL 1.30 spec:
//     Literal constants do not have precision qualifiers.

#version 130

float f() {
	highp float x = highp 0.0;
	return 0.0;
}
