// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'void' is a keyword.

#version 130

int f()
{
	int void;
	return 0;
}
