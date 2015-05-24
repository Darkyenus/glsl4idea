// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'sampler1DShadow' is a keyword.

#version 130

int f()
{
	int sampler1DShadow;
	return 0;
}
