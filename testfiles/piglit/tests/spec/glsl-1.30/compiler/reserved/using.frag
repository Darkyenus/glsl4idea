// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'using' is a reserved keyword.

#version 130

int f()
{
	int using;
	return 0;
}
