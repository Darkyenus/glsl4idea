// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'centroid in' cannot be used a parameter qualifer.

#version 130

float f(centroid in float x) {
	return x;
}
