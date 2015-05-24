// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'usampler3D' is a keyword.

#version 130

int f()
{
	int usampler3D;
	return 0;
}
