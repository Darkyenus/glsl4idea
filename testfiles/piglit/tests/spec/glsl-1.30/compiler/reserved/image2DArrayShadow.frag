// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'image2DArrayShadow' is a reserved keyword.

#version 130

int f()
{
	int image2DArrayShadow;
	return 0;
}
