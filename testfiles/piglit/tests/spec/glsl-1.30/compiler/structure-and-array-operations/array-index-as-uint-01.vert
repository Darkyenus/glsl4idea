// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// Access an array with an uint index.
//
// From section 5.7 of the GLSL 1.30 spec:
//     Array elements are accessed using an expression whose type is int or
//     uint.

#version 130

uniform float a[4];

float f() {
	uint u = 0u;
	float x = a[u];
	return 0.0;
}
