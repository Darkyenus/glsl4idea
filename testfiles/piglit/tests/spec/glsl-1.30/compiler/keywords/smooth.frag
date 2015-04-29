// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'smooth' is a keyword.

#version 130

int f()
{
	int smooth;
	return 0;
}
