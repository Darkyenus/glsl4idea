// [config]
// expect_result: pass
// glsl_version: 1.40
// [end config]
#version 140

float func()
{
	return gl_ClipDistance[0];
}
