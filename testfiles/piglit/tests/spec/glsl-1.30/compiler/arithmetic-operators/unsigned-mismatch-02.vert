// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Add together an int and an uint, expecint uint as result.
//
// From section 5.9 of the GLSL 1.30 spec:
//     If the operands [to a binary arithmetic operator] are integer types,
//     they must both be signed or both be unsigned.

#version 130

float f() {
	int x = 0;
	uint y = 0u;
	uint z = x + y;
	return 0.0;
}
