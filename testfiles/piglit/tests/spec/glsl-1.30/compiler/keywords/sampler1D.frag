// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'sampler1D' is a keyword.

#version 130

int f()
{
	int sampler1D;
	return 0;
}
