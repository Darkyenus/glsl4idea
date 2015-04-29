// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Attempt to declare a local varaible with 'smooth'.

#version 130

float f() {
	smooth float x = 0;
	return x;
}
