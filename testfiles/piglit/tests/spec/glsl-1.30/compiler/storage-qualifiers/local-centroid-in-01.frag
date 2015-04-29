// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'centroid in' cannot be used a local variable qualifer.

#version 130

float f() {
	centroid in float x = 0;
	return x;
}
