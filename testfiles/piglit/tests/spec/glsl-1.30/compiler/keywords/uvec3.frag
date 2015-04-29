// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'uvec3' is a keyword.

#version 130

int f()
{
	int uvec3;
	return 0;
}
