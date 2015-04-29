// [config]
// expect_result: fail
// glsl_version: 1.40
// [end config]
#version 140

int func()
{
	return gl_MaxClipPlanes;
}
