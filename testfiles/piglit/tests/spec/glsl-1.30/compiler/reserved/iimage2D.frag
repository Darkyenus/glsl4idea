// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'iimage2D' is a reserved keyword.

#version 130

int f()
{
	int iimage2D;
	return 0;
}
