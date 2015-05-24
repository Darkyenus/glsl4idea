// [config]
// expect_result: fail
// glsl_version: 1.00
// [end config]
//
// Precision qualifiers cannot be applied to integer literals.
//
// From section 4.5.2 of the GLSL 1.00 spec:
//     Literal constants do not have precision qualifiers.

#version 100

void f() {
	lowp int x = lowp 1;
}
