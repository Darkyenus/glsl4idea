// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'isampler3D' is a keyword.

#version 130

int f()
{
	int isampler3D;
	return 0;
}
