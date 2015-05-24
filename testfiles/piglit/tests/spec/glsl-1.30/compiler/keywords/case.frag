// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'case' is a keyword.

#version 130

int f()
{
	int case;
	return 0;
}
