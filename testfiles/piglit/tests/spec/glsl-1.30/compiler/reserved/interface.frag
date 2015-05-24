// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'interface' is a reserved keyword.

#version 130

int f()
{
	int interface;
	return 0;
}
