// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'usampler2D' is a keyword.

#version 130

int f()
{
	int usampler2D;
	return 0;
}
