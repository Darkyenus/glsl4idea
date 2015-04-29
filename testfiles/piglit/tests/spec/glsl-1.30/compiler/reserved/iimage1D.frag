// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'iimage1D' is a reserved keyword.

#version 130

int f()
{
	int iimage1D;
	return 0;
}
