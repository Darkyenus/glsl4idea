// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'sampler2DRectShadow' is a reserved keyword.

#version 130

int f()
{
	int sampler2DRectShadow;
	return 0;
}
