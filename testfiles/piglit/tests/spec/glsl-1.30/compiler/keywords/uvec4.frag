// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'uvec4' is a keyword.

#version 130

int f()
{
	int uvec4;
	return 0;
}
