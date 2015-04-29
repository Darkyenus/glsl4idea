// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'break' is a keyword.

#version 130

int f()
{
	int break;
	return 0;
}
