// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'row_major' is a reserved keyword.

#version 130

int f()
{
	int row_major;
	return 0;
}
