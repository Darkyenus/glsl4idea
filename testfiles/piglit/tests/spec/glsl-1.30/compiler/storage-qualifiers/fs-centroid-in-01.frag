// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// Check that fragment inputs can be declared as 'centroid in'.

#version 130

centroid in float x;

float f() {
	return x;
}
