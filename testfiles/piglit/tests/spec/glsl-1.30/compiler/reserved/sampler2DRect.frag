// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'sampler2DRect' is a reserved keyword.

#version 130

int f()
{
	int sampler2DRect;
	return 0;
}
