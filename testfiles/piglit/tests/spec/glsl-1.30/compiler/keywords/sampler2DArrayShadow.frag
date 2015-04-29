// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'sampler2DArrayShadow' is a keyword.

#version 130

int f()
{
	int sampler2DArrayShadow;
	return 0;
}
