// [config]
// expect_result: pass
// glsl_version: 1.30
// [end config]
//
// Check that the preprocessor concatenation operator behaves correctly when
// no space exists between the token and the operator.
//
// The GLSL 1.30 spec does not specify whether this syntax is supported.
// However, the C preprocessor does support this syntax.

#version 130
#define INT i##n##t

int f()
{
	INT x;
	return 0;
}
