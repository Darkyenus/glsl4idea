// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'mat3x2' is a keyword.

#version 130

int f()
{
	int mat3x2;
	return 0;
}
