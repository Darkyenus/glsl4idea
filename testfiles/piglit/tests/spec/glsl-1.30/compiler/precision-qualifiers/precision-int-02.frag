// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// Precision qualifiers can be applied to integer vectors.
//
// From section 4.5.2 of the GLSL 1.30 spec:
//     Any floating point or any integer declaration can have the type
//     preceded by one of these precision qualifiers

#version 130

float f() {
	lowp ivec2 v;
	return 0.0;
}
