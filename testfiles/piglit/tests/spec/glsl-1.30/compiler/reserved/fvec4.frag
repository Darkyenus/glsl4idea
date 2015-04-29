// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'fvec4' is a reserved keyword.

#version 130

int f()
{
	int fvec4;
	return 0;
}
