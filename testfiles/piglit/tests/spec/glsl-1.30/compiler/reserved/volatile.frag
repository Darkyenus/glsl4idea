// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'volatile' is a reserved keyword.

#version 130

int f()
{
	int volatile;
	return 0;
}
