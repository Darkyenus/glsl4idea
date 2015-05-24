// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'ivec4' is a keyword.

#version 130

int f()
{
	int ivec4;
	return 0;
}
