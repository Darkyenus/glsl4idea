// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'usampler1D' is a keyword.

#version 130

int f()
{
	int usampler1D;
	return 0;
}
