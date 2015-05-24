// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'uimage2DArray' is a reserved keyword.

#version 130

int f()
{
	int uimage2DArray;
	return 0;
}
