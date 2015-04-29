// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'image3D' is a reserved keyword.

#version 130

int f()
{
	int image3D;
	return 0;
}
