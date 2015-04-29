// [config]
// expect_result: fail
// glsl_version: 1.40
// [end config]
//
// Check that 'isamplerBuffer' is a reserved keyword.

#version 140

int f()
{
	int isamplerBuffer;
	return 0;
}
