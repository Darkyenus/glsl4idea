// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'mat2x4' is a keyword.

#version 130

int f()
{
	int mat2x4;
	return 0;
}
