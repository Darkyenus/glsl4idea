// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'out' is a keyword.

#version 130

int f()
{
	int out;
	return 0;
}
