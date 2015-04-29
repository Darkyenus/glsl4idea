// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'isampler1DArray' is a keyword.

#version 130

int f()
{
	int isampler1DArray;
	return 0;
}
