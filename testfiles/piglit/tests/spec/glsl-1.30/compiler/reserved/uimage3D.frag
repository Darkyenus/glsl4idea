// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'uimage3D' is a reserved keyword.

#version 130

int f()
{
	int uimage3D;
	return 0;
}
