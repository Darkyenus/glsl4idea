// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'extern' is a reserved keyword.

#version 130

int f()
{
	int extern;
	return 0;
}
