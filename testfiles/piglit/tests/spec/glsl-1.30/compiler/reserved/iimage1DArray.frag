// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'iimage1DArray' is a reserved keyword.

#version 130

int f()
{
	int iimage1DArray;
	return 0;
}
