// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// Vertex outputs can have type 'flat out int' and 'flat out uint'.
//
// From section 4.3.6 of the GLSL 1.30 spec:
//     "If a vertex output is a signed or unsigned integer or integer vector,
//     then it must be qualified with the interpolation qualifier flat."

#version 130

flat out int x;
flat out uint y;

float f() {
	return 0.0;
}
