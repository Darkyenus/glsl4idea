// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'continue' is a keyword.

#version 130

int f()
{
	int continue;
	return 0;
}
