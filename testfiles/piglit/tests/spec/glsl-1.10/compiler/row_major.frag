// [config]
// expect_result: pass
// glsl_version: 1.10
// [end config]
//
// Check that 'row_major' is not a reserved keyword.

#version 110

int f()
{
	int row_major;
	return 0;
}
