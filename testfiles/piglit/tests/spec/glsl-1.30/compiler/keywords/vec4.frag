// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'vec4' is a keyword.

#version 130

int f()
{
	int vec4;
	return 0;
}
