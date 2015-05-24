// [config]
// expect_result: fail
// glsl_version: 1.40
// [end config]
#version 140

float func()
{
	return gl_FogCoord;
}
