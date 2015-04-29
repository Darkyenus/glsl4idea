// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// Integer literals that are too large should raise an error.
//
// From page 22 (28 of PDF) of GLSL 1.30 spec:
//     It is an error to provide a literal integer whose magnitude is too
//     large to store in a variable of matching signed or unsigned type.
//
//     Unsigned integers have exactly 32 bits of precision.  Signed integers
//     use 32 bits, including a sign bit, in two's complement form.
//
// However, a 32-bit integer literal (whether it has a 'u' suffix or not)
// is valid.  Warnings generation for large signed decimals has to be careful,
// because -2147483648 is parsed as -(2147483648).

#version 130

int f() {
	// Requires 32 bits.
	return -2147483648;
}
