// [config]
// expect_result: pass
// glsl_version: 1.40
// [end config]
#version 140

void func()
{
	gl_FragData[0] = vec4(0.0);
}
