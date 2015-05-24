// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'sampler3DRect' is a reserved keyword.

#version 130

int f()
{
	int sampler3DRect;
	return 0;
}
