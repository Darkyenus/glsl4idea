// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'mat3' is a keyword.

#version 130

int f()
{
	int mat3;
	return 0;
}
