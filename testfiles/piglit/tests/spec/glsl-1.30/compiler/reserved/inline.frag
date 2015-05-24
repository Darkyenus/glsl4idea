// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'inline' is a reserved keyword.

#version 130

int f()
{
	int inline;
	return 0;
}
