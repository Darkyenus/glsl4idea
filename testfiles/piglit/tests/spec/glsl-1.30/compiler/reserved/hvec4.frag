// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'hvec4' is a reserved keyword.

#version 130

int f()
{
	int hvec4;
	return 0;
}
