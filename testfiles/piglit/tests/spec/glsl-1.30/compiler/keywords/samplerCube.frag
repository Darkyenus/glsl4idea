// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'samplerCube' is a keyword.

#version 130

int f()
{
	int samplerCube;
	return 0;
}
