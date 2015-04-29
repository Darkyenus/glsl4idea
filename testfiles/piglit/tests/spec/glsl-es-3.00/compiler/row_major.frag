// [config]
// expect_result: pass
// glsl_version: 3.00
// [end config]
//
// Check that 'row_major' is not a reserved keyword.

#version 300 es

int f()
{
	int row_major;
	return 0;
}
