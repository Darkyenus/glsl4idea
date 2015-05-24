// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'image1DShadow' is a reserved keyword.

#version 130

int f()
{
	int image1DShadow;
	return 0;
}
