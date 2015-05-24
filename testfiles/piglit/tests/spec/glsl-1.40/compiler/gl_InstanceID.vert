// [config]
// expect_result: pass
// glsl_version: 1.40
// [end config]
#version 140

int func()
{
	return gl_InstanceID;
}
