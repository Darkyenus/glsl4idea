// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'bvec4' is a keyword.

#version 130

int f()
{
	int bvec4;
	return 0;
}
