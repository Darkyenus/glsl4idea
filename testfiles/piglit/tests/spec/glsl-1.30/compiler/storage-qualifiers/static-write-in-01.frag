// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// From page 31 (37 of pdf) of the GLSL 1.30 spec:
//     Variables declared as in or centroid in may not be written
//     to during shader execution.


#version 130

in float x;

float f() {
	x = 0.0;
	return x;
}
