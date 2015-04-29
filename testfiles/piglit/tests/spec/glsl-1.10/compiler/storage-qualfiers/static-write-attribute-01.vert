// [config]
// expect_result: fail
// glsl_version: 1.10
// [end config]
//
// From section 4.3.4 of the GLSL 1.10 spec:
//     Attribute variables are read-only as far as the vertex shader is
//     concerned.

#version 110

attribute float x;

float f() {
	x = 0.0;
	return x;
}
