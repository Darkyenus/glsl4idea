// [config]
// expect_result: fail
// glsl_version: 1.30
// [end config]
//
// Check that 'dvec3' is a reserved keyword.

#version 130

int f()
{
	int dvec3;
	return 0;
}
