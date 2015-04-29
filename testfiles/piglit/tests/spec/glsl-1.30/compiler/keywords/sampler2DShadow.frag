// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'sampler2DShadow' is a keyword.

#version 130

int f()
{
	int sampler2DShadow;
	return 0;
}
