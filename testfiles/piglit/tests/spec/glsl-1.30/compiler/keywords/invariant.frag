// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'invariant' is a keyword.

#version 130

int f()
{
	int invariant;
	return 0;
}
