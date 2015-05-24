// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'image1D' is a reserved keyword.

#version 130

int f()
{
	int image1D;
	return 0;
}
