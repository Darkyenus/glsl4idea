// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'samplerBuffer' is a reserved keyword.

#version 130

int f()
{
	int samplerBuffer;
	return 0;
}
