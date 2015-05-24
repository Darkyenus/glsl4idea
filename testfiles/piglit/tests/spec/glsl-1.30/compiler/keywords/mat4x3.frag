// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'mat4x3' is a keyword.

#version 130

int f()
{
	int mat4x3;
	return 0;
}
