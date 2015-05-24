// [config]
// expect_result: fail
// glsl_version: 1.40
// [end config]
#version 140

void func()
{
	gl_BackColor = vec4(0.0);
}
