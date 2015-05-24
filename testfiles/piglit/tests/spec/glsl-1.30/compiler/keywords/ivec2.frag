// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'ivec2' is a keyword.

#version 130

int f()
{
	int ivec2;
	return 0;
}
