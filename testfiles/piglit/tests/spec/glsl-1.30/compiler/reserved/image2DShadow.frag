// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'image2DShadow' is a reserved keyword.

#version 130

int f()
{
	int image2DShadow;
	return 0;
}
