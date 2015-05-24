// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'uvec2' is a keyword.

#version 130

int f()
{
	int uvec2;
	return 0;
}
