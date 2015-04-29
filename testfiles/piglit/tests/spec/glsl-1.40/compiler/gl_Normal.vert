// [config]
// expect_result: fail
// glsl_version: 1.40
// [end config]
#version 140

vec3 func()
{
	return gl_Normal;
}
