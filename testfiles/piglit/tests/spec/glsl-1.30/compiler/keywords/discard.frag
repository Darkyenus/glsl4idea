// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'discard' is a keyword.

#version 130

int f()
{
	int discard;
	return 0;
}
