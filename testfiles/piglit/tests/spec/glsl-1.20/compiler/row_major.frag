// [config]
// expect_result: pass
// glsl_version: 1.20
// [end config]
//
// Check that 'row_major' is not a reserved keyword.

#version 120

int f()
{
	int row_major;
	return 0;
}
