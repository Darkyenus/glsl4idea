// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// Declare a non-flat integral vertex output.
//
// From section 4.3.6 of the GLSL 1.30 spec:
//     "If a vertex output is a signed or unsigned integer or integer vector,
//     then it must be qualified with the interpolation qualifier flat."
//
// However, in GLSL 1.50, this requirement is shifted to fragment
// inputs rather than vertex outputs, to accommodate the possibility
// of geometry shaders.  Since many implementations extend geometry
// shader support back before GLSL 1.50 (via ARB_geometry_shader4), it
// seems sensible to regard this change as a bug fix rather than a
// deliberate behavioural difference, and therefore test for the GLSL
// 1.50 behaviour even in GLSL 1.30.

#version 130

out uint x;

float f() {
	return 0.0;
}
