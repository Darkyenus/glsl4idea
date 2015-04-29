// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'varying' is a keyword.

#version 130

int f()
{
	int varying;
	return 0;
}
