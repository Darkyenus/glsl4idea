// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// Declare a uint constant expression.
//
// From section 4.3.3 of the GLSL 1.30 spec:
//     An integral constant expression is a constant expression that evaluates
//     to a scalar signed or unsigned integer.

#version 130

const uint x = 0u;

float f() {
	return 0.0;
}
