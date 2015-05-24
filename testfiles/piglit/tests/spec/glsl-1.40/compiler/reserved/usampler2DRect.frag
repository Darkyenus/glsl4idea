// [config]
// expect_result: fail
// glsl_version: 1.40
// [end config]
//
// Check that 'usampler2DRect' is a reserved keyword.

#version 140

int f()
{
	int usampler2DRect;
	return 0;
}
