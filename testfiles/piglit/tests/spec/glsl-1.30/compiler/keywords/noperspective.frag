// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'noperspective' is a keyword.

#version 130

int f()
{
	int noperspective;
	return 0;
}
