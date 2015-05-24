// [config]
// expect_result: fail
// glsl_version: 1.40
// [end config]
//
// Check that 'isampler2DRect' is a reserved keyword.

#version 140

int f()
{
	int isampler2DRect;
	return 0;
}
